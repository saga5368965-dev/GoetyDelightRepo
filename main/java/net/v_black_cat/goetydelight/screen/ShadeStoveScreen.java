package net.v_black_cat.goetydelight.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.gui.screens.recipebook.SmokingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.v_black_cat.goetydelight.GoetyDelight.MODID;

@OnlyIn(Dist.CLIENT)
public class ShadeStoveScreen extends AbstractContainerScreen<ShadeStoveMenu> implements RecipeUpdateListener {
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation(MODID, "textures/gui/recipe_button.png");
    private static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/shade_stove.png");

    private final SmokingRecipeBookComponent recipeBook = new SmokingRecipeBookComponent();
    private boolean widthTooNarrow;
    private boolean buttonClicked;

    public ShadeStoveScreen(ShadeStoveMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory,  Component.translatable("goetydelight.container.shade_stove"));
    }

    @Override
    public void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBook.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBook.updateScreenPosition(this.width, this.imageWidth);

        