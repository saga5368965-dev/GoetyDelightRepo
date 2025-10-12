package net.v_black_cat.goetydelight.item.food;

import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.server.SPlayPlayerSoundPacket;
import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.v_black_cat.goetydelight.ability.AbilityRegistry;
import net.v_black_cat.goetydelight.ability.TimedAbilitySystem;

public class SugarScepterItem extends Item {
    
    private static final int COOLDOWN_TICKS = 20 * 20;

    public SugarScepterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            
            if (player.getCooldowns().isOnCooldown(this)) {
                return super.finishUsingItem(stack, level, entity);
            }

            
            boolean success = TimedAbilitySystem.addAbilityToEntity(
                    entity,
                    AbilityRegistry.SUGAR_SCEPTER_IMMUNITY,
                    COOLDOWN_TICKS
            );

            if (success) {
                
                player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

                
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
        }

        return super.finishUsingItem(stack, level, entity);
    }

    @Mod.EventBusSubscriber
    public static class DamageImmunityHandler {

        @SubscribeEvent
        public static void onLivingAttack(LivingAttackEvent event) {
            LivingEntity entity = event.getEntity();

            
            if (entity.level().isClientSide) return;

            
            boolean hasImmunity = TimedAbilitySystem.hasAbility(
                    entity,
                    AbilityRegistry.SUGAR_SCEPTER_IMMUNITY
            );

            
            if (hasImmunity) {
                
                if (event.getSource().getEntity() instanceof LivingEntity attacker) {
                    
                    double dx =   attacker.getX()-entity.getX();
                    double dz =   attacker.getZ()-entity.getZ();

                    
                    double length = Math.sqrt(dx * dx + dz * dz);
                    if (length > 0) {
                        dx /= length;
                        dz /= length;
                    }

                    
                    attacker.push(dx * 5.0, 0.2, dz * 5.0);
                    attacker.hurtMarked = true;
                }

                
                event.setCanceled(true);

                
                TimedAbilitySystem.removeAbilityFromEntity(entity, AbilityRegistry.SUGAR_SCEPTER_IMMUNITY);
                Vec3 pos = entity.position();
                entity.level().playSound(
                        null, 
                        pos.x, pos.y, pos.z, 
                        SoundEvents.TURTLE_EGG_CRACK, 
                        SoundSource.PLAYERS, 
                        8.0F, 
                        1F 
                );
            }
        }
    }
}