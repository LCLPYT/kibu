package work.lclpnet.kibu.hook.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import work.lclpnet.kibu.hook.model.BlockModification;
import work.lclpnet.kibu.hook.model.BlockModificationType;

public class BlockModificationHooks {

    public static final Event<PlayerModifyBlock> MODIFY_BLOCK = EventFactory.createArrayBacked(PlayerModifyBlock.class, callbacks -> data -> {
        for (PlayerModifyBlock callback : callbacks)
            if (callback.onModify(data))
                return true;

        return false;
    });

    public interface PlayerModifyBlock {
        boolean onModify(BlockModification modification);
    }

    static {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            BlockModification data = new BlockModification(BlockModificationType.BREAK_BLOCK, world, pos, player);
            // propagate, when not cancelled
            return !BlockModificationHooks.MODIFY_BLOCK.invoker().onModify(data);
        });
    }
}
