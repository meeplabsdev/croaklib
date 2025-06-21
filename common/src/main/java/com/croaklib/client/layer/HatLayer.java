package com.croaklib.client.layer;

import com.croaklib.CroakLibMod;
import com.croaklib.client.model.hat.HatModel;
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

public class HatLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	private final HatModel hatModel;
	private static final ResourceLocation MODEL_TEXTURE = new ResourceLocation(CroakLibMod.MOD_ID, "textures/entity/hat.png");

	public HatLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent) {
		super(renderLayerParent);
		this.hatModel = new HatModel(HatModel.createBodyLayer().bakeRoot());
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight,
										 AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
										 float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

		if (shouldRenderModel(player) && player.isSkinLoaded() && !player.isInvisible()) {
			VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(MODEL_TEXTURE));
			int overlay = LivingEntityRenderer.getOverlayCoords(player, 0.0F);

			poseStack.pushPose();
			poseStack.scale(1.0F, 1.0F, 1.0F);
			poseStack.translate(0.0F, 0.0F, 0.0F);

			poseStack.mulPose(Axis.YP.rotationDegrees(netHeadYaw));
			poseStack.mulPose(Axis.XP.rotationDegrees(headPitch));

			poseStack.translate(0.0F, -1.81F, 0.0F);

			hatModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			hatModel.renderToBuffer(poseStack, vertexConsumer, packedLight, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

			poseStack.popPose();
		}
	}

	private boolean shouldRenderModel(AbstractClientPlayer player) {
		return player.getUUID().equals(UUID.fromString("84b165b0-030c-4e54-8084-cabf6deed349"));
	}
}
