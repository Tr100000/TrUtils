package io.github.tr100000.trutils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.tr100000.trutils.api.recipe.BlankRecipe;
import io.github.tr100000.trutils.command.ModCommands;
import io.github.tr100000.trutils.networking.TrUtilsNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class TrUtils implements ModInitializer {
    public static final String MODID = "trutils";
    public static final Logger LOGGER = LoggerFactory.getLogger("TrUtils");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static final Path RUNTIME_DATAGEN_PATH = FabricLoader.getInstance().getGameDir().resolve(".runtime-datagen");
    public static final Path DATA_CACHE_PATH = FabricLoader.getInstance().getGameDir().resolve(".cache");

    private static @Nullable MinecraftServer server;

    @Override
    public void onInitialize() {
        BlankRecipe.Type.register();

        CommandRegistrationCallback.EVENT.register(ModCommands::register);
        ServerLifecycleEvents.SERVER_STARTED.register(startedServer -> server = startedServer);
        ServerLifecycleEvents.SERVER_STOPPED.register(stoppedServer -> server = null);

        TrUtilsNetworking.registerPayloads();
    }

    public static @Nullable MinecraftServer getCurrentServer() {
        return server;
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
