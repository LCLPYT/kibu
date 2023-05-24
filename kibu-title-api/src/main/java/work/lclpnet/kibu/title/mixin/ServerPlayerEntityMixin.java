package work.lclpnet.kibu.title.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import work.lclpnet.kibu.title.Title;
import work.lclpnet.kibu.title.TitleAccess;
import work.lclpnet.kibu.title.impl.ServerPlayerTitle;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements TitleAccess {

    @SuppressWarnings("DataFlowIssue")
    @Unique
    private final ServerPlayerTitle title = new ServerPlayerTitle((ServerPlayerEntity) (Object)  this);

    @Override
    public Title kibu$getTitle() {
        return title;
    }
}
