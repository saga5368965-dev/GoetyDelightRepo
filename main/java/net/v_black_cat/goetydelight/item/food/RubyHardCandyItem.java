package net.v_black_cat.goetydelight.item.food;

import com.Polarice3.Goety.init.ModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.v_black_cat.goetydelight.ability.AbilityRegistry;
import net.v_black_cat.goetydelight.ability.TimedAbilitySystem;

import java.util.UUID;

public class RubyHardCandyItem extends Item {
    