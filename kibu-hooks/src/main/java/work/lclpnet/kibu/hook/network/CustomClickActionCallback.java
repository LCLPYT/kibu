package work.lclpnet.kibu.hook.network;

import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

import java.util.Optional;

public interface CustomClickActionCallback {

    Hook<CustomClickActionCallback> HOOK = HookFactory.createArrayBacked(CustomClickActionCallback.class, hooks -> (player, id, payload) -> {
        for (var hook : hooks) {
            hook.onCustomClickAction(player, id, payload);
        }
    });

    void onCustomClickAction(ServerPlayer player, ResourceLocation id, Optional<Tag> payload);
}
