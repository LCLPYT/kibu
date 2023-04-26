package work.lclpnet.kibu.hook.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerSyncHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.kibu.hook.mixin.ScreenHandlerAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerUtils {

    public static void syncPlayerItems(PlayerEntity player) {
        player.playerScreenHandler.syncState();
    }

    public static void syncPlayerHealthAndHunger(ServerPlayerEntity player) {
        var hungerManager = player.getHungerManager();
        var packet = new HealthUpdateS2CPacket(player.getHealth(), hungerManager.getFoodLevel(), hungerManager.getSaturationLevel());
        player.networkHandler.sendPacket(packet);
    }

    public static void syncCursorStack(ServerPlayerEntity player) {
        ScreenHandler handler = player.currentScreenHandler;
        if (handler == null) return;  // no screen open, ignore

        ScreenHandlerSyncHandler syncHandler = ((ScreenHandlerAccessor) handler).getSyncHandler();
        if (syncHandler == null) return;  // cannot sync, ignore

        syncHandler.updateCursorStack(handler, handler.getCursorStack());
    }

    public static void setCursorStack(ServerPlayerEntity player, @Nullable ItemStack stack) {
        ScreenHandler handler = player.currentScreenHandler;
        if (handler == null) return;  // no screen open, ignore

        handler.setCursorStack(stack != null ? stack : ItemStack.EMPTY);
        syncCursorStack(player);
    }

    @Nonnull
    public static ItemStack getCursorStack(ServerPlayerEntity player) {
        ScreenHandler handler = player.currentScreenHandler;
        if (handler == null) return ItemStack.EMPTY;  // no screen open, thus no cursor item

        return handler.getCursorStack();
    }
}
