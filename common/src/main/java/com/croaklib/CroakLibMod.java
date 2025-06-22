package com.croaklib;

import com.croaklib.client.CroakLibModClient;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class CroakLibMod {
    public static final String MOD_ID = "croaklib";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        if (Platform.getEnv() == Env.CLIENT.toPlatform()) {
            CroakLibModClient.init();
        }

//			LOGGER.info("Current version: {}", Platform.getMod(MOD_ID).getVersion());
    }
}
