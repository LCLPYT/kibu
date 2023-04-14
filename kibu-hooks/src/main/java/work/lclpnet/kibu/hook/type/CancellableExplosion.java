package work.lclpnet.kibu.hook.type;

public interface CancellableExplosion {

    void kibu$setCancelled(boolean cancel);

    boolean kibu$isCancelled();
}
