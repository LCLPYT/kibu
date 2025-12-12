package work.lclpnet.kibu.hook.player;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

public class PlayerConnectionHooks {

    private PlayerConnectionHooks() {}

    public static final Hook<JoinMessage> JOIN_MESSAGE = HookFactory.createArrayBacked(JoinMessage.class, callbacks -> (player, message) -> {
        for (var callback : callbacks) {
            final Component newMessage = callback.onJoin(player, message);
            if (!message.equals(newMessage))
                return newMessage;
        }

        return message;
    });

    public static final Hook<QuitMessage> QUIT_MESSAGE = HookFactory.createArrayBacked(QuitMessage.class, callbacks -> (player, message) -> {
        for (var callback : callbacks) {
            final Component newMessage = callback.onQuit(player, message);
            if (!message.equals(newMessage))
                return newMessage;
        }

        return message;
    });

    public static final Hook<ServerPlayerAction> JOIN = HookFactory.createArrayBacked(ServerPlayerAction.class, callbacks -> (player) -> {
        for (var callback : callbacks) {
            callback.act(player);
        }
    });

    public static final Hook<ServerPlayerAction> QUIT = HookFactory.createArrayBacked(ServerPlayerAction.class, callbacks -> (player) -> {
        for (var callback : callbacks) {
            callback.act(player);
        }
    });

    public interface JoinMessage {
        @Nullable
        Component onJoin(ServerPlayer player, Component message);
    }

    public interface QuitMessage {
        @Nullable
        Component onQuit(ServerPlayer player, Component message);
    }

    public interface ServerPlayerAction {
        void act(ServerPlayer player);
    }
}
