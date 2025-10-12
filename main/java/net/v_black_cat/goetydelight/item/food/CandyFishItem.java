package net.v_black_cat.goetydelight.item.food;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CandyFishItem extends Item {

    public CandyFishItem(Properties pProperties) {
        super(pProperties);
    }


    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player player, @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        