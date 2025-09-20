package work.lclpnet.kibu.hook.network;

import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

import java.util.Optional;

public interface CustomClickActionCallback {

    Hook<CustomClickActionCallback> HOOK = HookFactory.createArrayBacked(CustomClickActionCallback.class, hooks -> (player, id, payload) -> {
        for (var hook : hooks) {
            hook.onCustomClickAction(player, id, payload);
        }
    });

    void onCustomClickAction(ServerPlayerEntity player, Identifier id, Optional<NbtElement> payload);
}
