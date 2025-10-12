package net.v_black_cat.goetydelight.render.test;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.v_black_cat.goetydelight.GoetyDelight;

@Mod.EventBusSubscriber(modid = GoetyDelight.MODID, value = Dist.CLIENT)
public class ModShaderRenderer {

    private static float time = 0;

    @SubscribeEvent
    public static void onRenderLivingEvent(RenderLivingEvent event) {
        if(false){
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null) return;
            time = (player.level().getGameTime() + mc.getFrameTime()) / 10.0f;
            