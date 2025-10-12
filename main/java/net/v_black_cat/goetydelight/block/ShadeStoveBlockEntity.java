package net.v_black_cat.goetydelight.block;

import com.Polarice3.Goety.api.items.magic.ITotem;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.v_black_cat.goetydelight.screen.ShadeStoveMenu;

import javax.annotation.Nullable;
import java.util.UUID;

import static com.Polarice3.Goety.common.items.ModItems.SOUL_TRANSFER;

public class ShadeStoveBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    
    private static final int SOUL_ENERGY_COST = 50; 
    private static final int BURN_TIME_GAIN = 100; 

    
    private static final float COOKING_SPEED_MULTIPLIER = 1.5f; 
    private static final float FUEL_CONSUMPTION_MULTIPLIER = 1.5f; 

    private static final int SLOT_INPUT = 0;
    private static final int SLOT_FUEL = 1;
    private static final int SLOT_RESULT = 2;
    private static final int[] SLOTS_FOR_UP = new int[]{SLOT_INPUT};
    private static final int[] SLOTS_FOR_DOWN = new int[]{SLOT_RESULT, SLOT_FUEL};
    private static final int[] SLOTS_FOR_SIDES = new int[]{SLOT_FUEL};

    
    private int litTime;
    private int litDuration;
    private int cookingProgress;
    private int cookingTotalTime;

    
    protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

    
    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> litTime;
                case 1 -> litDuration;
                case 2 -> cookingProgress;
                case 3 -> cookingTotalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> litTime = value;
                case 1 -> litDuration = value;
                case 2 -> cookingProgress = value;
                case 3 -> cookingTotalTime = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;

    public ShadeStoveBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SHADE_STOVE_BE.get(), pPos, pBlockState);
        this.recipeType = RecipeType.SMOKING;
        this.quickCheck = RecipeManager.createCheck(this.recipeType);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.smoker");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory) {
        return new ShadeStoveMenu(pContainerId, pPlayerInventory, this, this.dataAccess);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return this.items.get(pIndex);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return ContainerHelper.removeItem(this.items, pIndex, pCount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ContainerHelper.takeItem(this.items, pIndex);
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        ItemStack itemstack = this.items.get(pIndex);
        boolean flag = !pStack.isEmpty() && ItemStack.isSameItemSameTags(itemstack, pStack);
        this.items.set(pIndex, pStack);

        if (pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }

        if (pIndex == SLOT_INPUT && !flag) {
            this.cookingTotalTime = getTotalCookTime();
            this.cookingProgress = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return pPlayer.distanceToSqr(
                    (double)this.worldPosition.getX() + 0.5D,
                    (double)this.worldPosition.getY() + 0.5D,
                    (double)this.worldPosition.getZ() + 0.5D
            ) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, this.items);
        this.litTime = pTag.getInt("BurnTime");
        this.cookingProgress = pTag.getInt("CookTime");
        this.cookingTotalTime = pTag.getInt("CookTimeTotal");
        this.litDuration = this.getBurnDuration(this.items.get(SLOT_FUEL));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("BurnTime", this.litTime);
        pTag.putInt("CookTime", this.cookingProgress);
        pTag.putInt("CookTimeTotal", this.cookingTotalTime);
        ContainerHelper.saveAllItems(pTag, this.items);
    }

    @Override
    public int[] getSlotsForFace(Direction pSide) {
        if (pSide == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return pSide == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return this.canPlaceItem(pIndex, pItemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        if (pDirection == Direction.DOWN && pIndex == SLOT_FUEL) {
            return pStack.is(net.minecraft.world.item.Items.WATER_BUCKET) || pStack.is(net.minecraft.world.item.Items.BUCKET);
        } else {
            return true;
        }
    }

    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        if (pIndex == SLOT_RESULT) {
            return false;
        } else if (pIndex != SLOT_FUEL) {
            return true;
        } else {
            ItemStack itemstack = this.items.get(SLOT_FUEL);
            return isFuel(pStack) || pStack.is(net.minecraft.world.item.Items.BUCKET) && !itemstack.is(net.minecraft.world.item.Items.BUCKET);
        }
    }

    
    private boolean consumeSoulEnergy(int amount) {
        ItemStack soulSource = this.items.get(SLOT_FUEL);
        if (soulSource.isEmpty()) return false;
        if (this.level == null) return false;

        
        if (soulSource.getItem() == SOUL_TRANSFER.get() && soulSource.getTag() != null &&
                soulSource.getTag().contains("owner")) {

            UUID ownerUuid = soulSource.getTag().getUUID("owner");
            Player owner = this.level.getPlayerByUUID(ownerUuid);
            if (owner == null) return false;
            if (!SEHelper.getSEActive(owner) || SEHelper.getSESouls(owner) < amount) return false;

            
            SEHelper.decreaseSESouls(owner, amount);
            SEHelper.sendSEUpdatePacket(owner);

            
            if (this.level instanceof ServerLevel serverLevel) {
                BlockPos pos = this.getBlockPos();
                serverLevel.sendParticles(ParticleTypes.SOUL,
                        pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                        5, 0.2, 0.0, 0.2, 0.05);
            }
            return true;
        }
        
        else if (soulSource.getItem() instanceof ITotem && soulSource.getTag() != null &&
                soulSource.getTag().contains("Souls")) {

            int souls = soulSource.getTag().getInt("Souls");
            if (souls >= amount) {
                
                soulSource.getTag().putInt("Souls", souls - amount);

                
                if (this.level instanceof ServerLevel serverLevel) {
                    BlockPos pos = this.getBlockPos();
                    serverLevel.sendParticles(ParticleTypes.SOUL,
                            pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                            5, 0.2, 0.0, 0.2, 0.05);
                }

                
                this.items.set(SLOT_FUEL, soulSource);
                return true;
            }
        }

        return false;
    }

    
    public static boolean isFuel(ItemStack pStack) {
        return pStack.getItem() instanceof ITotem ||
                pStack.getItem() == SOUL_TRANSFER.get() ||
                net.minecraftforge.common.ForgeHooks.getBurnTime(pStack, RecipeType.SMOKING) > 0;
    }

    
    protected int getBurnDuration(ItemStack pFuel) {
        if (pFuel.isEmpty()) {
            return 0;
        } else {
            
            if (pFuel.getItem() instanceof ITotem || pFuel.getItem() == SOUL_TRANSFER.get()) {
                return 0;
            }

            
            int baseBurnTime = net.minecraftforge.common.ForgeHooks.getBurnTime(pFuel, this.recipeType);
            return (int)(baseBurnTime / FUEL_CONSUMPTION_MULTIPLIER);
        }
    }

    
    private boolean isLit() {
        return this.litTime > 0;
    }

    
    private boolean canBurn(@Nullable Recipe<?> pRecipe) {
        if (this.items.get(SLOT_INPUT).isEmpty() || pRecipe == null) {
            return false;
        }

        ItemStack result = ((Recipe<Container>) pRecipe).assemble(this, this.level.registryAccess());
        if (result.isEmpty()) {
            return false;
        }

        ItemStack currentResult = this.items.get(SLOT_RESULT);
        if (currentResult.isEmpty()) {
            return true;
        } else if (!ItemStack.isSameItem(currentResult, result)) {
            return false;
        } else {
            return currentResult.getCount() + result.getCount() <= this.getMaxStackSize() &&
                    currentResult.getCount() + result.getCount() <= currentResult.getMaxStackSize();
        }
    }

    
    private boolean burn(@Nullable Recipe<?> pRecipe) {
        if (pRecipe != null && this.canBurn(pRecipe)) {
            ItemStack input = this.items.get(SLOT_INPUT);
            ItemStack result = ((Recipe<Container>) pRecipe).assemble(this, this.level.registryAccess());
            ItemStack currentResult = this.items.get(SLOT_RESULT);

            if (currentResult.isEmpty()) {
                this.items.set(SLOT_RESULT, result.copy());
            } else if (currentResult.is(result.getItem())) {
                currentResult.grow(result.getCount());
            }

            
            if (input.is(net.minecraft.world.level.block.Blocks.WET_SPONGE.asItem()) &&
                    !this.items.get(SLOT_FUEL).isEmpty() &&
                    this.items.get(SLOT_FUEL).is(net.minecraft.world.item.Items.BUCKET)) {
                this.items.set(SLOT_FUEL, new ItemStack(net.minecraft.world.item.Items.WATER_BUCKET));
            }

            input.shrink(1);
            return true;
        }
        return false;
    }

    
    private int getTotalCookTime() {
        int baseCookTime = this.quickCheck.getRecipeFor(this, this.level)
                .map(AbstractCookingRecipe::getCookingTime)
                .orElse(200);

        
        return (int)(baseCookTime / COOKING_SPEED_MULTIPLIER);
    }

    
    public static void serverTick(Level level, BlockPos pos, BlockState state, ShadeStoveBlockEntity blockEntity) {
        boolean isLitBefore = blockEntity.isLit();
        boolean changed = false;

        
        if (blockEntity.isLit()) {
            
            blockEntity.litTime = Math.max(0, blockEntity.litTime - (int)blockEntity.FUEL_CONSUMPTION_MULTIPLIER);
        }

        ItemStack fuelStack = blockEntity.items.get(SLOT_FUEL);
        boolean hasFuel = !fuelStack.isEmpty() && isFuel(fuelStack);
        boolean hasInput = !blockEntity.items.get(SLOT_INPUT).isEmpty();

        if (blockEntity.isLit() || hasFuel && hasInput) {
            Recipe<?> recipe = hasInput ?
                    blockEntity.quickCheck.getRecipeFor(blockEntity, level).orElse(null) : null;

            
            if (!blockEntity.isLit() && hasInput && blockEntity.consumeSoulEnergy(SOUL_ENERGY_COST)) {
                
                blockEntity.litTime = (int)(BURN_TIME_GAIN / FUEL_CONSUMPTION_MULTIPLIER);
                blockEntity.litDuration = blockEntity.litTime;
                changed = true;

                
                if (recipe != null) {
                    blockEntity.cookingTotalTime = blockEntity.getTotalCookTime();
                } else {
                    blockEntity.cookingTotalTime = (int)(200 / COOKING_SPEED_MULTIPLIER);
                }
            }
            
            else if (!blockEntity.isLit() && blockEntity.canBurn(recipe)) {
                blockEntity.litTime = blockEntity.getBurnDuration(fuelStack);
                blockEntity.litDuration = blockEntity.litTime;

                if (blockEntity.isLit()) {
                    changed = true;
                    if (fuelStack.hasCraftingRemainingItem()) {
                        blockEntity.items.set(SLOT_FUEL, fuelStack.getCraftingRemainingItem());
                    } else if (hasFuel) {
                        fuelStack.shrink(1);
                        if (fuelStack.isEmpty()) {
                            blockEntity.items.set(SLOT_FUEL, fuelStack.getCraftingRemainingItem());
                        }
                    }
                }
            }

            if (blockEntity.isLit() && blockEntity.canBurn(recipe)) {
                
                blockEntity.cookingProgress += COOKING_SPEED_MULTIPLIER;
                if (blockEntity.cookingProgress >= blockEntity.cookingTotalTime) {
                    blockEntity.cookingProgress = 0;
                    blockEntity.cookingTotalTime = blockEntity.getTotalCookTime();
                    if (blockEntity.burn(recipe)) {
                        
                        
                    }
                    changed = true;
                }
            } else {
                blockEntity.cookingProgress = 0;
            }
        } else if (!blockEntity.isLit() && blockEntity.cookingProgress > 0) {
            
            blockEntity.cookingProgress = Mth.clamp(
                    (int)(blockEntity.cookingProgress - 2 * FUEL_CONSUMPTION_MULTIPLIER),
                    0,
                    blockEntity.cookingTotalTime
            );
        }

        
        if (isLitBefore != blockEntity.isLit()) {
            changed = true;

            level.setBlock(pos, state, 3);
        }

        if (changed) {
            blockEntity.setChanged();
        }
    }
}