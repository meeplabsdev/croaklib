package com.croaklib;

import com.croaklib.screen.UpdateScreen;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;

import java.io.*;
import java.net.URI;
import java.net.URLConnection;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ModUpdater {
	private static final String GITHUB_API_BASE = "https://api.github.com/repos/";
	private static final String GITHUB_ACCEPT_HEADER = "application/vnd.github.v3+json";
	private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);
	private static final int BUFFER_SIZE = 8192;

	private static final List<Updatable> updatables = new ArrayList<>();
	public static class Updatable {
		private String branch;
		private final String modid;
		private final String user;
		private final String repo;

		public Updatable(String user, String repo, String modid) {
			this.branch = Platform.getMinecraftVersion();
			this.modid = modid;
			this.user = user;
			this.repo = repo;

			String url = GITHUB_API_BASE + user + "/" + repo + "/branches/" + this.branch;
			HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Accept", GITHUB_ACCEPT_HEADER)
				.timeout(REQUEST_TIMEOUT)
				.GET()
				.build();

			try {
				HttpResponse<String> response = CroakLibMod.HTTP.send(request, HttpResponse.BodyHandlers.ofString());
				if (response.statusCode() == 404) this.branch = "main";
			} catch (IOException | InterruptedException e) {
				this.branch = "main";
			}
		}
	}

	public static void addMod(String user, String repo, String modid) {
		CroakLibMod.LOGGER.info("Registered {}/{} ({}) to update.", user, repo, modid);
		updatables.add(new Updatable(user, repo, modid));
	}

	public static int v(String version) {
		if (version == null || version.isEmpty()) {
			return Integer.MAX_VALUE;
		}

		String cleanVersion = version.startsWith("v") ? version.substring(1) : version;
		String[] parts = cleanVersion.split("\\.");

		if (parts.length != 3) {
			return Integer.MAX_VALUE;
		}

		try {
			int major = Integer.parseInt(parts[0]);
			int minor = Integer.parseInt(parts[1]);
			int patch = Integer.parseInt(parts[2]);
			return (major << 16) | (minor << 8) | patch;
		} catch (NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
	}

	public static int getCurrentModVersion(Updatable u) {
		return v(Platform.getMod(u.modid).getVersion());
	}

	public static JsonObject getLatestGithubRelease(Updatable u) throws IOException {
		String url = GITHUB_API_BASE + u.user + "/" + u.repo + "/releases";
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.header("Accept", GITHUB_ACCEPT_HEADER)
			.timeout(REQUEST_TIMEOUT)
			.GET()
			.build();

		try {
			HttpResponse<String> response = CroakLibMod.HTTP.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				throw new IOException("GitHub API returned status code: " + response.statusCode());
			}

			JsonArray releases = CroakLibMod.GSON.fromJson(response.body(), JsonArray.class);
			for (JsonElement release : releases) {
				JsonObject data = release.getAsJsonObject();
				if (data.has("target_commitish") &&
					data.get("target_commitish").getAsString().equals(u.branch) &&
					data.has("assets") &&
					data.has("tag_name")) {
					return data;
				}
			}

			throw new IOException("No valid release found for branch: " + u.branch);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IOException("Request interrupted", e);
		}
	}

	public static int getLatestGithubVersion(Updatable u) throws IOException {
		JsonObject release = getLatestGithubRelease(u);
		return v(release.get("tag_name").getAsString());
	}

	public static URI getLatestGithubAsset(Updatable u) throws IOException {
		JsonObject release = getLatestGithubRelease(u);
		JsonArray assets = release.getAsJsonArray("assets");
		String version = release.get("tag_name").getAsString();
		String expectedFilename = generateModFilename(u, version);

		for (JsonElement asset : assets) {
			JsonObject assetObj = asset.getAsJsonObject();
			String name = assetObj.get("name").getAsString();

			if (expectedFilename.equals(name)) {
				return URI.create(assetObj.get("browser_download_url").getAsString());
			}
		}

		throw new IOException("Asset not found for version: " + version);
	}

	public static String generateModFilename(Updatable u, String version) {
		if (!Platform.isFabric() && !Platform.isForge()) {
			throw new IllegalStateException("Unsupported platform");
		}

		String cleanVersion = version.startsWith("v") ? version.substring(1) : version;
		String platformName = Platform.isFabric() ? "fabric" : "forge";

		return String.format("%s-%s-%s.jar", u.modid, platformName, cleanVersion);
	}

	public static String getCurrentModFilename(Updatable u) {
		return generateModFilename(u, Platform.getMod(u.modid).getVersion());
	}

	public static boolean shouldUpdate(Updatable u) throws IOException {
		if (Platform.isDevelopmentEnvironment()) return false;
		return getCurrentModVersion(u) < getLatestGithubVersion(u);
	}

	public static boolean isCompatible(Path jarFile) {
		try (JarFile jar = new JarFile(jarFile.toString())) {
			Manifest manifest = jar.getManifest();

			if (manifest == null) {
				return false;
			}

			List<String> supportedVersions = new ArrayList<>();
			Attributes attrs = manifest.getMainAttributes();

			attrs.forEach((key, value) -> {
				if (key.toString().contains("Minecraft-Version")) {
					supportedVersions.add(value.toString());
				}
			});

			return supportedVersions.contains(Platform.getMinecraftVersion());
		} catch (IOException e) {
			CroakLibMod.LOGGER.warn("Failed to read JAR manifest: {}", e.getMessage());

			return false;
		}
	}

	private static volatile boolean updating = false;
	public static synchronized void update() {
		if (updating) return;
		updating = true;

		try {
			UpdateScreen updateScreen = new UpdateScreen();
			Minecraft.getInstance().setScreen(updateScreen);

			boolean anything = false;
			int currentUpdatable = 0;
			for (Updatable u : updatables) {
				// TODO: Remove any other mods from the folder that are of the same modid but a lower version than the one
				//  currently running. This allows the user to restart later or at the current point. Not doing this is also
				//  valid, and the mods will work just fine, with the higher version being selected (at least on fabric), but
				//  the mods list will quickly become extremely cluttered.

				currentUpdatable++;
				if (!shouldUpdate(u)) continue;
				Path tempFile = Files.createTempFile("mod.update.", ".jar");

				try {
					URI updateAsset = getLatestGithubAsset(u);
					URLConnection connection = updateAsset.toURL().openConnection();
					connection.connect();

					long fileSize = connection.getContentLength();
					try (InputStream inputStream = new BufferedInputStream(connection.getInputStream());
							 OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(tempFile))) {
						byte[] buffer = new byte[BUFFER_SIZE];
						long totalBytesRead = 0;
						int bytesRead;

						while ((bytesRead = inputStream.read(buffer)) != -1) {
							outputStream.write(buffer, 0, bytesRead);
							totalBytesRead += bytesRead;

							if (fileSize > 0) {
								double progress = (double) totalBytesRead / fileSize;
								updateScreen.setProgress((float) Math.min(progress * 0.9 / updatables.size() * currentUpdatable, 0.9));
							}
						}
					}

					if (isCompatible(tempFile)) {
						JsonObject release = getLatestGithubRelease(u);
						String version = release.get("tag_name").getAsString();

						Path modsDir = Platform.getGameFolder().resolve("mods");
						Path newModPath = modsDir.resolve(generateModFilename(u, version));

						Files.copy(tempFile, newModPath);
						anything = true;
					}
				} finally {
					Files.deleteIfExists(tempFile);
				}
			}

			updateScreen.setProgress(1.0F);
			if (!anything) throw new Exception();
		} catch (Exception e) {
			CroakLibMod.LOGGER.error(e);
			Minecraft.getInstance().setScreen(new TitleScreen());
		}
	}
}