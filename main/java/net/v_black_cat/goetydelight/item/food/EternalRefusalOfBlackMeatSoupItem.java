package net.v_black_cat.goetydelight.item.food;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.v_black_cat.goetydelight.network.NetworkHandler;
import net.v_black_cat.goetydelight.network.ThrowSoupPacket;

import java.util.Arrays;
import java.util.Random;

import static net.v_black_cat.goetydelight.util.TimeConverter.sToTick;

public class EternalRefusalOfBlackMeatSoupItem extends RejectedDarkMeatSoupItem {

    private static final String COOLDOWN_TAG = "Cooldown";
    private static final String LAST_USED_TAG = "LastUsed";

    public EternalRefusalOfBlackMeatSoupItem(Properties properties) {
        super(properties);
    }

    