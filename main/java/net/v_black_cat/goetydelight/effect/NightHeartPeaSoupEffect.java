package net.v_black_cat.goetydelight.effect;

import com.Polarice3.Goety.api.items.magic.IFocus;
import com.Polarice3.Goety.api.items.magic.IWand;
import com.Polarice3.Goety.common.items.magic.DarkWand;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NightHeartPeaSoupEffect extends MobEffect {
    public NightHeartPeaSoupEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x98D982);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        