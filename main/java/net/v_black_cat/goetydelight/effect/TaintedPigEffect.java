package net.v_black_cat.goetydelight.effect;

import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.data.models.model.TextureMapping.getItemTexture;

public class TaintedPigEffect extends MobEffect {

    public TaintedPigEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x8B4513);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        