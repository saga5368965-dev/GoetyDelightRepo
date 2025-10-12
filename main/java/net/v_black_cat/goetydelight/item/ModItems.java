package net.v_black_cat.goetydelight.item;

import com.Polarice3.Goety.common.items.ModTiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.v_black_cat.goetydelight.GoetyDelight;
import net.v_black_cat.goetydelight.effect.ModEffects;
import net.v_black_cat.goetydelight.item.food.*;
import net.v_black_cat.goetydelight.item.food.BowlFoodItem;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.item.KnifeItem;

import java.util.Random;
import java.util.function.Supplier;

import static com.Polarice3.Goety.common.effects.GoetyEffects.*;
import static net.v_black_cat.goetydelight.block.ModBlocks.EXAMPLE_BLOCK;
import static net.v_black_cat.goetydelight.util.TimeConverter.minToTick;
import static net.v_black_cat.goetydelight.util.TimeConverter.sToTick;
import static vectorwing.farmersdelight.common.registry.ModItems.basicItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, GoetyDelight.MODID);

    