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
import java.util.regex.Pattern;

public class ModUpdater {
	private static final int BUFFER_SIZE = 8192;

	private static final List<Updatable> updatables = new ArrayList<>();
	private static volatile boolean updating = false;

	public static void addMod(String modid) {
		CroakLibMod.LOGGER.info("Registered {} to update.", modid);
		updatables.add(new Updatable(modid, modid));
	}

	public static void addMod(String modid, String modrinthid) {
		CroakLibMod.LOGGER.info("Registered {}/{} to update.", modid, modrinthid);
		updatables.add(new Updatable(modid, modrinthid));

	}

	public static synchronized void update() {
		if (updating) return;
		updating = true;

		try {
			UpdateScreen updateScreen = new UpdateScreen();
			Minecraft.getInstance().setScreen(updateScreen);
			Path modsDir = Platform.getGameFolder().resolve("mods");

			int currentUpdatable = 0;
			for (Updatable u : updatables) {
				Path tempFile = Files.createTempFile("mod.update.", ".jar");

				try {
					URI updateAsset = getLatestAsset(u);
					URLConnection connection = updateAsset.toURL().openConnection();
					connection.connect();

					long fileSize = connection.getContentLength();
					try (InputStream inputStream = new BufferedInputStream(connection.getInputStream()); OutputStream outputStream = new BufferedOutputStream(
						Files.newOutputStream(tempFile))) {
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

					if (!shouldUpdate(u)) continue;
					currentUpdatable++;

					if (isCompatible(tempFile)) {
						// Remove any other mods from the folder that are of the same modid and format. Not doing
						// this is also valid, and the mods will work just fine, with the higher version being
						// selected (at least on fabric), but the mod list will quickly become extremely cluttered.

						Pattern filePattern = Pattern.compile(u.modid + "-" + (Platform.isFabric() ? "fabric" : "forge") + "-.*\\.jar");
						try (var paths = Files.list(modsDir)) {
							List<Path> mods = paths.filter(Files::isRegularFile).filter(path -> filePattern.matcher(path.getFileName()
								.toString()).matches()).toList();

							for (Path mod : mods) {
								try {
									Files.deleteIfExists(mod);
								} catch (IOException ignored) { }
							}
						}

						JsonObject release = getLatestRelease(u);
						String version = release.get("version_number").getAsString();
						Path newModPath = modsDir.resolve(generateModFilename(u, version));

						Files.copy(tempFile, newModPath);
					}
				} finally {
					Files.deleteIfExists(tempFile);
				}
			}

			if (currentUpdatable > 0) {
				updateScreen.setProgress(1.0F);
			} else {
				Minecraft.getInstance().setScreen(new TitleScreen());
			}
		} catch (Exception e) {
			Minecraft.getInstance().setScreen(new TitleScreen());
			if (Platform.isDevelopmentEnvironment()) CroakLibMod.LOGGER.error(e.getStackTrace());
		}
	}

	public static boolean shouldUpdate(Updatable u) throws IOException {
		if (Platform.isDevelopmentEnvironment()) return false;
		return getCurrentModVersion(u) < getLatestVersion(u);
	}

	public static URI getLatestAsset(Updatable u) throws IOException {
		JsonObject latest = getLatestRelease(u);
		JsonArray files = latest.get("files").getAsJsonArray();
		if (files.isEmpty()) throw new IOException("No file found for release?");
		if (files.size() == 1) {
			return URI.create(files.get(0).getAsJsonObject().get("url").getAsString());
		} else {
			String desiredPlatform = Platform.isFabric() ? "fabric" : "forge";
			for (JsonElement file : files) {
				String filename = file.getAsJsonObject().get("filename").getAsString();
				if (filename.contains(desiredPlatform)) {
					return URI.create(file.getAsJsonObject().get("url").getAsString());
				}
			}
		}

		throw new IOException("No valid file found?");
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

	public static JsonObject getLatestRelease(Updatable u) throws IOException {
		String platformName = Platform.isFabric() ? "fabric" : "forge";
		String platformVers = Platform.getMinecraftVersion();
		String url =
			"https://api.modrinth.com/v2/project/" + u.modrinthid + "/version?" + "game_versions=[%22" + platformVers + "%22]" + "&loaders=[%22" + platformName + "%22]";

		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.header("User-Agent", "PrismLauncher/9.1")
			.timeout(Duration.ofSeconds(10))
			.GET()
			.build();

		try {
			HttpResponse<String> response = CroakLibMod.HTTP.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				throw new IOException("Modrinth API returned status code: " + response.statusCode());
			}

			JsonArray versions = CroakLibMod.GSON.fromJson(response.body(), JsonArray.class);
			for (JsonElement version : versions) {
				JsonObject data = version.getAsJsonObject();
				if (data.has("files")) {
					return data;
				}
			}

			throw new IOException("No valid version found?");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IOException("Request interrupted", e);
		}
	}

	public static String generateModFilename(Updatable u, String version) {
		if (!Platform.isFabric() && !Platform.isForge()) {
			throw new IllegalStateException("Unsupported platform");
		}

		String cleanVersion = version.startsWith("v") ? version.substring(1) : version;
		String platformName = Platform.isFabric() ? "fabric" : "forge";

		return String.format("%s-%s-%s.jar", u.modid, platformName, cleanVersion);
	}

	public static int getCurrentModVersion(Updatable u) {
		return v(Platform.getMod(u.modid).getVersion());
	}

	public static int getLatestVersion(Updatable u) throws IOException {
		JsonObject latest = getLatestRelease(u);
		return v(latest.get("version_number").getAsString());
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

	public static class Updatable {
		private final String modid;
		private final String modrinthid;

		public Updatable(String modid, String modrinthid) {
			this.modid = modid;
			this.modrinthid = modrinthid;
		}
	}
}