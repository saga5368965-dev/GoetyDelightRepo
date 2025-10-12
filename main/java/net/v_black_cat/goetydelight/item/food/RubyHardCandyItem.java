package net.v_black_cat.goetydelight.item.food;

import com.Polarice3.Goety.init.ModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.v_black_cat.goetydelight.ability.AbilityRegistry;
import net.v_black_cat.goetydelight.ability.TimedAbilitySystem;

import java.util.UUID;

public class RubyHardCandyItem extends Item {
    
    private static final int DAMAGE_REDUCTION_DURATION = 20 * 60 * 10;
    
    private static final int MAX_POTENCY_LEVEL = 3;
    
    private static final String POTENCY_LEVEL_TAG = "RubyCandyPotencyLevel";
    
    private static final UUID SPELL_POTENCY_UUID = UUID.fromString("8b4513a0-4e2a-11ee-be56-0242ac120004");

    public RubyHardCandyItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            
            int currentLevel = getPotencyLevel(player);

            
            if (currentLevel < MAX_POTENCY_LEVEL) {
                increasePotencyLevel(player);

                
                boolean success = TimedAbilitySystem.addAbilityToEntity(
                        entity,
                        AbilityRegistry.RUBY_HARD_CANDY_DAMAGE_REDUCTION,
                        DAMAGE_REDUCTION_DURATION
                );

                if (success) {
                    
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
            } else {
                
                TimedAbilitySystem.addAbilityToEntity(
                        entity,
                        AbilityRegistry.RUBY_HARD_CANDY_DAMAGE_REDUCTION,
                        DAMAGE_REDUCTION_DURATION
                );

                
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
        }

        return super.finishUsingItem(stack, level, entity);
    }

    
    private int getPotencyLevel(Player player) {
        return player.getPersistentData().getInt(POTENCY_LEVEL_TAG);
    }

    
    private void increasePotencyLevel(Player player) {
        int currentLevel = getPotencyLevel(player);
        if (currentLevel < MAX_POTENCY_LEVEL) {
            player.getPersistentData().putInt(POTENCY_LEVEL_TAG, currentLevel + 1);

            
            applyPotencyEffect(player, currentLevel + 1);
        }
    }

    
    private void applyPotencyEffect(Player player, int level) {
        
        removePotencyEffect(player);

        
        double potencyBonus = 2 * level;

        
        AttributeModifier modifier = new AttributeModifier(
                SPELL_POTENCY_UUID,
                "Ruby Hard Candy Potency Bonus",
                potencyBonus,
                AttributeModifier.Operation.ADDITION
        );

        
        if (player.getAttribute(ModAttributes.SPELL_POTENCY.get()) != null) {
            player.getAttribute(ModAttributes.SPELL_POTENCY.get()).addPermanentModifier(modifier);
        }

        
        player.getPersistentData().putDouble("RubyCandyPotencyValue", potencyBonus);
    }

    
    private void removePotencyEffect(Player player) {
        if (player.getAttribute(ModAttributes.SPELL_POTENCY.get()) != null) {
            
            player.getAttribute(ModAttributes.SPELL_POTENCY.get()).removeModifier(SPELL_POTENCY_UUID);
        }
    }

    
    public static double getCurrentPotencyBonus(Player player) {
        if (player.getAttribute(ModAttributes.SPELL_POTENCY.get()) != null) {
            
            AttributeModifier modifier = player.getAttribute(ModAttributes.SPELL_POTENCY.get()).getModifier(SPELL_POTENCY_UUID);
            if (modifier != null) {
                return modifier.getAmount();
            }
        }
        return 0;
    }

    
    @Mod.EventBusSubscriber
    public static class DamageReductionHandler {

        @SubscribeEvent
        public static void onLivingHurt(LivingHurtEvent event) {
            LivingEntity entity = event.getEntity();

            
            if (entity.level().isClientSide) return;

            
            boolean hasDamageReduction = TimedAbilitySystem.hasAbility(
                    entity,
                    AbilityRegistry.RUBY_HARD_CANDY_DAMAGE_REDUCTION
            );

            
            if (hasDamageReduction) {
                float reducedDamage = event.getAmount() * 0.5f;
                event.setAmount(reducedDamage);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        
        int potencyLevel = player.getPersistentData().getInt("RubyCandyPotencyLevel");
        if (potencyLevel > 0) {
            
            double potencyBonus = 10.0 * potencyLevel;
            AttributeModifier modifier = new AttributeModifier(
                    UUID.fromString("8b4513a0-4e2a-11ee-be56-0242ac120004"),
                    "Ruby Hard Candy Potency Bonus",
                    potencyBonus,
                    AttributeModifier.Operation.ADDITION
            );

            if (player.getAttribute(ModAttributes.SPELL_POTENCY.get()) != null) {
                player.getAttribute(ModAttributes.SPELL_POTENCY.get()).addPermanentModifier(modifier);
            }
        }
    }
}