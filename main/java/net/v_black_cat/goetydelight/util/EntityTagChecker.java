package net.v_black_cat.goetydelight.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTagChecker {
    
    public static boolean isEntityInTag(Entity entity, String tagId) {
        
        TagKey<EntityType<?>> tagKey = TagKey.create(
                ForgeRegistries.ENTITY_TYPES.getRegistryKey(),
                new ResourceLocation(tagId)
        );

        
        EntityType<?> entityType = entity.getType();

        
        return entityType.is(tagKey);
    }

    
    public static void checkRaider(Entity entity) {
        if (isEntityInTag(entity, "minecraft:raiders")) {
            System.out.println(entity.getName().getString() + " 是袭击者!");
        }
    }
}