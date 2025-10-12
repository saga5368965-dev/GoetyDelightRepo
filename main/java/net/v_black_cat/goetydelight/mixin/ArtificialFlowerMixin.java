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

    