package net.v_black_cat.goetydelight.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.v_black_cat.goetydelight.block.NightStoveBlockEntity;

public class NightStoveMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;

    public NightStoveMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, new SimpleContainer(2), new SimpleContainerData(2));
    }

    public NightStoveMenu(int pContainerId, Inventory pPlayerInventory, Container pContainer, ContainerData pData) {
        super(ModMenuTypes.NIGHT_STOVE.get(), pContainerId);
        this.container = pContainer;
        this.data = pData;

        