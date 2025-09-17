package net.fightingpainter.mc.coined.network.packets;

import io.netty.buffer.ByteBuf;
import net.fightingpainter.mc.coined.Coined;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

// PlayerLongPayload.java
public record PlayerLongPayload(int entityId, long value)
        implements CustomPacketPayload {

    public static final Type<PlayerLongPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "player_long_payload"));

    public static final StreamCodec<ByteBuf, PlayerLongPayload> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,  PlayerLongPayload::entityId,
                    ByteBufCodecs.VAR_LONG, PlayerLongPayload::value,
                    PlayerLongPayload::new);

    @Override public Type<? extends CustomPacketPayload> type() {return TYPE;}
}
