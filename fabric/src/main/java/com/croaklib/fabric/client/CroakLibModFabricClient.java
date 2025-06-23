package com.croaklib.fabric.client;

import com.croaklib.client.CroakLibModClient;
import com.croaklib.client.layer.FrogLayer;
import com.croaklib.client.layer.HatLayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

public final class CroakLibModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
      // Run our common client setup.
      CroakLibModClient.init();

      LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
        (entityType, entityRenderer, registrationHelper, context) -> {
          if (entityRenderer instanceof PlayerRenderer playerRenderer) {
            registrationHelper.register(new FrogLayer(playerRenderer));
            registrationHelper.register(new HatLayer(playerRenderer));
          }
        }
      );
    }
}
