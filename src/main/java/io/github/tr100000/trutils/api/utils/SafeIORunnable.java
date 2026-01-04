package io.github.tr100000.trutils.api.utils;

import org.apache.commons.io.function.IORunnable;

import java.io.IOException;

/**
 * Like {@link IORunnable} but with a {@link #safeRun()} method in a try-catch block
 */
@FunctionalInterface
public interface SafeIORunnable extends IORunnable {
    /**
     * Like {@link IORunnable#run()}, but in a try-catch block
     */
    default void safeRun() {
        try {
            run();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
