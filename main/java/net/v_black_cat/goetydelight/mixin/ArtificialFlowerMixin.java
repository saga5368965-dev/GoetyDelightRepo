package net.v_black_cat.goetydelight.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Pseudo
@Mixin(targets = "auviotre.enigmatic.addon.contents.items.ArtificialFlower")
public class ArtificialFlowerMixin {

    
    private static boolean isEnigmaticAddonsLoaded() {
        return ModList.get().isLoaded("enigmaticaddons");
    }

    @Inject(method = "onConfig", at = @At("TAIL"), remap = false)
    private static void addEffectsToBlacklist(CallbackInfo ci) {
        
        if (!isEnigmaticAddonsLoaded()) {
            return;
        }

        try {
            Class<?> targetClass = Class.forName("auviotre.enigmatic.addon.contents.items.ArtificialFlower");
            java.lang.reflect.Field effectBlackListField = targetClass.getDeclaredField("effectBlackList");
            effectBlackListField.setAccessible(true);

            @SuppressWarnings("unchecked")
            java.util.List<ResourceLocation> effectBlackList =
                    (java.util.List<ResourceLocation>) effectBlackListField.get(null);

            effectBlackList.add(new ResourceLocation("goetydelight", "the_pale_messenger"));

            net.v_black_cat.goetydelight.GoetyDelight.LOGGER.debug("added to ArtificialFlower blacklist successfully");

        } catch (Exception e) {

            net.v_black_cat.goetydelight.GoetyDelight.LOGGER.debug("Failed to add effects to ArtificialFlower blacklist: {}", e.getMessage());
        }
    }
}