package net.fightingpainter.mc.coined.network;

import net.fightingpainter.mc.coined.data.ModDataTypes;
import net.fightingpainter.mc.coined.network.packets.PlayerBalancePayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ClientPayloadHandler {
    private ClientPayloadHandler() {}

    public static void handlePlayerBalance(PlayerBalancePayload payload, IPayloadContext ctx) {
        // Update the client attachment directly so receiving a sync never sends another packet.
        ctx.enqueueWork(() -> ctx.player().setData(ModDataTypes.PLAYER_BALANCE, payload.ballance()));
    }
}
