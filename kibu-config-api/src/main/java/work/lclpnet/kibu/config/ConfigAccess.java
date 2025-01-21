package work.lclpnet.kibu.config;

import org.jetbrains.annotations.NotNull;

public interface ConfigAccess<C> {

    @NotNull C config();

    void save();
}
