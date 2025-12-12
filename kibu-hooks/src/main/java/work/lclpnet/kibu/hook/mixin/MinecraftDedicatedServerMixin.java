package work.lclpnet.kibu.hook.mixin;

import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.world.ServerWorldReadyCallback;

@Mixin(DedicatedServer.class)
public class MinecraftDedicatedServerMixin {

	@Inject(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/dedicated/DedicatedServer;loadLevel()V",
					shift = At.Shift.AFTER
			),
			method = "initServer"
	)
	private void kibu$afterWorldLoad(CallbackInfoReturnable<Boolean> cir) {
		DedicatedServer self = (DedicatedServer) (Object) this;
		ServerWorldReadyCallback.HOOK.invoker().onWorldReady(self);
	}
}