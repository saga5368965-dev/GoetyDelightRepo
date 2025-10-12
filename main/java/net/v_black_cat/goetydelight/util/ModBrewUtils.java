package net.v_black_cat.goetydelight.util;

import com.Polarice3.Goety.common.effects.brew.BrewEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.Polarice3.Goety.utils.BrewUtils.getBrewEffects;
import static com.Polarice3.Goety.utils.BrewUtils.setCustomEffects;

public class ModBrewUtils {

    public static void increaseNegativeEffects(ItemStack brewStack) {
        increaseNegativeEffects(brewStack, 3);
    }

    public static void increaseNegativeEffects(ItemStack brewStack, int maxAmplifier) {
        if (brewStack.isEmpty()) return;
        List<MobEffectInstance> mobEffects = PotionUtils.getMobEffects(brewStack);
        List<BrewEffectInstance> brewEffects = getBrewEffects(brewStack);
        
        clearAllEffects(brewStack);

        
        List<MobEffectInstance> finalMobEffects = new ArrayList<>();
        List<BrewEffectInstance> finalBrewEffects = new ArrayList<>();

        
        for (MobEffectInstance effect : mobEffects) {
            if (effect != null) {
                if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                    
                    int newAmplifier = Math.min(effect.getAmplifier() + 1, maxAmplifier);
                    finalMobEffects.add(new MobEffectInstance(
                            effect.getEffect(),
                            effect.getDuration(),
                            newAmplifier,
                            effect.isAmbient(),
                            effect.isVisible(),
                            effect.showIcon()
                    ));
                } else {
                    
                    finalMobEffects.add(effect);
                }
            }
        }

        
        if (brewEffects != null) {
            for (BrewEffectInstance effect : brewEffects) {
                if (effect != null) {
                    if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                        
                        int newAmplifier = Math.min(effect.getAmplifier() + 1, maxAmplifier);
                        finalBrewEffects.add(new BrewEffectInstance(
                                effect.getEffect(),
                                effect.getDuration(),
                                newAmplifier
                        ));
                    } else {
                        
                        finalBrewEffects.add(effect);
                    }
                }
            }
        }

        
        setCustomEffects(brewStack, finalMobEffects, finalBrewEffects);
    }

    
    private static void clearAllEffects(ItemStack brewStack) {
        CompoundTag tag = brewStack.getOrCreateTag();

        
        tag.remove("CustomPotionEffects");

        
        tag.remove("CustomBrewEffects");


        tag.remove("Potion");
    }
}