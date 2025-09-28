package work.lclpnet.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.io.function.IOSupplier;
import work.lclpnet.kibu.schematic.FabricBlockStateAdapter;
import work.lclpnet.kibu.schematic.SchematicFormats;
import work.lclpnet.kibu.schematic.api.SchematicFormat;
import work.lclpnet.kibu.schematic.vanilla.VanillaStructureFormat;
import work.lclpnet.kibu.structure.BlockStructure;
import work.lclpnet.kibu.util.StructureWriter;
import work.lclpnet.kibu.util.math.Matrix3i;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SchematicCommand {

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(command());
    }

    private LiteralArgumentBuilder<ServerCommandSource> command() {
        return literal("kibu:schematic")
                .requires(s -> s.hasPermissionLevel(2))
                .then(literal("sponge.2")
                        .then(argument("name", string())
                                .executes(this::sponge2)))
                .then(literal("structure")
                        .then(argument("name", string())
                                .executes(this::vanilla)));
    }

    private void loadAndPlace(CommandContext<ServerCommandSource> ctx, IOSupplier<BlockStructure> loader, ServerPlayerEntity player) {
        CompletableFuture.supplyAsync(() -> {
                    try {
                        return loader.get();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .thenAccept(structure -> ctx.getSource().getServer().execute(() -> pasteSchematic(player, structure)))
                .exceptionally(throwable -> {
                    ctx.getSource().sendError(Text.literal("Failed to load schematic: " + throwable.getMessage()));
                    return null;
                });
    }

    private int sponge2(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        String name = StringArgumentType.getString(ctx, "name");
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();

        loadAndPlace(ctx, read("schematics/" + name, SchematicFormats.SPONGE_V2), player);

        return 1;
    }

    private int vanilla(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        String name = StringArgumentType.getString(ctx, "name");
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();

        loadAndPlace(ctx, read("structures/" + name, VanillaStructureFormat.get(player.getEntityWorld().getServer())), player);

        return 1;
    }

    private IOSupplier<BlockStructure> read(String name, SchematicFormat format) {
        return () -> {
            InputStream in = getClass().getClassLoader().getResourceAsStream(name);

            if (in == null) throw new FileNotFoundException();

            var adapter = FabricBlockStateAdapter.getInstance();

            try (in) {
                return format.reader().read(in, adapter);
            }
        };
    }

    private void pasteSchematic(ServerPlayerEntity player, BlockStructure structure) {
        ServerWorld world = player.getEntityWorld();
        BlockPos pos = player.getBlockPos();

        StructureWriter.placeStructure(structure, world, pos, Matrix3i.makeRotationY(1));
    }
}
