package net.v_black_cat.goetydelight.datagen;

import net.v_black_cat.goetydelight.GoetyDelight;
import net.v_black_cat.goetydelight.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, GoetyDelight.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

























        this.tag(BlockTags.FENCES)
                .add(ModBlocks.MARBLE_FENCE.get());
        this.tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.MARBLE_FENCE_GATE.get());
        this.tag(BlockTags.WALLS)
                .add(ModBlocks.MARBLE_WALL.get());


    }
}
