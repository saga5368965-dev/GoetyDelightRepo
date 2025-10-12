package net.v_black_cat.goetydelight.item.food;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = "goetydelight")
public class BoneLordAshRiceItem extends Item {

     
    private static final UUID ARMOR_BONUS_UUID = UUID.fromString("a1b2c3d4-5e6f-7a8b-9c0d-1e2f3a4b5c6d");
    private static final UUID ARMOR_TOUGHNESS_BONUS_UUID = UUID.fromString("d4c3b2a1-f6e5-b8a7-d0c9-f2e1b4a3c5d6");

     
    private static final String BONUS_ACTIVE_TAG = "BoneLordAshRiceActive";
    private static final String ACTIVATION_TIME_TAG = "BoneLordAshRiceActivationTime";

     
    private static final int DURATION_TICKS = 20 * 60 * 5;  

    public BoneLordAshRiceItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        ItemStack resultStack = super.finishUsingItem(stack, level, entity);

        if (!level.isClientSide && entity instanceof Player player) {
             
            CompoundTag persistentData = player.getPersistentData();

             
            if (persistentData.getBoolean(BONUS_ACTIVE_TAG)) {
                removeBonusAttributes(player);
            }

             
            addBonusAttributes(player);

             
            persistentData.putBoolean(BONUS_ACTIVE_TAG, true);
            persistentData.putLong(ACTIVATION_TIME_TAG, level.getGameTime());

             
           