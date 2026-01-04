package io.github.tr100000.trutils.api.fluid;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;

public final class FluidVariantUtils {
    public static final String KEY_VARIANT = "variant";
    public static final String KEY_ID = "id";
    public static final String KEY_COMPONENTS = "components";

    public static final Codec<FluidVariant> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf(KEY_ID).forGetter(FluidVariant::getFluid),
            DataComponentPatch.CODEC.optionalFieldOf(KEY_COMPONENTS, DataComponentPatch.EMPTY).forGetter(FluidVariant::getComponents)
        ).apply(instance, (fluid, nbt) -> nbt.isEmpty() ? FluidVariant.of(fluid) : FluidVariant.of(fluid, nbt)));

    private FluidVariantUtils() {}

    public static FluidVariant fromJson(JsonObject json) {
        if (json.has(KEY_VARIANT)) {
            JsonObject variant = json.get(KEY_VARIANT).getAsJsonObject();
            Fluid fluid = BuiltInRegistries.FLUID.getOptional(Identifier.parse(GsonHelper.getAsString(variant, KEY_ID))).orElseThrow();
            try {
                DataComponentPatch components = DataComponentPatch.CODEC.decode(JsonOps.INSTANCE, variant).getOrThrow(JsonParseException::new).getFirst();
                return FluidVariant.of(fluid, components);
            }
            catch (Exception e) {
                return FluidVariant.of(fluid);
            }
        }
        else {
            return FluidVariant.blank();
        }
    }

    public static FluidVariant fromPacket(FriendlyByteBuf buf) {
        return buf.readLenientJsonWithCodec(FluidVariant.CODEC);
    }

    public static void writeJson(JsonObject json, FluidVariant fluid) {
        if (!fluid.isBlank()) {
            JsonObject object = new JsonObject();
            object.addProperty(KEY_ID, BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString());
            object.addProperty(KEY_COMPONENTS, fluid.getComponents().toString());
            json.add(KEY_VARIANT, object);
        }
    }

    public static void writePacket(FriendlyByteBuf buf, FluidVariant fluid) {
        buf.writeJsonWithCodec(FluidVariant.CODEC, fluid);
    }
}
