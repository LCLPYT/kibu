package work.lclpnet.kibu.world.init;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import work.lclpnet.kibu.world.KibuWorlds;
import work.lclpnet.kibu.world.WorldHandleTracker;
import work.lclpnet.kibu.world.WorldManager;
import xyz.nucleoid.fantasy.RuntimeWorld;

public class SafeKibuWorldsInit {

    /**
     * Called when dynamic dependencies are verified.
     */
    public void initSafe() {
        ServerWorldEvents.UNLOAD.register((server, world) -> {
            if (!(world instanceof RuntimeWorld runtimeWorld)) return;

            WorldManager worldManager = KibuWorlds.getInstance().getWorldManager(server);

            if (worldManager instanceof WorldHandleTracker tracker) {
                tracker.unregisterWorld(runtimeWorld);
            }
        });

//        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
//            dispatcher.register(CommandManager.literal("world")
//                    .requires(s -> s.hasPermissionLevel(4))
//                    .executes(this::testWorld));
//        });
    }

//    private int testWorld(CommandContext<ServerCommandSource> ctx) {
//        ServerCommandSource source = ctx.getSource();
//        MinecraftServer server = source.getServer();
//
//        server.execute(() -> {
//            WorldManager worldManager = KibuWorlds.getInstance().getWorldManager(server);
//
//            worldManager.openPersistentWorld(new Identifier("kibu", "test2"))
//                    .ifPresent(handle -> {
//                        System.out.println("Successfully opened persisted world " + handle.getRegistryKey().getValue());
//
//                        ServerPlayerEntity player = source.getPlayer();
//                        if (player != null) {
//                            ServerWorld world = handle.asWorld();
//                            BlockPos pos = world.getSpawnPos();
//
//                            player.teleport(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, Set.of(), 0, 0);
//                        }
//                    });
//        });
//
//        return 1;
//    }
}
