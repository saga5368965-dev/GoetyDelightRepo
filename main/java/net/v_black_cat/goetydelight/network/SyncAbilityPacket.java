package net.v_black_cat.goetydelight.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.v_black_cat.goetydelight.ability.TimedAbilitySystem;

import java.util.function.Supplier;

public class SyncAbilityPacket {
    private final int entityId;
    private final String abilityId;
    private final boolean add; 