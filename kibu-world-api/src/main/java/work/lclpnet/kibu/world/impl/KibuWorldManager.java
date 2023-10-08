package work.lclpnet.kibu.world.impl;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import work.lclpnet.kibu.world.WorldHandleTracker;
import work.lclpnet.kibu.world.WorldManager;
import work.lclpnet.kibu.world.init.KibuWorldsInit;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

import java.util.*;

@ApiStatus.Internal
public class KibuWorldManager implements WorldManager, WorldHandleTracker {

    private final Map<ServerWorld, RuntimeWorldHandle> worlds = new HashMap<>();
    private final WorldPersistenceService worldPersistenceService;

    public KibuWorldManager(MinecraftServer server) {
        this.worldPersistenceService = new WorldPersistenceService(server, KibuWorldsInit.LOGGER);
    }

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

    @Override
    public Optional<RuntimeWorldHandle> openPersistentWorld(Identifier identifier) {
        return worldPersistenceService.tryRecreateWorld(identifier);
    }

    @Override
    public void registerWorldHandle(RuntimeWorldHandle handle) {
        synchronized (this) {
            worlds.put(handle.asWorld(), handle);
        }
    }

    @Override
    public void unregisterWorld(ServerWorld world) {
        synchronized (this) {
            worlds.remove(world);
        }
    }
}
