package io.github.tr100000.trutils.api.datagen;

import net.fabricmc.loader.api.ModContainer;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jspecify.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class GeneratedPackResources extends PathPackResources {
    private final ModContainer mod;
    private final PackType type;

    public GeneratedPackResources(ModContainer mod, PackType type) {
        super(RuntimeDatagen.getPackInfo(mod, type), RuntimeDatagen.getPath(mod));
        this.mod = mod;
        this.type = type;
    }

    public ModContainer getMod() {
        return mod;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String... segments) {
        if (segments.length == 1) {
            if (segments[0].equals("pack.mcmeta")) {
                return createPackMetadata();
            }
            else if (segments[0].equals("pack.png")) {
                return createIcon();
            }
        }
        return super.getRootResource(segments);
    }

    private IoSupplier<InputStream> createPackMetadata() {
        return () -> new ByteArrayInputStream("""
                {
                    "pack": {
                            "description": "%s",
                            "pack_format": "%s"
                    }
                }
                """.formatted(Component.translatable("pack.generated." + type.getDirectory()), SharedConstants.getCurrentVersion().packVersion(type)).getBytes());
    }

    private @Nullable IoSupplier<InputStream> createIcon() {
        Optional<Path> path = mod.findPath(mod.getMetadata().getIconPath(512).orElse("icon.png"));
        return path.isPresent() && Files.exists(path.get()) ? IoSupplier.create(path.get()) : null;
    }
}
