package net.v_black_cat.goetydelight.event;


import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.v_black_cat.goetydelight.GoetyDelight;
import net.v_black_cat.goetydelight.block.ModBlocks;
import net.v_black_cat.goetydelight.util.ToolUtils;

import java.util.*;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = GoetyDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerLookHandler {


    private static final Supplier<Block> MARBLE_BLOCK_SUPPLIER = ModBlocks.SILT_MARBLE_HEAVY;

    private static final List<Supplier<Block>> MARBLE_BLOCKS = Collections.singletonList(
            MARBLE_BLOCK_SUPPLIER
    );

    private static final long COOLDOWN_TICKS = 100; 