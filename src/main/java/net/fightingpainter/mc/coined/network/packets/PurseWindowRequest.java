package net.fightingpainter.mc.coined.network.packets;

import io.netty.buffer.ByteBuf;
import net.fightingpainter.mc.coined.Coined;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public final class PurseWindowRequest implements CustomPacketPayload {
    public static final Type<PurseWindowRequest> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Coined.MOD_ID, "purse_window_request"));

    public static final StreamCodec<ByteBuf, PurseWindowRequest> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, request -> request.type,
            ByteBufCodecs.VAR_LONG, request -> request.amount,
            PurseWindowRequest::new);

    public final String type;
    public final long amount;

    public PurseWindowRequest(String type, long amount) {
        this.type = type;
        this.amount = amount;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
