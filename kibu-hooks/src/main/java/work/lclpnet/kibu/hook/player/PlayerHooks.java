package work.lclpnet.kibu.hook.player;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import javax.annotation.Nullable;

public class PlayerHooks {

    public static final Event<Join> JOIN = EventFactory.createArrayBacked(Join.class, callbacks -> (player, message) -> {
        for (Join callback : callbacks) {
            final Text newMessage = callback.onJoin(player, message);
            if (!message.equals(newMessage))
                return newMessage;
        }

        return message;
    });

    public static final Event<Quit> QUIT = EventFactory.createArrayBacked(Quit.class, callbacks -> (player, message) -> {
        for (Quit callback : callbacks) {
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
