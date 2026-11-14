package net.fightingpainter.mc.coined.network;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.data.ModDataTypes;
import net.fightingpainter.mc.coined.network.packets.PlayerLongPayload;
import net.fightingpainter.mc.coined.network.packets.PlayerBalancePayload;
import net.fightingpainter.mc.coined.network.packets.PurseWindowRequest;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


public class NetworkHandler {
    private static final String NET_VERSION = "2";
    
    public static void onRegister(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(NET_VERSION); //get registrar for network version
        
        registrar.playToClient(PlayerLongPayload.TYPE, PlayerLongPayload.CODEC, NetworkHandler::syncOnClient); //player long payload
        registrar.playToClient(PlayerBalancePayload.TYPE, PlayerBalancePayload.CODEC, ClientPayloadHandler::handlePlayerBalance);
        registrar.playToServer(PurseWindowRequest.TYPE, PurseWindowRequest.CODEC, NetworkHandler::handlePurseWindowRequest);
    }

    private static void handlePurseWindowRequest(PurseWindowRequest request, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof net.minecraft.server.level.ServerPlayer player) || !player.isAlive() || player.isSpectator()) return;
            switch (request.type) {
                case "deposit" -> net.fightingpainter.mc.coined.util.CurrencyTransactions.depositAll(player);
                case "withdraw", "withdraw_coins" -> net.fightingpainter.mc.coined.util.CurrencyTransactions.withdraw(player, request.amount, request.type.equals("withdraw_coins"));
                case "auto_deposit" -> {
                    player.setData(ModDataTypes.AUTO_DEPOSIT, request.amount == 1);
                    net.fightingpainter.mc.coined.data.SyncHelper.sync(player, player);
                }
                default -> { }
            }
        });
    }


    private static void syncOnClient(CustomPacketPayload pkt, IPayloadContext ctx) {
        ClientPayloadHandler.handleTrackedBalance((PlayerLongPayload)pkt, ctx);
    }
}
