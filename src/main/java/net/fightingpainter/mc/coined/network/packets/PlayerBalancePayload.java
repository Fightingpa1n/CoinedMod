package net.fightingpainter.mc.coined.network.packets;

import io.netty.buffer.ByteBuf;
import net.fightingpainter.mc.coined.Coined;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PlayerBalancePayload(long ballance) implements CustomPacketPayload {
    public static final Type<PlayerBalancePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "player_balance"));

    public static final StreamCodec<ByteBuf, PlayerBalancePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, PlayerBalancePayload::ballance,
            PlayerBalancePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
