package net.v_black_cat.goetydelight.ability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.v_black_cat.goetydelight.network.NetworkHandler;
import net.v_black_cat.goetydelight.network.SyncAbilityPacket;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public class TimedAbilitySystem {
    
    public static final Capability<EntityTimedAbilities> ENTITY_TIMED_ABILITIES =
            CapabilityManager.get(new CapabilityToken<>() {});

    
    private static final Map<String, AbilityDefinition> ABILITY_REGISTRY = new ConcurrentHashMap<>();

    
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(EntityTimedAbilities.class);
    }

    
    public static void registerAbility(String abilityId, AbilityApplier applier, AbilityRemover remover) {
        ABILITY_REGISTRY.put(abilityId, new AbilityDefinition(applier, remover));
    }

    
    public static Optional<AbilityDefinition> getAbilityDefinition(String abilityId) {
        return Optional.ofNullable(ABILITY_REGISTRY.get(abilityId));
    }

    
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide) return;

        event.getEntity().getCapability(ENTITY_TIMED_ABILITIES).ifPresent(abilities -> {
            abilities.tick(event.getEntity());
        });
    }

    
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide) return;

        event.getEntity().getCapability(ENTITY_TIMED_ABILITIES).ifPresent(abilities -> {
            abilities.clearAbilitiesOnDeath(event.getEntity());
        });
    }

    
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity().level().isClientSide) return;

        event.getEntity().getCapability(ENTITY_TIMED_ABILITIES).ifPresent(abilities -> {
            abilities.clearAbilitiesOnRespawn(event.getEntity());
        });
    }

    
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) return; 

        LivingEntity original = event.getOriginal();
        LivingEntity newEntity = event.getEntity();

        original.reviveCaps(); 

        original.getCapability(ENTITY_TIMED_ABILITIES).ifPresent(oldAbilities -> {
            newEntity.getCapability(ENTITY_TIMED_ABILITIES).ifPresent(newAbilities -> {
                
                CompoundTag nbt = oldAbilities.serializeNBT();
                newAbilities.deserializeNBT(nbt);
            });
        });

        original.invalidateCaps();
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(
                    new ResourceLocation("goetydelight", "timed_abilities"),
                    new ICapabilityProvider() {
                        private final LazyOptional<EntityTimedAbilities> instance = LazyOptional.of(EntityTimedAbilities::new);

                        @Override
                        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
                            return ENTITY_TIMED_ABILITIES.orEmpty(cap, instance.cast());
                        }
                    }
            );
        }
    }


    
    public static boolean hasAbility(LivingEntity entity, String abilityId) {
        
        if (entity.getPersistentData().contains("has" + abilityId.substring(0, 1).toUpperCase() + abilityId.substring(1))) {
            return entity.getPersistentData().getBoolean("has" + abilityId.substring(0, 1).toUpperCase() + abilityId.substring(1));
        }
        
        
        LazyOptional<EntityTimedAbilities> capabilities = entity.getCapability(ENTITY_TIMED_ABILITIES);
        if (capabilities.isPresent()) {
            EntityTimedAbilities abilities = capabilities.orElseThrow(IllegalStateException::new);
            return abilities.hasAbility(abilityId);
        }
        return false;
    }

    
    public static int getAbilityRemainingTime(LivingEntity entity, String abilityId) {
        LazyOptional<EntityTimedAbilities> capabilities = entity.getCapability(ENTITY_TIMED_ABILITIES);
        if (capabilities.isPresent()) {
            EntityTimedAbilities abilities = capabilities.orElseThrow(IllegalStateException::new);
            return abilities.getRemainingTime(abilityId);
        }
        return 0;
    }

    
    public static class EntityTimedAbilities implements ICapabilitySerializable<CompoundTag> {
        private final Map<String, TimedAbility> activeAbilities = new HashMap<>();
        private final List<Runnable> pendingRemovals = new ArrayList<>();
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == ENTITY_TIMED_ABILITIES ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
        }

        
        public void addAbility(String abilityId, int durationTicks, AbilityApplier applier, AbilityRemover remover) {
            TimedAbility ability = new TimedAbility(abilityId, durationTicks, applier, remover);
            activeAbilities.put(abilityId, ability);

            
            applier.apply(null); 
        }

        
        public void removeAbility(String abilityId, LivingEntity entity) {
            TimedAbility ability = activeAbilities.get(abilityId);
            if (ability != null) {
                ability.remover.remove(entity);
                activeAbilities.remove(abilityId);
                
                
                if (entity instanceof Player) {
                    syncAbilityWithClient(entity, abilityId, false);
                }
            }
        }

        
        public boolean hasAbility(String abilityId) {
            return activeAbilities.containsKey(abilityId);
        }

        
        public int getRemainingTime(String abilityId) {
            TimedAbility ability = activeAbilities.get(abilityId);
            return ability != null ? ability.remainingTicks : 0;
        }

        
        public void tick(LivingEntity entity) {
            pendingRemovals.clear();

            for (TimedAbility ability : activeAbilities.values()) {
                
                if (ability.remainingTicks == ability.initialDuration) {
                    ability.applier.apply(entity);
                }

                ability.remainingTicks--;

                if (ability.remainingTicks <= 0) {
                    ability.remover.remove(entity);
                    pendingRemovals.add(() -> activeAbilities.remove(ability.abilityId));

                    
                    if (entity instanceof Player player) {
                        
                        
                        
                        syncAbilityWithClient(entity, ability.abilityId, false);
                    }
                }
            }

            
            pendingRemovals.forEach(Runnable::run);
        }

        
        public void clearAbilitiesOnDeath(LivingEntity entity) {
            for (TimedAbility ability : activeAbilities.values()) {
                ability.remover.remove(entity);
                pendingRemovals.add(() -> activeAbilities.remove(ability.abilityId));
                
                
                if (entity instanceof Player) {
                    syncAbilityWithClient(entity, ability.abilityId, false);
                }
            }
            pendingRemovals.forEach(Runnable::run);
        }

        
        public void clearAbilitiesOnRespawn(LivingEntity entity) {
            clearAbilitiesOnDeath(entity);
        }

        
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            CompoundTag abilitiesTag = new CompoundTag();

            for (TimedAbility ability : activeAbilities.values()) {
                CompoundTag abilityTag = new CompoundTag();
                abilityTag.putInt("remainingTicks", ability.remainingTicks);
                abilityTag.putInt("initialDuration", ability.initialDuration);
                abilitiesTag.put(ability.abilityId, abilityTag);
            }

            tag.put("Abilities", abilitiesTag);
            return tag;
        }

        
        public void deserializeNBT(CompoundTag tag) {
            activeAbilities.clear();

            if (tag.contains("Abilities")) {
                CompoundTag abilitiesTag = tag.getCompound("Abilities");

                for (String abilityId : abilitiesTag.getAllKeys()) {
                    CompoundTag abilityTag = abilitiesTag.getCompound(abilityId);
                    int remainingTicks = abilityTag.getInt("remainingTicks");
                    int initialDuration = abilityTag.getInt("initialDuration");

                    
                    Optional<AbilityDefinition> definition = getAbilityDefinition(abilityId);
                    if (definition.isPresent()) {
                        AbilityDefinition def = definition.get();
                        TimedAbility ability = new TimedAbility(
                                abilityId,
                                initialDuration,
                                def.applier,
                                def.remover
                        );
                        ability.remainingTicks = remainingTicks;

                        activeAbilities.put(abilityId, ability);
                    }
                }
            }
        }
    }

    
    private static class TimedAbility {
        public final String abilityId;
        public final int initialDuration;
        public int remainingTicks;
        public final AbilityApplier applier;
        public final AbilityRemover remover;

        public TimedAbility(String abilityId, int durationTicks, AbilityApplier applier, AbilityRemover remover) {
            this.abilityId = abilityId;
            this.initialDuration = durationTicks;
            this.remainingTicks = durationTicks;
            this.applier = applier;
            this.remover = remover;
        }
    }

    
    public static class AbilityDefinition {
        public final AbilityApplier applier;
        public final AbilityRemover remover;

        public AbilityDefinition(AbilityApplier applier, AbilityRemover remover) {
            this.applier = applier;
            this.remover = remover;
        }
    }

    
    @FunctionalInterface
    public interface AbilityApplier {
        void apply(LivingEntity entity);
    }

    
    @FunctionalInterface
    public interface AbilityRemover {
        void remove(LivingEntity entity);
    }

    
    public static LazyOptional<EntityTimedAbilities> getAbilities(LivingEntity entity) {
        return entity.getCapability(ENTITY_TIMED_ABILITIES);
    }

    

    public static void syncAbilityWithClient(LivingEntity entity, String abilityId, boolean added) {
        if (!entity.level().isClientSide) {
            
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                    new SyncAbilityPacket(entity.getId(), abilityId, added)
            );
            
            if (entity instanceof ServerPlayer serverPlayer) {
                NetworkHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new SyncAbilityPacket(entity.getId(), abilityId, added)
                );
            }
        }
    }


    public static boolean addAbilityToEntity(LivingEntity entity, String abilityId, int durationTicks) {
        LazyOptional<EntityTimedAbilities> capabilities = entity.getCapability(ENTITY_TIMED_ABILITIES);
        if (capabilities.isPresent()) {
            EntityTimedAbilities abilities = capabilities.orElseThrow(IllegalStateException::new);
            Optional<AbilityDefinition> definition = getAbilityDefinition(abilityId);
            if (definition.isPresent()) {
                AbilityDefinition def = definition.get();
                abilities.addAbility(abilityId, durationTicks, def.applier, def.remover);
                
                
                if (entity instanceof Player) {
                    syncAbilityWithClient(entity, abilityId, true);
                }
                
                return true;
            }
        }
        return false;
    }

    public static boolean removeAbilityFromEntity(LivingEntity entity, String abilityId) {
        LazyOptional<EntityTimedAbilities> capabilities = entity.getCapability(ENTITY_TIMED_ABILITIES);
        if (capabilities.isPresent()) {
            EntityTimedAbilities abilities = capabilities.orElseThrow(IllegalStateException::new);
            abilities.removeAbility(abilityId, entity);

            
            if (entity instanceof Player) {
                syncAbilityWithClient(entity, abilityId, false);
            }

            return true;
        }
        return false;
    }
}