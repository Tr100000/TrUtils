package io.github.tr100000.trutils.networking;

import io.github.tr100000.trutils.api.gui.component.ClientComponentScreenHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

public final class TrUtilsClientNetworking {
    private TrUtilsClientNetworking() {}

    public static void registerClientRecievers() {
        ClientPlayNetworking.registerGlobalReceiver(GuiComponentSyncS2CPacket.ID, (payload, context) ->
                context.client().execute(() -> {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.copiedBuffer(payload.data()));
                    try {
                        if (context.player().containerMenu.containerId == payload.syncId()) {
                            ClientComponentScreenHandler<?> screenHandler = (ClientComponentScreenHandler<?>)context.player().containerMenu;
                            screenHandler.components.get(payload.componentIndex()).readPacket(context.player().level(), buf);
                        }
                    }
                    finally {
                        buf.release();
                    }
                }));
    }
}
