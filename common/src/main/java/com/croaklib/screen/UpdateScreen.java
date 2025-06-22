package com.croaklib.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class UpdateScreen extends Screen {
	private static final ResourceLocation GUI_ICONS = new ResourceLocation("textures/gui/icons.png");

	private String statusText = "0%";
	private float progress = 0.0F;

	public UpdateScreen() {
		this(Component.literal("Updating"));
	}

	public UpdateScreen(Component title) {
		super(title);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(guiGraphics);

		updateProgress();
		guiGraphics.drawString(this.font, this.statusText, this.width / 2 - 89, this.height / 2 - 10, 0xAAAAAA);
		renderLoadingBar(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	private void renderLoadingBar(GuiGraphics guiGraphics) {
		int centerX = this.width / 2;
		int centerY = this.height / 2;
		int barX = centerX - 91;
		int barY = centerY - 3;

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		guiGraphics.blit(GUI_ICONS, barX, barY, 0, 64, 182, 5);
		int filledWidth = Mth.floor(progress * 182);

		if (filledWidth > 0) {
			guiGraphics.blit(GUI_ICONS, barX, barY, 0, 69, filledWidth, 5);
		}
	}

	private void updateProgress() {
		if (this.progress >= 1.0F && this.minecraft != null) {
//			this.minecraft.stop();
			this.minecraft.setScreen(new UpdateFinishedScreen());
		}

		this.statusText = "Updating (" + Mth.floor(Mth.clamp(this.progress * 100, 0, 100)) + "%)";
	}

	public void setProgress(float progress) {
		this.progress = Mth.clamp(progress, 0.0F, 1.0F);
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}