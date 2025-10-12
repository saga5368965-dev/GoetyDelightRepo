package net.v_black_cat.goetydelight.block;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.v_black_cat.goetydelight.GoetyDelight;
import net.v_black_cat.goetydelight.render.test.ModShaderInstance;
import net.v_black_cat.goetydelight.render.test.ModShaderReg;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class RenderBlockRenderer implements BlockEntityRenderer<RenderBlockEntity> {

    private static final ResourceLocation DUMMY_TEXTURE =
            new ResourceLocation(GoetyDelight.MODID, "textures/effect/test_texture.png");


    private static final RenderType SHADER_RENDER_TYPE = RenderType.create(
            "shader_block",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> ModShaderReg.getFloridShader()))
                    .setTextureState(new RenderStateShard.TextureStateShard(DUMMY_TEXTURE, false, false))
                    .createCompositeState(false)
    );

    public RenderBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(RenderBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        