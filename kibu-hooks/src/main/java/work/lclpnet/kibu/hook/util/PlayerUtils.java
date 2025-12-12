package work.lclpnet.kibu.hook.util;

import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.hook.mixin.access.AbstractContainerMenuAccessor;

public class PlayerUtils {

    public static void syncPlayerItems(Player player) {
        player.inventoryMenu.sendAllDataToRemote();
    }

    public static void syncPlayerHealthAndHunger(ServerPlayer player) {
        var hungerManager = player.getFoodData();
        var packet = new ClientboundSetHealthPacket(player.getHealth(), hungerManager.getFoodLevel(), hungerManager.getSaturationLevel());
        player.connection.send(packet);
    }

    public static void syncCursorStack(ServerPlayer player) {
        AbstractContainerMenu handler = player.containerMenu;
        if (handler == null) return;  // no screen open, ignore

        ContainerSynchronizer syncHandler = ((AbstractContainerMenuAccessor) handler).getSynchronizer();
        if (syncHandler == null) return;  // cannot sync, ignore

        syncHandler.sendCarriedChange(handler, handler.getCarried());
    }

    public static void setCursorStack(ServerPlayer player, @Nullable ItemStack stack) {
        AbstractContainerMenu handler = player.containerMenu;
        if (handler == null) return;  // no screen open, ignore

        handler.setCarried(stack != null ? stack : ItemStack.EMPTY);
        syncCursorStack(player);
    }

    @NotNull
    public static ItemStack getCursorStack(ServerPlayer player) {
        AbstractContainerMenu handler = player.containerMenu;
        if (handler == null) return ItemStack.EMPTY;  // no screen open, thus no cursor item

        return handler.getCarried();
    }
}
