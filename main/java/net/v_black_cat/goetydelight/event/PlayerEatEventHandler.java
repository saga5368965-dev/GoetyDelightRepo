package net.v_black_cat.goetydelight.event;

import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.v_black_cat.goetydelight.config.FoodSoulEnergyConfig;
import net.v_black_cat.goetydelight.GoetyDelight;

@Mod.EventBusSubscriber(modid = GoetyDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEatEventHandler {

    @SubscribeEvent
    public static void onPlayerFinishEating(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }

        ItemStack finishedItem = event.getItem();
        int soulEnergy = FoodSoulEnergyConfig.getSoulEnergyForItem(finishedItem.getItem());

        
        if (soulEnergy > 0) {
            SEHelper.increaseSouls(player, soulEnergy);
        }

        
        CompoundTag tag = finishedItem.getTag();
        if (tag != null && tag.getBoolean("SoulInfused")) {
            
            FoodProperties foodProperties = finishedItem.getFoodProperties(player);
            if (foodProperties != null) {
                
                int bonusSoulEnergy = 5 * foodProperties.getNutrition();
                SEHelper.increaseSouls(player, bonusSoulEnergy);
            }
        }

        
        SEHelper.sendSEUpdatePacket(player);
    }
}