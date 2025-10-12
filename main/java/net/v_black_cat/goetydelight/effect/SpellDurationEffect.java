package net.v_black_cat.goetydelight.effect;

import com.Polarice3.Goety.init.ModAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.fml.common.Mod;
import net.v_black_cat.goetydelight.GoetyDelight;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = GoetyDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpellDurationEffect extends MobEffect {

    