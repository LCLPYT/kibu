package work.lclpnet.kibu.hook.util;

public class Pending<T> {

    private static final Pending<?> PASS = new Pending<>(null);
    private final T value;

    private Pending(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public boolean isPass() {
        return PASS == this;
    }

    @SuppressWarnings("unchecked")
    public static <T> Pending<T> pass() {
        return (Pending<T>) PASS;
    }

    public static <T> Pending<T> of(T value) {
        return new Pending<>(value);
    }
}
