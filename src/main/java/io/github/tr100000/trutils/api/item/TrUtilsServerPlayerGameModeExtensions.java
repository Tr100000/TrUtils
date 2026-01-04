package io.github.tr100000.trutils.api.item;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface TrUtilsServerPlayerGameModeExtensions {
    default void trutils_setIsMining(boolean isMining) {
        throw new AssertionError();
    }
}
