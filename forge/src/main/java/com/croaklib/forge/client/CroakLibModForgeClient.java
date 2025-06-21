package com.croaklib.forge.client;

import com.croaklib.CroakLibMod;
import com.croaklib.client.layer.FrogLayer;
import com.croaklib.client.layer.HatLayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CroakLibMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CroakLibModForgeClient {
	@SubscribeEvent
	public static void onEntityRenderersRegister(EntityRenderersEvent.AddLayers event) {
		LivingEntityRenderer<?, ?> defaultSkin = event.getSkin("default");
		LivingEntityRenderer<?, ?> slimSkin = event.getSkin("slim");

		if (defaultSkin instanceof PlayerRenderer playerRenderer) {
			playerRenderer.addLayer(new FrogLayer(playerRenderer));
			playerRenderer.addLayer(new HatLayer(playerRenderer));
		}

		if (slimSkin instanceof PlayerRenderer playerRendererSlim) {
			playerRendererSlim.addLayer(new FrogLayer(playerRendererSlim));
			playerRendererSlim.addLayer(new HatLayer(playerRendererSlim));
		}
	}
}
