package net.fightingpainter.mc.coined.data;

import net.fightingpainter.mc.coined.network.packets.PlayerLongPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public final class SyncHelper {

    /** Send current value of {@code source} to {@code target}. */
    public static void sync(ServerPlayer target, Player source) {
        long v = source.getData(ModDataTypes.PLAYER_BALANCE);
        PacketDistributor.sendToPlayer(target, new PlayerLongPayload(source.getId(), v));
    }

    /** Broadcast a new value after a server-side change. */
    public static void broadcast(Player source, long newValue) {
        source.setData(ModDataTypes.PLAYER_BALANCE, newValue);
        PacketDistributor.sendToPlayersTrackingEntity(source, new PlayerLongPayload(source.getId(), newValue));
    }
}