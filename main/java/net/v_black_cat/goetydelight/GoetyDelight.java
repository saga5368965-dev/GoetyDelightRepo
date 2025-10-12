package net.v_black_cat.goetydelight;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.v_black_cat.goetydelight.ability.AbilityRegistry;
import net.v_black_cat.goetydelight.block.ModBlockEntities;
import net.v_black_cat.goetydelight.block.RenderBlockRenderer;
import net.v_black_cat.goetydelight.config.Config;
import net.v_black_cat.goetydelight.effect.ModEffects;
import net.v_black_cat.goetydelight.entities.ModEntities;
import net.v_black_cat.goetydelight.item.ModCreativeModTabs;
import net.v_black_cat.goetydelight.loot.RegHelper;
import net.v_black_cat.goetydelight.network.NetworkHandler;
import net.v_black_cat.goetydelight.recipe.ModRecipeSerializers;
import net.v_black_cat.goetydelight.render.animation.RotationEffectHandler;

import net.v_black_cat.goetydelight.ritual.DelightRitualType;
import net.v_black_cat.goetydelight.screen.*;
import org.slf4j.Logger;

import java.io.IOException;

import static net.v_black_cat.goetydelight.block.ModBlocks.NIGHT_STOVE;
import static net.v_black_cat.goetydelight.loot.ModLootModifier.GLOBAL_LOOT_MODIFIER_CODECS;
import static net.v_black_cat.goetydelight.item.ModItems.ITEMS;
import static net.v_black_cat.goetydelight.block.ModBlocks.BLOCKS;


