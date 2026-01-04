package io.github.tr100000.trutils.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class TrUtilsNetworking {
    private TrUtilsNetworking() {}

    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(GuiComponentSyncS2CPacket.ID, GuiComponentSyncS2CPacket.CODEC);
    }
}
