package work.lclpnet.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import work.lclpnet.kibu.schematic.FabricBlockStateAdapter;
import work.lclpnet.kibu.schematic.SchematicFormats;
import work.lclpnet.kibu.structure.BlockStructure;
import work.lclpnet.kibu.util.StructureWriter;
import work.lclpnet.kibu.util.math.Matrix3i;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class SchematicCommand {

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(command());
    }

    private LiteralArgumentBuilder<ServerCommandSource> command() {
        return CommandManager.literal("kibu:schematic")
                .requires(s -> s.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .executes(this::loadAndPasteSchematic));
    }

    private int loadAndPasteSchematic(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        String name = StringArgumentType.getString(ctx, "name");
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();

        readStructure(name)
                .thenAccept(structure -> ctx.getSource().getServer().submit(() -> pasteSchematic(player, structure)))
                .exceptionally(throwable -> {
                    ctx.getSource().sendError(Text.literal("Failed to load schematic: " + throwable.getMessage()));
                    return null;
                });

        return 1;
    }

    private CompletableFuture<BlockStructure> readStructure(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                InputStream in = getClass().getClassLoader().getResourceAsStream(name);

                if (in == null) throw new FileNotFoundException();

                var adapter = FabricBlockStateAdapter.getInstance();

                try (in) {
                    return SchematicFormats.SPONGE_V2.reader().read(in, adapter);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void pasteSchematic(ServerPlayerEntity player, BlockStructure structure) {
        ServerWorld world = player.getServerWorld();
        BlockPos pos = player.getBlockPos();

        StructureWriter.placeStructure(structure, world, pos, Matrix3i.makeRotationY(1));
    }
}
