package io.github.tr100000.trutils.api.utils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Somewhere inbetween a JS Promise and an {@link java.util.Optional}, but worse in every way
 * @param <T> The type of the value to be hopefully held
 * @see java.util.Optional
 */
public class Hopefully<T> {
    private final List<Consumer<T>> consumers;
    private @Nullable T value;

    /**
     * Creates a new empty {@link Hopefully<T>}.
     */
    @Contract("-> new")
    public static <T> Hopefully<T> empty() {
        return new Hopefully<>();
    }

    /**
     * Creates a new {@link Hopefully<T>} with the given value.
     */
    @Contract("_ -> new")
    public static <T> Hopefully<T> ofValue(@Nullable T value) {
        return new Hopefully<>(value);
    }

    private Hopefully() {
        this.consumers = new ObjectArrayList<>();
    }

    private Hopefully(@Nullable T value) {
        this();
        this.value = value;
    }

    /**
     * @return an {@link Optional<T>} that contains the value
     */
    public Optional<T> getOrEmpty() {
        return Optional.ofNullable(value);
    }

    /**
     * @return the value, even if it's null
     */
    public @Nullable T getOrNull() {
        return value;
    }

    /**
     * Wishful thinking
     * @return the value
     * @throws NullPointerException if the held value is null
     */
    public T getOrThrow() {
        return Objects.requireNonNull(value, "value is null");
    }

    /**
     * If a value is present, performs the given action with the value.
     * Otherwise waits to be fulfilled, then performs the action.
     * @param consumer the action to be performed if or when the value is present
     * @see #then(Function)
     */
    public void whenReady(Consumer<T> consumer) {
        Objects.requireNonNull(consumer, "consumer is null");
        if (value == null) {
            consumers.add(consumer);
        }
        else {
            consumer.accept(value);
        }
    }

    /**
     * Returns a new {@link Hopefully<U>} that will hold the result of applying the given mapping function to the held value.
     * @param function the mapping function that will be performed on the held value
     * @return a new {@link Hopefully<U>} that will hold the new result
     * @param <U> the type of the value in the new {@link Hopefully<U>}
     * @see #whenReady(Consumer)
     */
    public <U> Hopefully<U> then(Function<T, U> function) {
        Objects.requireNonNull(function, "function is null");
        Hopefully<U> hopefully = empty();
        whenReady(v -> hopefully.fulfill(function.apply(v)));
        return hopefully;
    }

    /**
     * Fulfills with the given value.
     * @param value the value
     * @throws NullPointerException if the given value is {@code null}
     * @throws IllegalStateException if the {@link Hopefully<T>} has already been fulfilled
     */
    public void fulfill(T value) {
        Objects.requireNonNull(value, "value is null");
        if (this.value != null) {
            throw new IllegalStateException("This Hopefully already has a value!");
        }
        this.value = value;
        consumers.forEach(consumer -> consumer.accept(value));
        consumers.clear();
    }
}
