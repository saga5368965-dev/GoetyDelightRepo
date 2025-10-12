package net.v_black_cat.goetydelight.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ToolUtils {
    public static boolean isPickaxe(ItemStack stack) {
        
        return stack.is(ItemTags.PICKAXES);
    }

    
    public ResourceLocation getItemTexture(Item item) {
        
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
        if (itemId == null) {
            
            return new ResourceLocation("minecraft", "textures/item/dirt.png");
        }
        
        
        return new ResourceLocation(itemId.getNamespace(), "textures/item/" + itemId.getPath() + ".png");
    }

}