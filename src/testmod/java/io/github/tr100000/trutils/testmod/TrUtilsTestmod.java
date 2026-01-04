package io.github.tr100000.trutils.testmod;

import io.github.tr100000.trutils.testmod.registry.TestmodItems;
import net.fabricmc.api.ModInitializer;

public class TrUtilsTestmod implements ModInitializer {
    public static final String MODID = "trutils-testmod";

    @Override
    public void onInitialize() {
        TestmodItems.REGISTRY.register();
    }
}
