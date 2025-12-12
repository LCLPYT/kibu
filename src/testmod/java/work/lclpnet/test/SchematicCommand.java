package work.lclpnet.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class SchematicCommand {

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(command());
    }

    private LiteralArgumentBuilder<CommandSourceStack> command() {
        return literal("kibu:schematic")
                .requires(s -> s.hasPermission(2))
                .then(literal("sponge.2")
                        .then(argument("name", string())
                                .executes(this::sponge2)))
                .then(literal("structure")
                        .then(argument("name", string())
                                .executes(this::vanilla)));
    }

    private void loadAndPlace(CommandContext<CommandSourceStack> ctx, IOSupplier<BlockStructure> loader, ServerPlayer player) {
        CompletableFuture.supplyAsync(() -> {
                    try {
                        return loader.get();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .thenAccept(structure -> ctx.getSource().getServer().execute(() -> pasteSchematic(player, structure)))
                .exceptionally(throwable -> {
                    ctx.getSource().sendFailure(Component.literal("Failed to load schematic: " + throwable.getMessage()));
                    return null;
                });
    }

    private int sponge2(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String name = StringArgumentType.getString(ctx, "name");
        ServerPlayer player = ctx.getSource().getPlayerOrException();

        loadAndPlace(ctx, read("schematics/" + name, SchematicFormats.SPONGE_V2), player);

        return 1;
    }

    private int vanilla(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String name = StringArgumentType.getString(ctx, "name");
        ServerPlayer player = ctx.getSource().getPlayerOrException();

        loadAndPlace(ctx, read("structures/" + name, VanillaStructureFormat.get(player.level().getServer())), player);

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

    private void pasteSchematic(ServerPlayer player, BlockStructure structure) {
        ServerLevel world = player.level();
        BlockPos pos = player.blockPosition();

        StructureWriter.placeStructure(structure, world, pos, Matrix3i.makeRotationY(1));
    }
}
