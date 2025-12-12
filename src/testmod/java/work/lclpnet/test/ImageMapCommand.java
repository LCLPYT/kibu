package work.lclpnet.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import work.lclpnet.kibu.map.MapColorUtil;
import work.lclpnet.kibu.map.MapUtil;
import work.lclpnet.kibu.map.mixin.MapItemSavedDataAccessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class ImageMapCommand {

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("kibu:imagemap")
                .requires(s -> s.hasPermission(2))
                .then(Commands.argument("name", StringArgumentType.string())
                        .executes(this::giveMap)));
    }

    private int giveMap(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String name = StringArgumentType.getString(ctx, "name");
        ServerPlayer player = ctx.getSource().getPlayerOrException();

        ctx.getSource().sendSystemMessage(Component.literal("Generating image map..."));

        readImage(name)
                .thenAccept(img -> processImage(player, img))
                .exceptionally(throwable -> {
                    ctx.getSource().sendFailure(Component.literal("Failed to load image: " + throwable.getMessage()));
                    return null;
                });

        return 1;
    }

    private CompletableFuture<BufferedImage> readImage(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                InputStream in = getClass().getClassLoader().getResourceAsStream(name);

                if (in == null) throw new FileNotFoundException();

                try (in) {
                    return ImageIO.read(in);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void processImage(ServerPlayer player, BufferedImage img) {
        ServerLevel world = player.level();

        MapId id = MapUtil.allocateMapId(world, 0, 0, 0, false, false, world.dimension());
        MapItemSavedData mapState = world.getMapData(id);

        if (mapState == null) throw new IllegalStateException("Map state not found");

        byte[] imgData = MapColorUtil.toBytes(img);
        System.arraycopy(imgData, 0, mapState.colors, 0, Math.min(mapState.colors.length, imgData.length));

        ((MapItemSavedDataAccessor) mapState).setLocked(true);
        mapState.setDirty();

        ItemStack stack = new ItemStack(Items.FILLED_MAP);
        stack.set(DataComponents.MAP_ID, id);

        player.getInventory().setItem(0, stack);
    }
}
