package io.github.tr100000.trutils.api.fluid;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;

public final class FluidUtils {
    public static final String KEY_FLUID = "fluid";

    private FluidUtils() {}

    public static Fluid fromJson(JsonObject json) {
        return BuiltInRegistries.FLUID.getOptional(Identifier.parse(GsonHelper.getAsString(json, KEY_FLUID))).orElseThrow();
    }

    public static Fluid fromPacket(FriendlyByteBuf buf) {
        return BuiltInRegistries.FLUID.getOptional(buf.readIdentifier()).orElseThrow();
    }

    public static void writeJson(JsonObject json, Fluid fluid) {
        json.addProperty(KEY_FLUID, BuiltInRegistries.FLUID.getKey(fluid).toString());
    }

    public static void writePacket(FriendlyByteBuf buf, Fluid fluid) {
        buf.writeIdentifier(BuiltInRegistries.FLUID.getKey(fluid));
    }
}
