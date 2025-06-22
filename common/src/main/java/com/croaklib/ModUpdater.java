package com.croaklib;

import com.croaklib.screen.UpdateScreen;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.platform.Platform;
import net.minecraft.client.Minecraft;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ModUpdater {
	public static int v(String version) {
		if (version.startsWith("v")) {
			version = version.substring(1);
		}

		String[] parts = version.split("\\.");
		if (parts.length != 3) return Integer.MAX_VALUE;

		int major = Integer.parseInt(parts[0]);
		int minor = Integer.parseInt(parts[1]);
		int patch = Integer.parseInt(parts[2]);

		return (major << 16 | minor << 8 | patch);
	}

	public static int currentModVersion() {
		return v(Platform.getMod(CroakLibMod.MOD_ID).getVersion());
	}

	public static int latestGithubVersion(String user, String repo) {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://api.github.com/repos/" + user + "/" + repo + "/releases/latest"))
			.header("Accept", "application/vnd.github.v3+json")
			.timeout(Duration.ofSeconds(10))
			.GET().build();

		try {
			HttpResponse<String> response = CroakLibMod.HTTP.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == 200) {
				JsonObject data = CroakLibMod.GSON.fromJson(response.body(), JsonObject.class);
				if (data.has("tag_name")) return v(data.get("tag_name").getAsString());
			}
		} catch (IOException | InterruptedException ignored) {}

		return 0;
	}

	public static URI latestGithubAsset(String user, String repo) throws FileNotFoundException {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://api.github.com/repos/" + user + "/" + repo + "/releases/latest"))
			.header("Accept", "application/vnd.github.v3+json")
			.timeout(Duration.ofSeconds(10))
			.GET().build();

		try {
			HttpResponse<String> response = CroakLibMod.HTTP.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == 200) {
				JsonObject data = CroakLibMod.GSON.fromJson(response.body(), JsonObject.class);
				if (data.has("assets") && data.has("tag_name")) {
					JsonArray assets = data.getAsJsonArray("assets");
					String version = data.get("tag_name").getAsString();

					for (JsonElement asset : assets) {
						String name = asset.getAsJsonObject().get("name").getAsString();
						if (!name.equals(vModFilename(version))) continue;
						return URI.create(asset.getAsJsonObject().get("browser_download_url").getAsString());
					}
				}
			}
		} catch (IOException | InterruptedException ignored) {}

		throw new FileNotFoundException("Could not find asset for release on this platform or version.");
	}

	public static String vModFilename(String version) {
		if (!Platform.isFabric() && !Platform.isForge()) throw new UnknownError("Platform is unrecognised.");

		if (version.startsWith("v")) {
			version = version.substring(1);
		}

		return String.format(
			"%s-%s-%s.jar",
			CroakLibMod.MOD_ID,
			Platform.isFabric() ? "fabric" : "forge",
			version
		);
	}

	public static String currentModFilename() {
		return vModFilename(Platform.getMod(CroakLibMod.MOD_ID).getVersion());
	}

	public static boolean shouldUpdate() {
		return currentModVersion() < latestGithubVersion("meeplabsdev", CroakLibMod.MOD_ID);
//		return true;
	}

	public static UpdateScreen showUpdateScreen() {
		UpdateScreen updateScreen = new UpdateScreen();
		Minecraft.getInstance().setScreen(updateScreen);
		return updateScreen;
	}

	private static boolean updating = false;
	public static void update() {
		if (!updating) {
			updating = true;
			UpdateScreen updateScreen = showUpdateScreen();
			updateScreen.setProgress(1);
		}
	}

	public static boolean supportsVersion(Path jarFile, String mcVersion) {
		try (JarFile jar = new JarFile(jarFile.toString())) {
			Manifest manifest = jar.getManifest();
			if (manifest != null) {
				List<String> versions = new ArrayList<>();
				Attributes attrs = manifest.getMainAttributes();
				attrs.forEach((key, value) -> {
					if (key.toString().contains("Minecraft-Version")) {
						versions.add(value.toString());
					}
				});

				return versions.contains(mcVersion);
			}
		} catch(IOException ignored) {}

		return false;
	}
}
