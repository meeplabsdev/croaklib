package com.croaklib;

import com.croaklib.client.CroakLibModClient;
import com.google.gson.Gson;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.http.HttpClient;

public final class CroakLibMod {
    public static final String MOD_ID = "croaklib";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final HttpClient HTTP = HttpClient.newHttpClient();
    public static final Gson GSON = new Gson();

    public static void init() {
        if (Platform.getEnv() == Env.CLIENT.toPlatform()) {
            CroakLibModClient.init();
        }
    }
}
