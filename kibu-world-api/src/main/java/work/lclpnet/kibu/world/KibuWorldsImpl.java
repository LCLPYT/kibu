package work.lclpnet.kibu.world;

import net.minecraft.server.world.ServerWorld;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

import java.util.*;

public final class KibuWorldsImpl implements KibuWorlds {

    private final Map<ServerWorld, RuntimeWorldHandle> worlds = new HashMap<>();

    private KibuWorldsImpl() {}

    public Set<RuntimeWorldHandle> getRuntimeWorldHandles() {
        synchronized (this) {
            return new HashSet<>(worlds.values());
        }
    }

    @Override
    public Optional<RuntimeWorldHandle> getRuntimeWorldHandle(ServerWorld world) {
        RuntimeWorldHandle handle;

        synchronized (this) {
            handle = worlds.get(world);
        }

        return Optional.ofNullable(handle);
    }

    public void registerWorldHandle(RuntimeWorldHandle handle) {
        synchronized (this) {
            worlds.put(handle.asWorld(), handle);
        }
    }

    public void unregisterWorld(ServerWorld world) {
        synchronized (this) {
            worlds.remove(world);
        }
    }

    static KibuWorldsImpl getInstance() {
        return Holder.INSTANCE;
    }

    // lazy, thread-safe singleton
    private static class Holder {
        private static final KibuWorldsImpl INSTANCE = new KibuWorldsImpl();
    }
}
