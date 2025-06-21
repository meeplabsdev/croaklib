package com.croaklib.client.model.frog;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class FrogModel extends HierarchicalModel<Entity> {
	public final AnimationState idleAnimationState = new AnimationState();

	private final ModelPart frog;
	private final ModelPart true_head;
	private final ModelPart head_up;
	private final ModelPart head_down;
	private final ModelPart croak;
	private final ModelPart true_tongue;
	private final ModelPart true_tongue_r;
	private final ModelPart true_tongue_up;
	private final ModelPart legs;
	private final ModelPart ru_leg;
	private final ModelPart lu_leg;
	private final ModelPart rd_leg;
	private final ModelPart rd_leg_r;
	private final ModelPart rd_leg_f;
	private final ModelPart ld_leg;
	private final ModelPart ld_leg_r;
	private final ModelPart ld_leg_f;

	public FrogModel(ModelPart root) {
		this.frog = root.getChild("frog");
		this.true_head = this.frog.getChild("true_head");
		this.head_up = this.true_head.getChild("head_up");
		this.head_down = this.true_head.getChild("head_down");
		this.croak = this.head_down.getChild("croak");
		this.true_tongue = this.true_head.getChild("true_tongue");
		this.true_tongue_r = this.true_tongue.getChild("true_tongue_r");
		this.true_tongue_up = this.true_tongue_r.getChild("true_tongue_up");
		this.legs = this.frog.getChild("legs");
		this.ru_leg = this.legs.getChild("ru_leg");
		this.lu_leg = this.legs.getChild("lu_leg");
		this.rd_leg = this.legs.getChild("rd_leg");
		this.rd_leg_r = this.rd_leg.getChild("rd_leg_r");
		this.rd_leg_f = this.rd_leg.getChild("rd_leg_f");
		this.ld_leg = this.legs.getChild("ld_leg");
		this.ld_leg_r = this.ld_leg.getChild("ld_leg_r");
		this.ld_leg_f = this.ld_leg.getChild("ld_leg_f");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition frog = partdefinition.addOrReplaceChild("frog", CubeListBuilder.create().texOffs(0, 14).addBox(-3.5F, -4.0F, -8.0F, 7.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, 4.0F));
		PartDefinition true_head = frog.addOrReplaceChild("true_head", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, -8.0F));
		PartDefinition head_up = true_head.addOrReplaceChild("head_up", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
			.texOffs(47, 0).addBox(-3.0F, -0.9F, -5.0F, 6.0F, 0.0F, 5.0F, new CubeDeformation(0.0F))
			.texOffs(0, 9).addBox(-4.0F, -3.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(9, 9).addBox(2.0F, -3.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition head_down = true_head.addOrReplaceChild("head_down", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, 0.0F, -5.0F, 6.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
			.texOffs(47, 6).addBox(-3.0F, 1.0F, -5.0F, 6.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition croak = head_down.addOrReplaceChild("croak", CubeListBuilder.create().texOffs(24, 8).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 1.0F, 5.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 1.0F, -3.0F));
		PartDefinition true_tongue = true_head.addOrReplaceChild("true_tongue", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition true_tongue_r = true_tongue.addOrReplaceChild("true_tongue_r", CubeListBuilder.create().texOffs(51, 18).addBox(-1.5F, 0.0F, -5.0F, 3.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.1F));
		PartDefinition true_tongue_up = true_tongue_r.addOrReplaceChild("true_tongue_up", CubeListBuilder.create().texOffs(51, 18).addBox(-1.5F, 0.0F, -5.0F, 3.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition legs = frog.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition ru_leg = legs.addOrReplaceChild("ru_leg", CubeListBuilder.create().texOffs(9, 30).addBox(-1.75F, -0.25F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.25F))
			.texOffs(9, 47).addBox(-4.5F, 4.01F, -5.5F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -2.0F, -6.0F));
		PartDefinition lu_leg = legs.addOrReplaceChild("lu_leg", CubeListBuilder.create().texOffs(0, 30).addBox(-0.25F, -0.25F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.25F))
			.texOffs(-8, 47).addBox(-3.5F, 4.01F, -5.5F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -2.0F, -6.0F));
		PartDefinition rd_leg = legs.addOrReplaceChild("rd_leg", CubeListBuilder.create(), PartPose.offset(-5.5F, -0.5F, -0.5F));
		PartDefinition rd_leg_r = rd_leg.addOrReplaceChild("rd_leg_r", CubeListBuilder.create(), PartPose.offset(1.0F, -0.5F, 0.5F));
		PartDefinition rd_leg_r_r1 = rd_leg_r.addOrReplaceChild("rd_leg_r_r1", CubeListBuilder.create().texOffs(15, 37).mirror().addBox(-5.5F, -4.0F, 2.0F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.5F, 1.25F, -4.85F, -0.3927F, 0.0F, 0.0F));
		PartDefinition rd_leg_f = rd_leg.addOrReplaceChild("rd_leg_f", CubeListBuilder.create().texOffs(9, 56).addBox(-5.5F, 3.01F, -3.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -0.5F, -0.5F));
		PartDefinition ld_leg = legs.addOrReplaceChild("ld_leg", CubeListBuilder.create(), PartPose.offset(3.5F, -0.5F, -0.5F));
		PartDefinition ld_leg_r = ld_leg.addOrReplaceChild("ld_leg_r", CubeListBuilder.create(), PartPose.offset(1.0F, -0.5F, 0.5F));
		PartDefinition ld_leg_r_r1 = ld_leg_r.addOrReplaceChild("ld_leg_r_r1", CubeListBuilder.create().texOffs(0, 37).addBox(3.5F, -4.0F, 2.0F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, 1.25F, -4.85F, -0.3927F, 0.0F, 0.0F));
		PartDefinition ld_leg_f = ld_leg.addOrReplaceChild("ld_leg_f", CubeListBuilder.create().texOffs(-8, 56).addBox(-2.5F, 3.01F, -3.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, -0.5F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.frog.getAllParts().forEach(ModelPart::resetPose);
		this.idleAnimationState.startIfStopped(0);
		this.animate(this.idleAnimationState, FrogModelAnimation.idle, ageInTicks);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		frog.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public @NotNull ModelPart root() {
		return this.frog;
	}
}