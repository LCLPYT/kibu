package work.lclpnet.kibu.world;

import net.minecraft.server.world.ServerWorld;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

import java.util.Optional;
import java.util.Set;

public interface KibuWorlds {

    Set<RuntimeWorldHandle> getRuntimeWorldHandles();

    Optional<RuntimeWorldHandle> getRuntimeWorldHandle(ServerWorld world);

    static KibuWorlds getInstance() {
        return KibuWorldsImpl.getInstance();
    }
}
