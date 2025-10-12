package net.v_black_cat.goetydelight.recipe;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.v_black_cat.goetydelight.item.ModItems;
import net.v_black_cat.goetydelight.item.food.EternalRefusalOfBlackMeatSoupItem;

@Mod.EventBusSubscriber(modid = "goetydelight")
public class CraftingEventHandler {

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getEntity();
        ItemStack crafted = event.getCrafting();

        