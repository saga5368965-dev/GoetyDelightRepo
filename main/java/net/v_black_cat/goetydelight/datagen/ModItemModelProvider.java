package net.v_black_cat.goetydelight.datagen;


import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.v_black_cat.goetydelight.GoetyDelight;
import net.v_black_cat.goetydelight.block.ModBlocks;
import net.v_black_cat.goetydelight.item.ModItems;



public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, GoetyDelight.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        simpleHandHoldItem(ModItems.APOCALYPTIUM_KNIFE);
        simpleHandHoldItem(ModItems.VENOMOUS_SPIDER_KNIFE);
        simpleHandHoldItem(ModItems.SPECTRE_KNIFE);
        simpleHandHoldItem(ModItems.APOCALYPTIUM_KNIFE2);
        simpleHandHoldItem(ModItems.APOCALYPTIUM_KNIFE1);
