package work.lclpnet.kibu.hook;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;

public class ServerMessageHooks {

    public static final Hook<ServerMessageEvents.AllowChatMessage> ALLOW_CHAT_MESSAGE = HookFactory.createArrayBacked(ServerMessageEvents.AllowChatMessage.class, callbacks -> (message, sender, params) -> {
        for (var cb : callbacks) {
            if (!cb.allowChatMessage(message, sender, params)) {
                return false;
            }
        }

        return true;
    });

    public static final Hook<ServerMessageEvents.AllowGameMessage> ALLOW_GAME_MESSAGE = HookFactory.createArrayBacked(ServerMessageEvents.AllowGameMessage.class, callbacks -> (server, message, overlay) -> {
        for (var cb : callbacks) {
            if (!cb.allowGameMessage(server, message, overlay)) {
                return false;
            }
        }

        return true;
    });

    public static final Hook<ServerMessageEvents.AllowCommandMessage> ALLOW_COMMAND_MESSAGE = HookFactory.createArrayBacked(ServerMessageEvents.AllowCommandMessage.class, callbacks -> (message, source, params) -> {
        for (var cb : callbacks) {
            if (!cb.allowCommandMessage(message, source, params)) {
                return false;
            }
        }

        return true;
    });

    public static final Hook<ServerMessageEvents.ChatMessage> CHAT_MESSAGE = HookFactory.createArrayBacked(ServerMessageEvents.ChatMessage.class, callbacks -> (message, sender, params) -> {
        for (var cb : callbacks) {
            cb.onChatMessage(message, sender, params);
        }
    });

    public static final Hook<ServerMessageEvents.GameMessage> GAME_MESSAGE = HookFactory.createArrayBacked(ServerMessageEvents.GameMessage.class, callbacks -> (server, message, overlay) -> {
        for (var cb : callbacks) {
            cb.onGameMessage(server, message, overlay);
        }
    });

    public static final Hook<ServerMessageEvents.CommandMessage> COMMAND_MESSAGE = HookFactory.createArrayBacked(ServerMessageEvents.CommandMessage.class, callbacks -> (message, source, params) -> {
        for (var cb : callbacks) {
            cb.onCommandMessage(message, source, params);
        }
    });

    private ServerMessageHooks() {}

    static {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ALLOW_CHAT_MESSAGE.invoker());
        ServerMessageEvents.ALLOW_GAME_MESSAGE.register(ALLOW_GAME_MESSAGE.invoker());
        ServerMessageEvents.ALLOW_COMMAND_MESSAGE.register(ALLOW_COMMAND_MESSAGE.invoker());
        ServerMessageEvents.CHAT_MESSAGE.register(CHAT_MESSAGE.invoker());
        ServerMessageEvents.GAME_MESSAGE.register(GAME_MESSAGE.invoker());
        ServerMessageEvents.COMMAND_MESSAGE.register(COMMAND_MESSAGE.invoker());
    }
}
