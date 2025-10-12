package net.v_black_cat.goetydelight.item.food;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import net.v_black_cat.goetydelight.util.EntityTagChecker;

import javax.annotation.Nullable;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = "goetydelight")
public class OminousIceCreamItem extends Item {

    public static final String OMINOUS_ACTIVE_TAG = "OminousIceCreamActive";
    public static final String HAS_CONSUMED_TAG = "HasConsumedOminousIceCream";

    