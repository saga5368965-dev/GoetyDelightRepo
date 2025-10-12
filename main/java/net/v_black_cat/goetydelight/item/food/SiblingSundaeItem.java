package net.v_black_cat.goetydelight.item.food;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.ally.spider.AbstractSpiderServant;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SiblingSundaeItem extends Item {
    public SiblingSundaeItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack resultStack = super.finishUsingItem(stack, level, entity);

        if (!level.isClientSide) {
            
            TagKey<EntityType<?>>[] servantTags = new TagKey[]{
                    TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("goetydelight", "servants_pool/servants_pool_1")), 
                    TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("goetydelight", "servants_pool/servants_pool_2")), 
                    TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("goetydelight", "servants_pool/servants_pool_3")), 
                    TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("goetydelight", "servants_pool/servants_pool_4")), 
                    TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("goetydelight", "servants_pool/servants_pool_5")), 
                    TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("goetydelight", "servants_pool/servants_pool_6"))  
            };

            
            double[] weights = new double[]{50, 35, 12.5, 2.25, 0.2,0.05};
            int[] summonCounts = new int[]{8, 4, 2, 1, 1, 1};
            int[] lifeTimes = new int[]{2400, 6000, 12000, 36000, 72000, 144000}; 
            double[] immortalBaseChance = new double[]{0.5, 0.5, 0.5, 0.5, 0.2, 0.1}; 

            
            int luck = 0;
            if (entity instanceof Player player) {
                luck = (int)player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.LUCK).getValue();
            }

            
            int rollTimes = 1 + Math.min(luck / 3, 4);
            int bestLevel = 0;
            Random random = new Random();
            for (int i = 0; i < rollTimes; i++) {
                int level1 = rollLevel(weights, random);
                if (level1 > bestLevel) bestLevel = level1;
            }

            
            List<EntityType<?>> servantTypes = new ArrayList<>();
            int checkedLevel = bestLevel;
            while (checkedLevel >= 0) {
                servantTypes.clear();
                BuiltInRegistries.ENTITY_TYPE.getTag(servantTags[checkedLevel]).ifPresent(tag -> {
                    for (Holder<EntityType<?>> holder : tag) {
                        servantTypes.add(holder.value());
                    }
                });
                if (!servantTypes.isEmpty()) break;
                checkedLevel--;
            }

            if (!servantTypes.isEmpty()) {
                
                int summonCount = summonCounts[checkedLevel];
                
                double servantImmortalChance = immortalBaseChance[checkedLevel] + Math.min(luck, 50) * 0.01;
                if (checkedLevel == 4) servantImmortalChance = Math.min(servantImmortalChance, 0.2); 
                else servantImmortalChance = Math.min(servantImmortalChance, 1.0);

                
                for (int summonIdx = 0; summonIdx < summonCount; summonIdx++) {
                    EntityType<?> servantTypeToSummon = null;
                    Entity entityToSummon = null;
                    int tryCount = 0;
                    while (tryCount < 10) {
                        servantTypeToSummon = servantTypes.get(random.nextInt(servantTypes.size()));
                        entityToSummon = servantTypeToSummon.create(level);
                        if (entityToSummon != null) break;
                        tryCount++;
                    }
                    if (entityToSummon != null) {
                        boolean isImmortal = random.nextDouble() < servantImmortalChance;
                        int servantLife = isImmortal ? -1 : lifeTimes[checkedLevel];
                        createAndSetupServant((EntityType<? extends Entity>) servantTypeToSummon, level, entity, servantLife);
                    }
                }
            }

            
            if (bestLevel >= 4) {
                double v = random.nextDouble();
                if (v < 0.33) {
                    Cat cat = EntityType.CAT.create(level);
                    if (cat != null) {
                        if (level instanceof ServerLevel serverLevel) {

                            CatVariant allBlack = serverLevel.registryAccess()
                                .registryOrThrow(Registries.CAT_VARIANT)
                                .get(new ResourceLocation("minecraft", "all_black"));
                            if (allBlack != null) {
                                cat.setVariant(allBlack);
                            }
                        }
                        cat.setCustomName(Component.literal("V_BlackCAT"));
                        cat.setCustomNameVisible(true);
                        cat.moveTo(entity.getX(), entity.getY(), entity.getZ(), level.random.nextFloat() * 360F, 0F);
                        cat.setInvulnerable(true);
                        level.addFreshEntity(cat);
                    }
                }
            }
        }
        return resultStack;
    }

    
    private int rollLevel(double[] weights, Random random) {
        double total = 0;
        for (double w : weights) total += w;
        double r = random.nextDouble() * total;
        double acc = 0;
        for (int i = 0; i < weights.length; i++) {
            acc += weights[i];
            if (r < acc) return i;
        }
        return weights.length - 1;
    }

    private void createAndSetupServant(EntityType<? extends Entity> servantType, Level level, LivingEntity owner, int lifeTicks) {
        if (level instanceof ServerLevel serverLevel) {
            Entity entity = servantType.create(level);

            if (entity instanceof AbstractSpiderServant servant){
                servant.setPos(owner.getX(), owner.getY(), owner.getZ());
                servant.setTrueOwner(owner);
                if (lifeTicks > 0) servant.setLimitedLife(lifeTicks);
                
                servant.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(servant.blockPosition()),
                        MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null, (CompoundTag)null);
                processHealthAndEffects(servant);
                level.addFreshEntity(servant);
            } else if (entity instanceof Summoned servant) {
                servant.setPos(owner.getX(), owner.getY(), owner.getZ());
                servant.setTrueOwner(owner);
                if (lifeTicks > 0) servant.setLimitedLife(lifeTicks);
                
                servant.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(servant.blockPosition()),
                        MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                processHealthAndEffects(servant);
                level.addFreshEntity(servant);
            }
        }
    }

    private void processHealthAndEffects(Mob servant) {
        float maxHealth = servant.getMaxHealth();
        if (maxHealth < 60.0f) {
            int healthDeficit = (int) (60.0f - maxHealth);
            int healthBoostLevel = healthDeficit / 3;

            if (healthBoostLevel > 0) {
                servant.addEffect(new MobEffectInstance(
                        GoetyEffects.BUFF.get(),
                        1200,
                        healthBoostLevel - 1,
                        false,
                        true
                ));
            }
        }
    }
}
