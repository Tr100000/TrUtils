package io.github.tr100000.trutils.api.utils;

import net.fabricmc.fabric.api.tag.convention.v2.TagUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.Contract;

/**
 * Utilities specific to Minecraft.
 */
public final class GameUtils {
    private GameUtils() {}

    /**
     * Get the mod name from an {@link Identifier}
     * @param id the id to get the mod name for
     * @return the mod name
     * @see #getModName(String)
     */
    public static String getModName(Identifier id) {
        return getModName(id.getNamespace());
    }

    /**
     * Get the mod name of the mod id
     * @param modid the mod id to get the name for
     * @return the mod name, or {@code modid} if there is no mod with the specified id
     * @see #getModName(Identifier)
     */
    public static String getModName(String modid) {
        if (modid.equals("c")) {
            return "Common";
        }
        else {
            return FabricLoader.getInstance().getModContainer(modid).map(mod -> mod.getMetadata().getName()).orElse(modid);
        }
    }

    /**
     * Returns an {@link Identifier} with the fabric namespace and the specified path.
     * @see #commonId(String)
     */
    @Contract("_ -> new")
    public static Identifier fabricId(String path) {
        return Identifier.fromNamespaceAndPath(TagUtil.FABRIC_TAG_NAMESPACE, path);
    }

    /**
     * Returns an {@link Identifier} with the common namespace and the specified path
     * @see #fabricId(String)
     */
    @Contract("_ -> new")
    public static Identifier commonId(String path) {
        return Identifier.fromNamespaceAndPath(TagUtil.C_TAG_NAMESPACE, path);
    }

    /**
     * A helper function to register an {@link CreativeModeTab}
     * @param tab the {@link CreativeModeTab} to register
     * @param id the {@link Identifier} for the item tab
     * @return a {@link ResourceKey} for the newly registered tab
     */
    @Contract("_, _ -> new")
    public static ResourceKey<CreativeModeTab> registerItemGroup(CreativeModeTab tab, Identifier id) {
        ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
        return key;
    }
}
