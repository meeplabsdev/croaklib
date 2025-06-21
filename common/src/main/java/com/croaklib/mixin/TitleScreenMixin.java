package com.croaklib.mixin;

import com.croaklib.CroakLibMod;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
	protected TitleScreenMixin(Component title) {
		super(title);
	}

	@Unique
	private static final ResourceLocation FROG_TEXTURE = new ResourceLocation(CroakLibMod.MOD_ID, "textures/gui/frog.png");

	@Inject(method = "render", at = @At("TAIL"))
	private void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
		TitleScreen titleScreen = (TitleScreen)(Object)this;

		Button quitButton = null;
		for (GuiEventListener component : titleScreen.children()) {
			if (component instanceof Button button) {
				if (button.getMessage().getString().toLowerCase().contains("quit")) {
					quitButton = button;
					break;
				}
			}
		}

		if (quitButton != null) {
			int frogX = quitButton.getX() + quitButton.getWidth() - 24;
			int frogY = quitButton.getY() - 9;

			guiGraphics.blit(FROG_TEXTURE, frogX, frogY, 0, 0, 21, 11, 21, 11);
		}
	}
}
