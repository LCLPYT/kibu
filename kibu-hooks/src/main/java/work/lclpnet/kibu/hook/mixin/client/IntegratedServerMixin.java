package work.lclpnet.kibu.hook.mixin.client;

import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.world.ServerWorldReadyCallback;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {

	@Inject(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/integrated/IntegratedServer;loadWorld()V",
					shift = At.Shift.AFTER
			),
			method = "setupServer"
	)
	private void kibu$afterWorldLoad(CallbackInfoReturnable<Boolean> cir) {
		IntegratedServer self = (IntegratedServer) (Object) this;
		ServerWorldReadyCallback.HOOK.invoker().onWorldReady(self);
	}
}