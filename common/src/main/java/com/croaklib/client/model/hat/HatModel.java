package com.croaklib.client.model.hat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class HatModel extends HierarchicalModel<Entity> {
	private final ModelPart root;
	private final ModelPart center;
	private final ModelPart edge;

	public HatModel(ModelPart root) {
		this.root = root.getChild("root");
		this.center = this.root.getChild("center");
		this.edge = this.root.getChild("edge");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -1.5708F, 0.0611F));
		PartDefinition center = root.addOrReplaceChild("center", CubeListBuilder.create().texOffs(0, 0).addBox(-4.2F, 1.1F, -5.5F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.8F, -3.1F, 0.5F));
		PartDefinition cube_r1 = center.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(46, 27).addBox(-3.9962F, -2.8255F, -3.0F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.3F, -0.5F, 0.0F, -1.5708F, 0.0436F));
		PartDefinition cube_r2 = center.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(17, 46).addBox(-3.9962F, -2.8255F, -3.0F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.3F, -0.5F, 3.1416F, 0.0F, -3.098F));
		PartDefinition cube_r3 = center.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(44, 38).addBox(-3.9962F, -2.8255F, -3.0F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.3F, -0.5F, 0.0F, 1.5708F, 0.0436F));
		PartDefinition cube_r4 = center.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 43).addBox(0.0F, -3.0F, -3.0F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, 0.3F, -0.5F, 0.0F, 0.0F, 0.0436F));
		PartDefinition cube_r5 = center.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(27, 27).addBox(0.0F, -2.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.2F, 2.1F, -0.5F, 0.0F, 0.0F, 0.2182F));
		PartDefinition cube_r6 = center.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(34, 49).addBox(-4.0F, -2.0F, 0.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8F, 2.1F, -5.5F, -0.2182F, 0.0F, 0.0F));
		PartDefinition cube_r7 = center.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(34, 53).addBox(-4.0F, -2.0F, -1.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8F, 2.1F, 4.5F, 0.2182F, 0.0F, 0.0F));
		PartDefinition cube_r8 = center.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, -2.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.8F, 2.1F, -0.5F, 0.0F, 0.0F, -0.2182F));
		PartDefinition cube_r9 = center.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 13).addBox(-3.9962F, -2.8255F, -4.0F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0436F));
		PartDefinition edge = root.addOrReplaceChild("edge", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -5.0F));
		PartDefinition cube_r10 = edge.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(19, 42).addBox(-5.0F, -1.0406F, -2.0097F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 10.0F, 2.7489F, 0.0F, 3.1416F));
		PartDefinition cube_r11 = edge.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(27, 22).addBox(-5.0F, -0.9876F, -3.0464F, 10.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.8F, 11.8F, 2.3126F, 0.0F, 3.1416F));
		PartDefinition cube_r12 = edge.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(18, 61).addBox(-8.9366F, 1.0915F, -2.7959F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(54, 17).addBox(-6.9149F, 1.1191F, -3.7507F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.1F, 0.2F, -0.8249F, 0.0051F, 0.1232F));
		PartDefinition cube_r13 = edge.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(27, 61).addBox(-0.4684F, 0.6129F, -8.8454F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(36, 61).addBox(-0.4823F, 0.6465F, -6.8234F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.1F, 0.2F, -1.5708F, 1.1781F, -1.4399F));
		PartDefinition cube_r14 = edge.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(54, 22).addBox(4.9149F, 1.1191F, -3.7507F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
			.texOffs(61, 42).addBox(6.9366F, 1.0915F, -2.7959F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.1F, 0.2F, -0.8249F, -0.0051F, -0.1232F));
		PartDefinition cube_r15 = edge.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(61, 38).addBox(-1.5177F, 0.6465F, -6.8234F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(45, 62).addBox(-1.5316F, 0.6129F, -8.8454F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.1F, 0.2F, -1.5708F, -1.1781F, 1.4399F));
		PartDefinition cube_r16 = edge.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(56, 58).addBox(-2.0F, -0.9876F, -2.0464F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -1.1F, 11.8F, 2.3167F, -0.0051F, 3.0184F));
		PartDefinition cube_r17 = edge.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(54, 12).addBox(-2.0F, -0.9876F, -3.0464F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -0.8F, 11.8F, 2.3167F, -0.0051F, 3.0184F));
		PartDefinition cube_r18 = edge.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 59).addBox(0.0F, -1.0406F, -2.0097F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -0.3F, 10.0F, 1.5708F, -1.1781F, -1.7017F));
		PartDefinition cube_r19 = edge.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(9, 61).addBox(0.0F, -1.0406F, -2.0097F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 0.0F, 10.0F, 1.5708F, -1.1781F, -1.7017F));
		PartDefinition cube_r20 = edge.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(41, 4).addBox(-5.0F, -1.0406F, -2.0097F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -0.3F, 5.0F, 0.0F, 1.5708F, 0.1309F));
		PartDefinition cube_r21 = edge.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(41, 0).addBox(-5.0F, -1.0406F, -2.0097F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 0.0F, 5.0F, 0.0F, 1.5708F, 0.1309F));
		PartDefinition cube_r22 = edge.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(0, 22).addBox(-5.0F, -0.9876F, -3.0464F, 10.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.8F, 11.8F, 2.3126F, 0.0F, -3.1416F));
		PartDefinition cube_r23 = edge.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(19, 38).addBox(-5.0F, -1.0406F, -2.0097F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -0.3F, 5.0F, 0.0F, -1.5708F, -0.1309F));
		PartDefinition cube_r24 = edge.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(29, 17).addBox(-5.0F, -1.0406F, -2.0097F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 0.0F, 5.0F, 0.0F, -1.5708F, -0.1309F));
		PartDefinition cube_r25 = edge.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(0, 27).addBox(-5.0F, -0.9876F, -3.0464F, 10.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.8F, -1.8F, -0.829F, 0.0F, 0.0F));
		PartDefinition cube_r26 = edge.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(41, 8).addBox(-5.0F, -1.0406F, -2.0097F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
		PartDefinition cube_r27 = edge.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(0, 54).addBox(0.0F, -0.9876F, -3.0464F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -0.8F, 11.8F, 2.3167F, 0.0051F, -3.0184F));
		PartDefinition cube_r28 = edge.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(47, 58).addBox(0.0F, -0.9876F, -2.0464F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -1.1F, 11.8F, 2.3167F, 0.0051F, -3.0184F));
		PartDefinition cube_r29 = edge.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(29, 57).addBox(-2.0F, -1.0406F, -2.0097F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 0.0F, 10.0F, 1.5708F, 1.1781F, 1.7017F));
		PartDefinition cube_r30 = edge.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(38, 57).addBox(-2.0F, -1.0406F, -2.0097F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -0.3F, 10.0F, 1.5708F, 1.1781F, 1.7017F));
		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public @NotNull ModelPart root() {
		return this.root;
	}
}