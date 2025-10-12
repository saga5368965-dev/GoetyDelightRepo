package net.v_black_cat.goetydelight.render.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.v_black_cat.goetydelight.item.ModItems;
import net.v_black_cat.goetydelight.ability.TimedAbilitySystem;
import net.v_black_cat.goetydelight.ability.AbilityRegistry;
import org.joml.Quaternionf;

public class RotationEffectHandler {
    private static float rotationAngle = 0;
    private static final int ITEM_COUNT = 4;
    private static final double RADIUS = 0.8;
    private static final double HEIGHT_OFFSET = 0.3;

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == Phase.START) {
            rotationAngle += 1f;
            if (rotationAngle >= 360) rotationAngle = 0;
        }
    }

    @SubscribeEvent
    public static void onRenderLivingEvent(RenderLivingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        