package net.v_black_cat.goetydelight.screen;

import com.Polarice3.Goety.api.items.magic.ITotem;
import com.Polarice3.Goety.common.items.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class ShadeStoveMenu extends AbstractFurnaceMenu {



    public ShadeStoveMenu(int pContainerId, Inventory pPlayerInventory) {
        super(ModMenuTypes.SHADE_STOVE.get(), RecipeType.SMOKING, RecipeBookType.SMOKER, pContainerId, pPlayerInventory);
        replaceFuelSlot();
    }

    public ShadeStoveMenu(int pContainerId, Inventory pPlayerInventory, Container pSmokerContainer, ContainerData pSmokerData) {
        super(ModMenuTypes.SHADE_STOVE.get(), RecipeType.SMOKING, RecipeBookType.SMOKER, pContainerId, pPlayerInventory, pSmokerContainer, pSmokerData);
        replaceFuelSlot();
    }

    private void replaceFuelSlot() {
        for (int i = 0; i < this.slots.size(); i++) {
            Slot slot = this.slots.get(i);
            if (slot.index == 1) {

                SoulFuelSlot newSlot = new SoulFuelSlot(slot.container, 1, slot.x, slot.y);
                newSlot.index = 1;
                this.slots.set(i, newSlot);
                break;
            }
        }
    }
    @Override
    protected boolean isFuel(ItemStack pStack) {
        return pStack.getItem() instanceof ITotem ||
                pStack.getItem() == ModItems.SOUL_TRANSFER.get();
    }



    protected boolean moveItemStackTo(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
        boolean flag = false;
        int i = pStartIndex;
        if (pReverseDirection) {
            i = pEndIndex - 1;
        }

        