package io.github.tr100000.trutils.api.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Utilities for writing to and reading from {@link FriendlyByteBuf} and {@link JsonObject}.
 */
public final class SerializationHelper {
    private SerializationHelper() {}

    public static <T> List<T> readJsonList(JsonObject json, String element, Function<JsonElement, T> reader) {
        if (GsonHelper.isArrayNode(json, element)) {
            return GsonHelper.getAsJsonArray(json, element).asList().stream().map(JsonElement::getAsJsonObject).map(reader).toList();
        }
        else {
            JsonElement jsonElement = json.get(element);
            if (jsonElement != null) {
                List<T> list = new ObjectArrayList<>();
                list.add(reader.apply(jsonElement));
                return list;
            }
            else {
                return new ObjectArrayList<>();
            }
        }
    }

    public static <T, B extends FriendlyByteBuf> List<T> readPacketList(B buf, Function<B, T> reader) {
        int size = buf.readVarInt();
        List<T> list = new ObjectArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(reader.apply(buf));
        }
        return list;
    }

    public static <T, B extends FriendlyByteBuf> void readPacketList(List<T> list, B buf, Function<B, T> reader) {
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++) {
            list.set(i, reader.apply(buf));
        }
    }

    public static <K, V, B extends FriendlyByteBuf> Map<K, V> readPacketMap(B buf, Function<B, K> keyReader, Function<B, V> valueReader) {
        int size = buf.readVarInt();
        Map<K, V> map = HashMap.newHashMap(size);
        for (int i = 0; i < size; i++) {
            map.put(keyReader.apply(buf), valueReader.apply(buf));
        }
        return map;
    }

    public static <T> void writeJsonList(JsonObject json, String element, Collection<T> list, BiConsumer<T, JsonObject> writer) {
        JsonArray array = new JsonArray();
        list.forEach(item -> {
            JsonObject object = new JsonObject();
            writer.accept(item, object);
            array.add(object);
        });
        json.add(element, array);
    }

    public static <T, B extends FriendlyByteBuf> void writePacketList(B buf, Collection<T> list, BiConsumer<T, B> writer) {
        buf.writeVarInt(list.size());
        list.forEach(item -> writer.accept(item, buf));
    }

    public static <T, B extends FriendlyByteBuf> void writePacketList(B buf, Collection<T> list, Consumer<T> writer) {
        buf.writeVarInt(list.size());
        list.forEach(writer);
    }

    public static <K, V, B extends FriendlyByteBuf> void writePacketMap(B buf, Map<K, V> map, BiConsumer<B, Map.Entry<K, V>> writer) {
        buf.writeVarInt(map.size());
        map.entrySet().forEach(entry -> writer.accept(buf, entry));
    }
}
