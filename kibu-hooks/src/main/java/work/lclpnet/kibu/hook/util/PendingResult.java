package work.lclpnet.kibu.hook.util;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PendingResult<T> {

    private static final PendingResult<?> PASS = new PendingResult<>(null);
    private static final PendingResult<?> EMPTY = new PendingResult<>(null);
    @Nullable
    private final T result;

    private PendingResult(@Nullable T result) {
        this.result = result;
    }

    public Optional<T> get() {
        return Optional.ofNullable(result);
    }

    public boolean isPass() {
        return PASS == this;
    }

    @SuppressWarnings("unchecked")
    public static <T> PendingResult<T> empty() {
        return (PendingResult<T>) EMPTY;
    }

    @SuppressWarnings("unchecked")
    public static <T> PendingResult<T> pass() {
        return (PendingResult<T>) PASS;
    }

    public static <T> PendingResult<T> of(@Nullable T result) {
        if (result == null) {
            return empty();
        }

        return new PendingResult<>(result);
    }
}
