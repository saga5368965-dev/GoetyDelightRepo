package net.v_black_cat.goetydelight.item.food;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class CakeItem extends Item {

    private static final double EFFECT_RADIUS = 32.0;
    private static final String REMAINING_USES_TAG = "remaining_uses";
    private static final int MAX_USES = 8;

    public CakeItem(Properties properties) {
        super(properties.stacksTo(1)); 
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false; 
    }



    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        
        if (entity instanceof Player player) {
            player.getFoodData().eat(this, stack);
        } else {
            entity.eat(level, stack);
        }

        
        if (!level.isClientSide && entity instanceof Player player) {
            
            CompoundTag nbt = stack.getOrCreateTag();
            int remainingUses = nbt.getInt(REMAINING_USES_TAG);

            
            if (remainingUses == 0) {
                remainingUses = MAX_USES;
            }

            
            remainingUses--;
            nbt.putInt(REMAINING_USES_TAG, remainingUses);

            
            AABB effectArea = new AABB(
                    player.position().subtract(EFFECT_RADIUS, EFFECT_RADIUS, EFFECT_RADIUS),
                    player.position().add(EFFECT_RADIUS, EFFECT_RADIUS, EFFECT_RADIUS)
            );

            List<Mob> nearbyEntities = level.getEntitiesOfClass(Mob.class, effectArea);
            int kills = 0;

            for (Mob target : nearbyEntities) {
                if (isTargetEntity(target)) {
                    float maxHealth = target.getHealth();

                    player.heal(maxHealth);
                    target.hurt(level.damageSources().magic(), Integer.MAX_VALUE);
                    kills++;
                    addDeathEffects(level, target);
                }
            }

            
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PHANTOM_DEATH, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (kills > 0) {
                