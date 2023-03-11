package work.lclpnet.kibu.hook.model;

public interface CancellableExplosion {

    void setCancelled(boolean cancel);

    boolean isCancelled();
}
