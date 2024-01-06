package work.lclpnet.kibu.hook;

public interface Unregistrable<T> {

    void unregister(T listener);
}
