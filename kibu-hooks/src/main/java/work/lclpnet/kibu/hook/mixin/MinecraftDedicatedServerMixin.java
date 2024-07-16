package work.lclpnet.kibu.hook.mixin;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.world.WorldReadyCallback;

@Mixin(MinecraftDedicatedServer.class)
public class MinecraftDedicatedServerMixin {

	@Inject(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/dedicated/MinecraftDedicatedServer;loadWorld()V",
					shift = At.Shift.AFTER
			),
			method = "setupServer"
	)
	private void mplugins$afterWorldLoad(CallbackInfoReturnable<Boolean> cir) {
		WorldReadyCallback.HOOK.invoker().onWorldReady();
	}
}