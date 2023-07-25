package work.lclpnet.kibu.translate.util;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class WeakList<T> implements Iterable<T> {

    protected final List<WeakReference<T>> collection = new ArrayList<>();

    public void add(T obj) {
        Objects.requireNonNull(obj);

        collection.add(new WeakReference<>(obj));
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        var parent = collection.iterator();

        return new Iterator<>() {

            private T next = null;

            @Override
            public boolean hasNext() {
                advance();
                return next != null;
            }

            @Override
            public T next() {
                T ret = next;
                next = null;
                return ret;
            }

            private void advance() {
                while (next == null && parent.hasNext()) {
                    next = parent.next().get();
                    if (next == null) parent.remove();
                }
            }
        };
    }
}
