package io.github.tr100000.trutils.api.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface RuntimeDatagenEntrypoint extends DataGeneratorEntrypoint {
    @Override
    void onInitializeDataGenerator(FabricDataGenerator generator);

    default boolean strictValidation() {
        return true;
    }

    default ResourceTypes generatedTypes() {
        return ResourceTypes.BOTH;
    }

    default @Nullable Identifier getId() {
        return null;
    }

    enum ResourceTypes {
        CLIENT(true, false),
        SERVER(false, true),
        BOTH(true, true);

        public final boolean client;
        public final boolean server;

        ResourceTypes(boolean client, boolean server) {
            this.client = client;
            this.server = server;
        }

        public boolean matches(PackType resourceType) {
            return switch (resourceType) {
                case CLIENT_RESOURCES -> client;
                case SERVER_DATA -> server;
            };
        }
    }
}
