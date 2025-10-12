package net.v_black_cat.goetydelight.item.food;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.v_black_cat.goetydelight.network.NetworkHandler;
import net.v_black_cat.goetydelight.network.ThrowSoupPacket;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static net.v_black_cat.goetydelight.util.TimeConverter.sToTick;

public class RejectedDarkMeatSoupItem extends Item {

    public RejectedDarkMeatSoupItem(Properties pProperties) {
        super(pProperties);
    }

    public @NotNull String getDescriptionId(ItemStack stack) {
        return this.getOrCreateDescriptionId();
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return (int) (32 * 2.5);
    }


    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }



    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            throwPotion(stack, player.level(), player);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return true;
        }
        return false;
    }


    