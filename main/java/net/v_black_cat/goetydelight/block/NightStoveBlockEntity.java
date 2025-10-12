package net.v_black_cat.goetydelight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.v_black_cat.goetydelight.ability.AbilityRegistry;
import net.v_black_cat.goetydelight.ability.TimedAbilitySystem;
import net.v_black_cat.goetydelight.screen.NightStoveMenu;

import javax.annotation.Nullable;
import java.util.List;

public class NightStoveBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    
    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{1};
    private static final int[] SLOTS_FOR_SIDES = new int[]{0};

    
    private int cookingProgress;
    private int cookingTotalTime;

    
    private int effectTimer;

    
    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    
    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> cookingProgress;
                case 1 -> cookingTotalTime;
                case 2 -> effectTimer; 
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> cookingProgress = value;
                case 1 -> cookingTotalTime = value;
                case 2 -> effectTimer = value; 
            }
        }

        @Override
        public int getCount() {
            return 3; 
        }
    };

    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;

    public NightStoveBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.NIGHT_STOVE_BE.get(), pPos, pBlockState);
        this.recipeType = RecipeType.SMOKING;
        this.quickCheck = RecipeManager.createCheck(this.recipeType);
        
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("goetydelight.container.night_stove");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory) {
        return new NightStoveMenu(pContainerId, pPlayerInventory, this, this.dataAccess);
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

        if (pIndex == 0 && !flag) {
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
        this.cookingProgress = pTag.getInt("CookTime");
        this.cookingTotalTime = pTag.getInt("CookTimeTotal");
        this.effectTimer = pTag.getInt("EffectTimer"); 
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("CookTime", this.cookingProgress);
        pTag.putInt("CookTimeTotal", this.cookingTotalTime);
        pTag.putInt("EffectTimer", this.effectTimer); 
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
        return true;
    }

    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        return pIndex == 0; 
    }

    
    private boolean isWorking() {
        return this.cookingProgress > 0;
    }

    
    private boolean canBurn(@Nullable Recipe<?> pRecipe) {
        if (this.items.get(0).isEmpty() || pRecipe == null) {
            return false;
        }

        ItemStack result = ((Recipe<Container>) pRecipe).assemble(this, this.level.registryAccess());
        if (result.isEmpty()) {
            return false;
        }

        ItemStack currentResult = this.items.get(1);
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
            ItemStack input = this.items.get(0);
            ItemStack result = ((Recipe<Container>) pRecipe).assemble(this, this.level.registryAccess());
            ItemStack currentResult = this.items.get(1);

            if (currentResult.isEmpty()) {
                this.items.set(1, result.copy());
            } else if (currentResult.is(result.getItem())) {
                currentResult.grow(result.getCount());
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

        
        float speedMultiplier = getCookingSpeedMultiplier();
        return (int)(baseCookTime / speedMultiplier);
    }

    
    private float getCookingSpeedMultiplier() {
        if (level == null) return 2.0f;

        
        int skyLight = level.getBrightness(LightLayer.SKY, worldPosition);
        
        int blockLight = level.getBrightness(LightLayer.BLOCK, worldPosition);

        
        boolean isNighttime = skyLight <= 10 && blockLight <= 10;

        return isNighttime ? 4.0f : 2.0f;
    }

    
    private void applyEffectsToNearbyPlayers() {
        if (level == null || level.isClientSide) return;

        
        AABB area = new AABB(
                worldPosition.getX() - 32, worldPosition.getY() - 32, worldPosition.getZ() - 32,
                worldPosition.getX() + 32, worldPosition.getY() + 32, worldPosition.getZ() + 32
        );

        List<Player> players = level.getEntitiesOfClass(Player.class, area);

        for (Player player : players) {
            
            player.addEffect(new MobEffectInstance(
                    MobEffects.FIRE_RESISTANCE,
                    200, 
                    0,
                    false,
                    false
            ));

            
            player.addEffect(new MobEffectInstance(
                    MobEffects.REGENERATION,
                    200, 
                    0,
                    false,
                    false
            ));

            
            TimedAbilitySystem.addAbilityToEntity(
                    player,
                    AbilityRegistry.NIGHT_STOVE,
                    200 
            );
        }
    }

    
    public static void serverTick(Level level, BlockPos pos, BlockState state, NightStoveBlockEntity blockEntity) {
        boolean isWorkingBefore = blockEntity.isWorking();
        boolean changed = false;

        boolean hasInput = !blockEntity.items.get(0).isEmpty();

        if (hasInput) {
            Recipe<?> recipe = blockEntity.quickCheck.getRecipeFor(blockEntity, level).orElse(null);

            if (blockEntity.canBurn(recipe)) {
                
                float speedMultiplier = blockEntity.getCookingSpeedMultiplier();
                blockEntity.cookingProgress += speedMultiplier;

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
        } else if (blockEntity.cookingProgress > 0) {
            blockEntity.cookingProgress = Mth.clamp(blockEntity.cookingProgress - 2, 0, blockEntity.cookingTotalTime);
        }

        
        blockEntity.effectTimer++;
        if (blockEntity.effectTimer >= 100) { 
            blockEntity.effectTimer = 0;
            blockEntity.applyEffectsToNearbyPlayers();
        }

        if (isWorkingBefore != blockEntity.isWorking()) {
            changed = true;
            
            level.setBlock(pos, state, 3);
        }

        if (changed) {
            blockEntity.setChanged();
        }
    }
}