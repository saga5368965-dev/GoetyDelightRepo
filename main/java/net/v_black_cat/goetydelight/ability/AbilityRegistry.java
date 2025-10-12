package net.v_black_cat.goetydelight.ability;

import java.util.UUID;

import static net.v_black_cat.goetydelight.ability.TimedAbilitySystem.syncAbilityWithClient;

public class AbilityRegistry {

    public static final String SUGAR_SCEPTER_IMMUNITY = "sugar_scepter_immunity";
    public static final String RUBY_HARD_CANDY_DAMAGE_REDUCTION = "ruby_hard_candy_damage_reduction";
    public static final String NIGHT_STOVE = "night_stove";
    public static void registerAbilities() {

        TimedAbilitySystem.registerAbility(
                SUGAR_SCEPTER_IMMUNITY,
                
                entity -> {
                    if (entity != null) {
                        
                        
                        entity.getPersistentData().putBoolean("hasSugarScepterImmunity", true);
                    }
                },
                
                entity -> {
                    if (entity != null) {
                        
                        entity.getPersistentData().putBoolean("hasSugarScepterImmunity", false);
                        
                    }
                }
        );


        
        TimedAbilitySystem.registerAbility(
                RUBY_HARD_CANDY_DAMAGE_REDUCTION,
                
                entity -> {
                    if (entity != null) {
                        entity.getPersistentData().putBoolean("hasRubyCandyDamageReduction", true);
                    }
                },
                
                entity -> {
                    if (entity != null) {
                        entity.getPersistentData().putBoolean("hasRubyCandyDamageReduction", false);
                    }
                }
        );


        
        TimedAbilitySystem.registerAbility(
                NIGHT_STOVE,
                
                entity -> {
                    if (entity != null) {
                        
                        entity.getPersistentData().putBoolean("hasNightStove", true);
                    }
                },
                
                entity -> {
                    if (entity != null) {
                        
                        entity.getPersistentData().putBoolean("hasNightStove", false);
                        
                    }
                }
        );
    }
}