package com.croaklib.client;

import com.croaklib.CroakLibMod;
import com.croaklib.ModUpdater;

public class CroakLibModClient {
	public static void init() {
		ModUpdater.addMod(CroakLibMod.MOD_ID);
	}
}
