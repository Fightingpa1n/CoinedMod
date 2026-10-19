package net.fightingpainter.mc.coined.data;

import net.fightingpainter.mc.coined.network.packets.PlayerLongPayload;
import net.fightingpainter.mc.coined.network.packets.PlayerBalancePayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public final class SyncHelper {

    /** Send current value of {@code source} to {@code target}. */
    public static void sync(ServerPlayer target, Player source) {
        long v = source.getData(ModDataTypes.PLAYER_BALANCE);
        if (target == source) {
            PacketDistributor.sendToPlayer(target, new PlayerBalancePayload(v));
        } else {
            PacketDistributor.sendToPlayer(target, new PlayerLongPayload(source.getId(), v));
        }
    }

    /** Broadcast a new value after a server-side change. */
    public static void broadcast(Player source, long newValue) {
        if (!(source instanceof ServerPlayer serverPlayer)) return;
        PacketDistributor.sendToPlayer(serverPlayer, new PlayerBalancePayload(newValue));
        PacketDistributor.sendToPlayersTrackingEntity(source, new PlayerLongPayload(source.getId(), newValue));
    }
}
