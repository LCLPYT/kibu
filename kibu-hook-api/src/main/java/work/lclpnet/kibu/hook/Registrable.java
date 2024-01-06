package work.lclpnet.kibu.hook;

public interface Registrable<T> {

    void register(T listener);
}
