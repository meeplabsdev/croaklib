package com.croaklib;

import com.croaklib.client.CroakLibModClient;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;

public final class CroakLibMod {
    public static final String MOD_ID = "croaklib";

    public static void init() {
        if (Platform.getEnv() == Env.CLIENT.toPlatform()) {
            CroakLibModClient.init();
        }
    }
}
