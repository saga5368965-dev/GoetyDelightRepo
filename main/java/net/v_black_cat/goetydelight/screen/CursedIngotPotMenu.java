
package net.v_black_cat.goetydelight.screen;

import com.Polarice3.Goety.api.items.magic.ITotem;
import com.Polarice3.Goety.common.items.ModItems;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.v_black_cat.goetydelight.GoetyDelight;
import net.v_black_cat.goetydelight.block.CursedIngotPotBlockEntity;
import net.v_black_cat.goetydelight.block.ModBlocks;
import vectorwing.farmersdelight.FarmersDelight;

import vectorwing.farmersdelight.common.block.entity.container.CookingPotMealSlot;
import vectorwing.farmersdelight.common.tag.ModTags;

import static net.v_black_cat.goetydelight.GoetyDelight.MODID;

public class CursedIngotPotMenu extends RecipeBookMenu<RecipeWrapper> {
    public static final ResourceLocation EMPTY_CONTAINER_SLOT_BOWL = new ResourceLocation("farmersdelight", "item/empty_container_slot_bowl");

    public final CursedIngotPotBlockEntity blockEntity;
    public final ItemStackHandler inventory;
    private final ContainerData cookingPotData;
    private final ContainerLevelAccess canInteractWithCallable;
    protected final Level level;

    public CursedIngotPotMenu(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data), new SimpleContainerData(4));
    }

    public CursedIngotPotMenu(int windowId, Inventory playerInventory, CursedIngotPotBlockEntity blockEntity, ContainerData cookingPotDataIn) {
        super((MenuType) ModMenuTypes.CURSED_INGOT_POT.get(), windowId);
        this.blockEntity = blockEntity;
        this.inventory = blockEntity.getInventory();
        this.cookingPotData = cookingPotDataIn;
        this.level = playerInventory.player.level();
        this.canInteractWithCallable = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        int startX = 8;
        int startY = 18;
        int inputStartX = 30;
        int inputStartY = 17;
        int borderSlotSize = 18;

        