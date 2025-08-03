package com.croaklib.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

public class UpdateFinishedScreen extends Screen {
	private static final Component TITLE = Component.literal("Update finished");
	private static final Component SUBTITLE = Component.literal("Mods will be reloaded next time you start the game");

	public UpdateFinishedScreen() {
		super(TITLE);
	}

	@Override
	protected void init() {
		super.init();

		int buttonWidth = 120;
		int buttonHeight = 20;
		int buttonSpacing = 10;
		int totalButtonWidth = (buttonWidth * 2) + buttonSpacing;
		int startX = (this.width - totalButtonWidth) / 2;
		int buttonY = this.height / 2 + 40;

		this.addRenderableWidget(Button.builder(
			Component.literal("Restart Now"),
			button -> {
				if (this.minecraft != null) this.minecraft.close();
			}).bounds(startX, buttonY, buttonWidth, buttonHeight).build());

		this.addRenderableWidget(Button.builder(
			Component.literal("Restart Later"),
			button -> {
				if (this.minecraft != null) this.minecraft.setScreen(new TitleScreen());
			}).bounds(startX + buttonWidth + buttonSpacing, buttonY, buttonWidth, buttonHeight).build());
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(guiGraphics);

		guiGraphics.drawCenteredString(
			this.font,
			TITLE,
			this.width / 2,
			this.height / 2 - 40,
			0xFFFFFF
		);

		guiGraphics.drawCenteredString(
			this.font,
			SUBTITLE,
			this.width / 2,
			this.height / 2 - 20,
			0xAAAAAA
		);

		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
