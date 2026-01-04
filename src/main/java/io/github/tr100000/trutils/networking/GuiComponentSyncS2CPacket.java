package io.github.tr100000.trutils.networking;

import io.github.tr100000.trutils.TrUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record GuiComponentSyncS2CPacket(int syncId, int componentIndex, byte[] data) implements CustomPacketPayload {
    public static final Type<GuiComponentSyncS2CPacket> ID = new CustomPacketPayload.Type<>(TrUtils.id("machine_gui_component_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GuiComponentSyncS2CPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, GuiComponentSyncS2CPacket::syncId,
            ByteBufCodecs.VAR_INT, GuiComponentSyncS2CPacket::componentIndex,
            ByteBufCodecs.BYTE_ARRAY, GuiComponentSyncS2CPacket::data,
            GuiComponentSyncS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
