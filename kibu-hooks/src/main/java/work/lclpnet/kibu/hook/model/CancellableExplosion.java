package work.lclpnet.kibu.hook.model;

public interface CancellableExplosion {

    void kibu$setCancelled(boolean cancel);

    boolean kibu$isCancelled();
}
