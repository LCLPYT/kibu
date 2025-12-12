package work.lclpnet.kibu.title.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import work.lclpnet.kibu.title.Title;
import work.lclpnet.kibu.title.TitleAccess;
import work.lclpnet.kibu.title.impl.ServerPlayerTitle;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements TitleAccess {

    @SuppressWarnings("DataFlowIssue")
    @Unique
    private final ServerPlayerTitle title = new ServerPlayerTitle((ServerPlayer) (Object) this);

    @Override
    public Title kibu$getTitle() {
        return title;
    }
}
