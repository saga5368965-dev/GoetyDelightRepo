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
        