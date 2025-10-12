package net.v_black_cat.goetydelight.item.food;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;

import java.util.ArrayList;
import java.util.List;

public class PoachedNetherWartEggItem extends Item {
    public PoachedNetherWartEggItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        
        ItemStack result = super.finishUsingItem(stack, level, entity);

        
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            
            List<MobEffect> effectsToRemove = new ArrayList<>();

            
            for (MobEffectInstance effectInstance : entity.getActiveEffects()) {
                MobEffect effect = effectInstance.getEffect();

                
                if (effect.getCategory() == MobEffectCategory.HARMFUL) {
                    effectsToRemove.add(effect);
                }
            }

            
            for (MobEffect effect : effectsToRemove) {
                entity.removeEffect(effect);
            }

            
            serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1.0F, 1.5F);

            
            for (int i = 0; i < 15; i++) {
                double x = entity.getX() + (level.random.nextDouble() - 0.5) * 2.0;
                double y = entity.getY() + level.random.nextDouble() * 2.0;
                double z = entity.getZ() + (level.random.nextDouble() - 0.5) * 2.0;

                serverLevel.sendParticles(ParticleTypes.ENCHANT,
                        x, y, z, 1, 0, 0, 0, 0.1);
            }

            
            if (!effectsToRemove.isEmpty()) {
                serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.7F, 1.2F);
            }
        }

        return result;
    }
}