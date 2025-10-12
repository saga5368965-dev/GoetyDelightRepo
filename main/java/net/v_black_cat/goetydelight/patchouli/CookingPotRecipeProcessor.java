package net.v_black_cat.goetydelight.patchouli;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.List;

public class CookingPotRecipeProcessor implements IComponentProcessor {

    private CookingPotRecipe recipe;

    @Override
    public void setup(Level level, IVariableProvider variables) {
        ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
        recipe = (CookingPotRecipe) level.getRecipeManager()
                .byKey(id)
                .filter(r -> r.getType() == vectorwing.farmersdelight.common.registry.ModRecipeTypes.COOKING.get())
                .map(r -> (CookingPotRecipe) r)
                .orElse(null);
    }

    @Override
    public IVariable process(Level level, String key) {
        if (recipe == null) return null;

        switch (key) {
            