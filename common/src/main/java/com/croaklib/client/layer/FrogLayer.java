package com.croaklib.client.layer;

import com.croaklib.CroakLibMod;
import com.croaklib.client.model.frog.FrogModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class FrogLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	private final FrogModel frogModel;
	private static final ResourceLocation MODEL_TEXTURE = new ResourceLocation(CroakLibMod.MOD_ID, "textures/entity/frog.png");

	public FrogLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent) {
		super(renderLayerParent);
		this.frogModel = new FrogModel(FrogModel.createBodyLayer().bakeRoot());
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight,
										 AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
										 float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

		if (shouldRenderModel(player) && player.isSkinLoaded() && !player.isInvisible()) {
			VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(MODEL_TEXTURE));
			int overlay = LivingEntityRenderer.getOverlayCoords(player, 0.0F);

			poseStack.pushPose();
			poseStack.scale(0.5F, 0.5F, 0.5F);
			poseStack.translate(0.0F, 0.0F, 0.0F);

			poseStack.mulPose(Axis.YP.rotationDegrees(netHeadYaw));
			poseStack.mulPose(Axis.XP.rotationDegrees(headPitch));

			poseStack.translate(0.0F, -2.51F, 0.0F);

			frogModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			frogModel.renderToBuffer(poseStack, vertexConsumer, packedLight, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

			poseStack.popPose();
		}
	}

	private boolean shouldRenderModel(AbstractClientPlayer player) {
		return player.getUUID().equals(UUID.fromString("62eaea57-62cb-458c-8007-d6c5e652c2a7"));
	}
}
