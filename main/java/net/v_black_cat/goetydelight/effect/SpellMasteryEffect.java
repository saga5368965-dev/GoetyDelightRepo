package net.v_black_cat.goetydelight.effect;

import com.Polarice3.Goety.init.ModAttributes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class SpellMasteryEffect extends MobEffect {

    
    private static final UUID SPELL_POTENCY_UUID = UUID.fromString("8b4513a0-4e2a-11ee-be56-0242ac120003");

    public SpellMasteryEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x8B4513);
        
        this.addAttributeModifier(
                ModAttributes.SPELL_POTENCY.get(),
                SPELL_POTENCY_UUID.toString(),
                2, 
                AttributeModifier.Operation.ADDITION
        );
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        
        
        return modifier.getAmount() * (amplifier + 1);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);

        Level world = entity.level();
        if (world.isClientSide) {
            
            double x = entity.getX() + (world.random.nextDouble() - 0.5) * entity.getBbWidth();
            double y = entity.getY() + world.random.nextDouble() * entity.getBbHeight();
            double z = entity.getZ() + (world.random.nextDouble() - 0.5) * entity.getBbWidth();

            world.addParticle(ParticleTypes.ENCHANT, x, y, z, 0.0D, 0.1D, 0.0D);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        
        return duration % 10 == 0;
    }
}