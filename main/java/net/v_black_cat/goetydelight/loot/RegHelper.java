package net.v_black_cat.goetydelight.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.v_black_cat.goetydelight.GoetyDelight;

public class RegHelper {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS =
            DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, GoetyDelight.MODID);

    