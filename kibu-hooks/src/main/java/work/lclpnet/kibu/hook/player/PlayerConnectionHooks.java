package work.lclpnet.kibu.hook.player;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

import javax.annotation.Nullable;

public class PlayerConnectionHooks {

    private PlayerConnectionHooks() {}

    public static final Hook<JoinMessage> JOIN_MESSAGE = HookFactory.createArrayBacked(JoinMessage.class, callbacks -> (player, message) -> {
        for (var callback : callbacks) {
            final Text newMessage = callback.onJoin(player, message);
            if (!message.equals(newMessage))
                return newMessage;
        }

        return message;
    });

    public static final Hook<QuitMessage> QUIT_MESSAGE = HookFactory.createArrayBacked(QuitMessage.class, callbacks -> (player, message) -> {
        for (var callback : callbacks) {
            final Text newMessage = callback.onQuit(player, message);
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
        Text onJoin(ServerPlayerEntity player, Text message);
    }

    public interface QuitMessage {
        @Nullable
        Text onQuit(ServerPlayerEntity player, Text message);
    }

    public interface ServerPlayerAction {
        void act(ServerPlayerEntity player);
    }
}
