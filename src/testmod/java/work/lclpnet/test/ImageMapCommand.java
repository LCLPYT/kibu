package work.lclpnet.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import work.lclpnet.kibu.map.MapColorUtil;
import work.lclpnet.kibu.map.MapUtil;
import work.lclpnet.kibu.map.mixin.MapStateAccessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class ImageMapCommand {

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("kibu:imagemap")
                .requires(s -> s.hasPermissionLevel(2))
                .executes(this::giveMap));
    }

    private int giveMap(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();

        ctx.getSource().sendMessage(Text.literal("Generating image map..."));

        readImage()
                .thenAccept(img -> processImage(player, img))
                .exceptionally(throwable -> {
                    ctx.getSource().sendError(Text.literal("Failed to load image: " + throwable.getMessage()));
                    return null;
                });

        return 1;
    }

    private CompletableFuture<BufferedImage> readImage() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                InputStream in = getClass().getClassLoader().getResourceAsStream("img.png");

                if (in == null) throw new FileNotFoundException();

                try (in) {
                    return ImageIO.read(in);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void processImage(ServerPlayerEntity player, BufferedImage img) {
        ServerWorld world = player.getServerWorld();

        int id = MapUtil.allocateMapId(world, 0, 0, 0, false, false, world.getRegistryKey());
        String name = FilledMapItem.getMapName(id);

        MapState mapState = world.getMapState(name);

        if (mapState == null) throw new IllegalStateException("Map state not found");

        byte[] imgData = MapColorUtil.toBytes(img);
        System.arraycopy(imgData, 0, mapState.colors, 0, Math.min(mapState.colors.length, imgData.length));

        ((MapStateAccessor) mapState).setLocked(true);
        mapState.markDirty();

        ItemStack stack = new ItemStack(Items.FILLED_MAP);
        MapUtil.setMapId(stack, id);

        player.getInventory().setStack(0, stack);
    }
}
