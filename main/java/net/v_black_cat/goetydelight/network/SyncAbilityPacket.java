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

    public SyncAbilityPacket(int entityId, String abilityId, boolean add) {
        this.entityId = entityId;
        this.abilityId = abilityId;
        this.add = add;
    }

    public static void encode(SyncAbilityPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.entityId);
        buffer.writeUtf(msg.abilityId);
        buffer.writeBoolean(msg.add);
    }

    public static SyncAbilityPacket decode(FriendlyByteBuf buffer) {
        return new SyncAbilityPacket(buffer.readInt(), buffer.readUtf(), buffer.readBoolean());
    }

    public static void handle(SyncAbilityPacket msg, Supplier<NetworkEvent.Context> ctx) {
        
        ctx.get().enqueueWork(() -> {
            
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            Entity entity = mc.level.getEntity(msg.entityId);

            if (entity instanceof net.minecraft.world.entity.LivingEntity livingEntity) {
                if (msg.add) {
                    
                    
                    
                    livingEntity.getPersistentData().putBoolean("ClientSide_" + msg.abilityId, true);
                } else {
                    
                    livingEntity.getPersistentData().remove("ClientSide_" + msg.abilityId);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}