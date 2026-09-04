package com.fixed.stamina.network;

import com.fixed.stamina.FixedStamina;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.gamerules.GameRules;

public record EatTimeSyncPayload (int initial, int scaling) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, EatTimeSyncPayload> CODEC =
            CustomPacketPayload.codec(EatTimeSyncPayload::write, EatTimeSyncPayload::new);


    public EatTimeSyncPayload(FriendlyByteBuf buf) {
        this(
            buf.readInt(),
            buf.readInt()
        );
    }
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.initial);
        buf.writeInt(this.scaling);
    }

    public static final CustomPacketPayload.Type<EatTimeSyncPayload> ID = new Type<>(FixedStamina.id("eat_time"));

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
