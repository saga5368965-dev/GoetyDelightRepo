package net.v_black_cat.goetydelight.item.food;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.ally.spider.AbstractSpiderServant;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SiblingSundaeItem extends Item {
    public SiblingSundaeItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack resultStack = super.finishUsingItem(stack, level, entity);

        if (!level.isClientSide) {
            