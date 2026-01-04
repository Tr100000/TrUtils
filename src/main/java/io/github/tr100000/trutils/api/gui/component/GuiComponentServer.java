package io.github.tr100000.trutils.api.gui.component;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;

public interface GuiComponentServer<D> extends GuiComponent {
    default void update() {}
    D copyData();
    boolean needsSync(D cachedData);
    default void writeInitPacket(RegistryFriendlyByteBuf buf) {
        writePacket(buf);
    }
    void writePacket(RegistryFriendlyByteBuf buf);

    Identifier getId();

    interface NoData extends GuiComponentServer<Unit> {
        default Unit copyData() {
            return Unit.INSTANCE;
        }

        default boolean needsSync(Unit cachedData) {
            return false;
        }

        @Override
        default void writeInitPacket(RegistryFriendlyByteBuf buf) {}
        default void writePacket(RegistryFriendlyByteBuf buf) {}
    }
}
