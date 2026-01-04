package io.github.tr100000.trutils.api.datagen;

import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class GeneratedPackResourcesPair {
    private @Nullable GeneratedPackResources clientPack;
    private @Nullable GeneratedPackResources serverPack;

    public GeneratedPackResourcesPair(@Nullable GeneratedPackResources clientPack, @Nullable GeneratedPackResources serverPack) {
        this.clientPack = clientPack;
        this.serverPack = serverPack;
    }

    public GeneratedPackResourcesPair() {}

    public static GeneratedPackResourcesPair create(ModContainer mod, RuntimeDatagenEntrypoint entrypoint) {
        GeneratedPackResourcesPair pair = new GeneratedPackResourcesPair();
        if (entrypoint.generatedTypes().client && RuntimeDatagen.needsClientPack()) {
            pair.setClientPack(RuntimeDatagen.createPack(mod, PackType.CLIENT_RESOURCES));
        }
        if (entrypoint.generatedTypes().server) {
            pair.setServerPack(RuntimeDatagen.createPack(mod, PackType.SERVER_DATA));
        }
        return pair;
    }

    public boolean hasClientPack() {
        return clientPack != null;
    }

    public boolean hasServerPack() {
        return serverPack != null;
    }

    public boolean hasPack(PackType type) {
        return switch (type) {
            case CLIENT_RESOURCES -> hasClientPack();
            case SERVER_DATA -> hasServerPack();
        };
    }

    public @Nullable PackResources getClientPack() {
        return clientPack;
    }

    public @Nullable PackResources getServerPack() {
        return serverPack;
    }

    public @Nullable GeneratedPackResources getPack(PackType type) {
        return switch (type) {
            case CLIENT_RESOURCES -> clientPack;
            case SERVER_DATA -> serverPack;
        };
    }

    public GeneratedPackResourcesPair setClientPack(@Nullable GeneratedPackResources clientPack) {
        this.clientPack = clientPack;
        return this;
    }

    public GeneratedPackResourcesPair setServerPack(@Nullable GeneratedPackResources serverPack) {
        this.serverPack = serverPack;
        return this;
    }

    public GeneratedPackResourcesPair setPack(@Nullable GeneratedPackResources pack, PackType type) {
        return switch (type) {
            case CLIENT_RESOURCES -> setClientPack(pack);
            case SERVER_DATA -> setServerPack(pack);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GeneratedPackResourcesPair other)) return false;
        return Objects.equals(getClientPack(), other.getClientPack()) && Objects.equals(getServerPack(), other.getServerPack());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClientPack(), getServerPack());
    }
}
