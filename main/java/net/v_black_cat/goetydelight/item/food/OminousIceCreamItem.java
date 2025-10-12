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

    
    private static final WeakHashMap<Mob, Boolean> modifiedMobs = new WeakHashMap<>();

    public OminousIceCreamItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        ItemStack resultStack = super.finishUsingItem(stack, level, entity);

        if (!level.isClientSide && entity instanceof Player player) {
            if (player.hasEffect(MobEffects.BAD_OMEN)) {
                player.getPersistentData().putBoolean(OMINOUS_ACTIVE_TAG, true);
                player.getPersistentData().putBoolean(HAS_CONSUMED_TAG, true);

                
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        net.minecraft.sounds.SoundEvents.EVOKER_PREPARE_SUMMON,
                        net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
            } else {
                
            }
        }

        return resultStack;
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Mob mob) || event.getLevel().isClientSide()) {
            return;
        }

        
        if (!EntityTagChecker.isEntityInTag(mob, "minecraft:raiders") ||
                mob.getType().is(Tags.EntityTypes.BOSSES)) {
            return;
        }

        if (modifiedMobs.containsKey(mob)) {
            return;
        }


        
        mob.targetSelector.addGoal(0, new ConditionalPlayerTargetGoal(mob));

        modifiedMobs.put(mob, true);
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effect = event.getEffectInstance();
        if (effect == null) return;

        if (entity instanceof Player player && effect.getEffect() == MobEffects.BAD_OMEN) {
            if (player.getPersistentData().getBoolean(OMINOUS_ACTIVE_TAG)) {
                player.getPersistentData().remove(OMINOUS_ACTIVE_TAG);
                
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        net.minecraft.sounds.SoundEvents.EVOKER_CAST_SPELL,
                        net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 0.5F);
            }
        }
    }

    @SubscribeEvent
    public static void onEffectExpire(MobEffectEvent.Expired event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effect = event.getEffectInstance();
        if (effect == null) return;

        if (entity instanceof Player player && effect.getEffect() == MobEffects.BAD_OMEN) {
            if (player.getPersistentData().getBoolean(OMINOUS_ACTIVE_TAG)) {
                player.getPersistentData().remove(OMINOUS_ACTIVE_TAG);
                
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        net.minecraft.sounds.SoundEvents.WITHER_SPAWN,
                        net.minecraft.sounds.SoundSource.PLAYERS, 0.8F, 1.2F);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(net.minecraftforge.event.entity.living.LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.getPersistentData().remove(OMINOUS_ACTIVE_TAG);
            player.getPersistentData().remove(HAS_CONSUMED_TAG);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        player.getPersistentData().remove(OMINOUS_ACTIVE_TAG);
        player.getPersistentData().remove(HAS_CONSUMED_TAG);
    }

    public static class ConditionalPlayerTargetGoal extends NearestAttackableTargetGoal<Player> {
        public ConditionalPlayerTargetGoal(Mob mob) {
            super(mob, Player.class, true);
            this.targetConditions = TargetingConditions.forCombat()
                    .range(this.getFollowDistance())
                    .selector(player -> {
                        
                        return !player.getPersistentData().getBoolean(OminousIceCreamItem.OMINOUS_ACTIVE_TAG);
                    });
        }

        @Override
        public boolean canUse() {
            
            LivingEntity lastAttacker = this.mob.getLastHurtByMob();
            if (lastAttacker instanceof Player) {
                Player player = (Player) lastAttacker;
                if (player.getPersistentData().getBoolean(OminousIceCreamItem.OMINOUS_ACTIVE_TAG)) {
                    
                    if (this.mob.tickCount - this.mob.getLastHurtByMobTimestamp() < 100) { 
                        this.target = player;
                        return true;
                    }
                }
            }

            
            if (this.mob.getTarget() != null) {
                
                LivingEntity currentTarget = this.mob.getTarget();
                if (currentTarget instanceof Player) {
                    Player player = (Player) currentTarget;
                    
                    if (player.getPersistentData().getBoolean(OminousIceCreamItem.OMINOUS_ACTIVE_TAG)) {
                        this.mob.setTarget(null);
                    }
                }
                return false;
            }

            
            this.findTarget();
            return this.target != null;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.target == null) return false;

            
            if (this.target == this.mob.getLastHurtByMob()) {
                
                return this.mob.tickCount - this.mob.getLastHurtByMobTimestamp() < 100;
            }

            
            return !this.target.isRemoved() &&
                    !this.target.getPersistentData().getBoolean(OminousIceCreamItem.OMINOUS_ACTIVE_TAG) &&
                    super.canContinueToUse(); 
        }

        
        @Override
        public void setTarget(@Nullable LivingEntity target) {
            if (target instanceof Player) {
                Player player = (Player) target;
                
                if (player.getPersistentData().getBoolean(OminousIceCreamItem.OMINOUS_ACTIVE_TAG)) {
                    
                    if (this.mob.getLastHurtByMob() != player) {
                        return; 
                    }
                }
            }
            super.setTarget(target);
        }
    }
}