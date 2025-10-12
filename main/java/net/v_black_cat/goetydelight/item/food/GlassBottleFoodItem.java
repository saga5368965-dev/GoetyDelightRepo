package net.v_black_cat.goetydelight.item.food;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class GlassBottleFoodItem extends Item {
    public GlassBottleFoodItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        
        ItemStack result = super.finishUsingItem(stack, level, entity);

        
        if (entity instanceof Player player) {
            if (player.getAbilities().instabuild) {
                return result; 
            }

            
            if (result.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE); 
            } else if (!player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE))) {
                player.drop(new ItemStack(Items.GLASS_BOTTLE), false); 
            }
        }

        return result;
    }

}
