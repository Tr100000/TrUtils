package io.github.tr100000.trutils.api.utils;

import com.google.common.collect.BiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.function.Function;

public final class CodecUtils {
    private CodecUtils() {}

    public static <K, V> Codec<V> ofMap(BiMap<K, V> map, Codec<K> keyCodec, Function<K, String> errorMessage) {
        return keyCodec.flatXmap(
                key -> map.containsKey(key) ? DataResult.success(map.get(key)) : DataResult.error(() -> errorMessage.apply(key)),
                value -> DataResult.success(map.inverse().get(value))
        );
    }
}
