package work.lclpnet.kibu.hook.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.world.WorldUnreadyCallback;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;getNetworkIo()Lnet/minecraft/server/ServerNetworkIo;",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            ),
            method = "shutdown"
    )
    private void mplugins$beforeGetNetworkIo(CallbackInfo ci) {
        WorldUnreadyCallback.HOOK.invoker().onWorldUnready();
    }
}
