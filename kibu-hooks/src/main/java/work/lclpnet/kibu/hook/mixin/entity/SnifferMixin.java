package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.util.MixinUtils;

@Mixin(Sniffer.class)
public class SnifferMixin {

    @WrapOperation(
            method = "lambda$dropSeed$0",  // this is a lambda in dropSeeds(); the name might change in the future
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    public boolean kibu$onDropSeeds(ServerLevel world, Entity entity, Operation<Boolean> original) {
        return MixinUtils.wrapEntityItemDrop(world, entity, original, this);
    }

    @WrapOperation(
            method = "spawnChildFromBreeding",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    public boolean kibu$onDropItemBreed(ServerLevel world, Entity entity, Operation<Boolean> original) {
        return MixinUtils.wrapEntityItemDrop(world, entity, original, this);
    }
}
