package net.v_black_cat.goetydelight.recipe;

import com.Polarice3.Goety.common.effects.brew.BrewEffectInstance;
import com.Polarice3.Goety.common.items.brew.BrewItem;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.v_black_cat.goetydelight.item.ModItems;
import net.v_black_cat.goetydelight.item.food.EternalRefusalOfBlackMeatSoupItem;
import net.v_black_cat.goetydelight.util.ModBrewUtils;

import java.util.List;

import static com.Polarice3.Goety.common.items.ModItems.BREW;
import static com.Polarice3.Goety.utils.BrewUtils.getBrewEffects;

public class PotionAmplifierRecipe extends CustomRecipe {
    public PotionAmplifierRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
        super(pId, pCategory);
    }

    @Override
    public boolean matches(CraftingContainer pContainer, Level pLevel) {
        boolean hasWitchBrew = false;
        boolean hasAmplifier = false;
        ItemStack brewStack = ItemStack.EMPTY;
        ItemStack amplifierStack = ItemStack.EMPTY;

        for (int i = 0; i < pContainer.getContainerSize(); i++) {
            ItemStack stack = pContainer.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() == BREW.get()) {
                if (hasNegativeEffects(stack)) {
                    hasWitchBrew = true;
                    brewStack = stack;
                }
            } else if (stack.getItem() == ModItems.REJECTED_DARK_MEAT_SOUP.get() ||
                    stack.getItem() == ModItems.CUP.get()) {
                if(stack.getItem() == ModItems.REJECTED_DARK_MEAT_SOUP.get()){
                    amplifierStack = stack;
                    hasAmplifier = true;
                }else if (stack.getItem() instanceof EternalRefusalOfBlackMeatSoupItem cup){
                    