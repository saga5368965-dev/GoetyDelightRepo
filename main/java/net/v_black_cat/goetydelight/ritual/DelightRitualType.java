package net.v_black_cat.goetydelight.ritual;

import com.Polarice3.Goety.api.ritual.IRitualType;
import com.Polarice3.Goety.api.ritual.RitualType;
import com.Polarice3.Goety.common.blocks.entities.DarkAltarBlockEntity;
import com.Polarice3.Goety.common.blocks.entities.RitualBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.v_black_cat.goetydelight.block.ModBlocks;
import net.v_black_cat.goetydelight.item.ModItems;

import java.util.HashMap;
import java.util.Map;

public class DelightRitualType implements IRitualType {
    @Override
    public String getName() {
        
        return "culinary";
    }

    @Override
    public ItemStack getJeiIcon() {
        
        return new ItemStack(ModBlocks.CURSED_INGOT_POT.get());
    }

    @Override
    public boolean getRequirement(RitualBlockEntity tileEntity, BlockPos pos, Level level) {
        
        return checkDelightRequirements(pos, level);
    }

    private boolean checkDelightRequirements(BlockPos pos, Level level) {
        
        final int RANGE = 5;

        
        Map<Block, Integer> blockRequirements = new HashMap<>();
        blockRequirements.put(Blocks.SMOKER, 2);      
        blockRequirements.put(ModBlocks.SHADE_STOVE.get(), 1);      
        blockRequirements.put(ModBlocks.CURSED_INGOT_POT.get(), 1);      

        
        Map<Block, Integer> blockCounts = new HashMap<>();
        for (Block block : blockRequirements.keySet()) {
            blockCounts.put(block, 0);
        }

        
        for (int i = -RANGE; i <= RANGE; ++i) {
            for (int j = -RANGE; j <= RANGE; ++j) {
                for (int k = -RANGE; k <= RANGE; ++k) {
                    BlockPos checkPos = pos.offset(i, j, k);
                    BlockState state = level.getBlockState(checkPos);
                    Block block = state.getBlock();

                    
                    if (blockRequirements.containsKey(block)) {
                        blockCounts.put(block, blockCounts.get(block) + 1);
                    }
                }
            }
        }

        
        for (Map.Entry<Block, Integer> entry : blockRequirements.entrySet()) {
            Block block = entry.getKey();
            int requiredCount = entry.getValue();
            int actualCount = blockCounts.get(block);

            if (actualCount < requiredCount) {
                return false; 
            }
        }

        return true; 
    }

    
    @Override
    public void onFinishRitual(Level world, BlockPos darkAltarPos,
                               DarkAltarBlockEntity tileEntity,
                               Player castingPlayer,
                               ItemStack activationItem) {
        
        
        
        

        
        
                
                
                
        

        
        world.playSound(null, darkAltarPos, SoundEvents.BELL_RESONATE,
                SoundSource.BLOCKS, 1.0F, 0.5F);

        for (int i = 0; i < 20; i++) {
            world.addParticle(ParticleTypes.HEART,
                    darkAltarPos.getX() + 0.5 + world.random.nextGaussian() * 0.5,
                    darkAltarPos.getY() + 1.5,
                    darkAltarPos.getZ() + 0.5 + world.random.nextGaussian() * 0.5,
                    0, 0.1, 0);
        }
    }


    public static void registerRitualType() {
        DelightRitualType delightRitualType = new DelightRitualType();
        RitualType.create(
                "DELIGHT",
                delightRitualType
        );
    }


}