package work.lclpnet.kibu.hook.player;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

import javax.annotation.Nullable;

public class PlayerConnectionHooks {

    private PlayerConnectionHooks() {}

    public static final Hook<Join> JOIN_MESSAGE = HookFactory.createArrayBacked(Join.class, callbacks -> (player, message) -> {
        for (var callback : callbacks) {
            final Text newMessage = callback.onJoin(player, message);
            if (!message.equals(newMessage))
                return newMessage;
        }

        return message;
    });

    public static final Hook<Quit> QUIT_MESSAGE = HookFactory.createArrayBacked(Quit.class, callbacks -> (player, message) -> {
        for (var callback : callbacks) {
            final Text newMessage = callback.onQuit(player, message);
            if (!message.equals(newMessage))
                return newMessage;
        }

        return message;
    });

    public interface Join {
        @Nullable
        Text onJoin(ServerPlayerEntity player, Text message);
    }

    public interface Quit {
        @Nullable
        Text onQuit(ServerPlayerEntity player, Text message);
    }
}
