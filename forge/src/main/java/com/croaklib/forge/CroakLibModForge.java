package com.croaklib.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.croaklib.CroakLibMod;

@Mod(CroakLibMod.MOD_ID)
public final class CroakLibModForge {
    public CroakLibModForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(CroakLibMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        CroakLibMod.init();
    }
}
