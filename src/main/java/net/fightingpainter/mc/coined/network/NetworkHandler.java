package net.fightingpainter.mc.coined.network;

import net.fightingpainter.mc.coined.data.ModDataTypes;
import net.fightingpainter.mc.coined.network.packets.PlayerLongPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


public class NetworkHandler {
    private static final String NET_VERSION = "1";
    
    public static void onRegister(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(NET_VERSION); //get registrar for network version
        
        registrar.playToClient(PlayerLongPayload.TYPE, PlayerLongPayload.CODEC, NetworkHandler::syncOnClient); //player long payload
    }


    private static void syncOnClient(CustomPacketPayload pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;
            Entity e1 = level.getEntity(((PlayerLongPayload)pkt).entityId());
            if (e1 instanceof Player p)
                p.setData(ModDataTypes.PLAYER_BALANCE, ((PlayerLongPayload)pkt).value());
        });
    }
}
