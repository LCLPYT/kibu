package work.lclpnet.kibu.world.mixin;

import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.world.KibuWorlds;
import work.lclpnet.kibu.world.KibuWorldsImpl;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

@Mixin(value = Fantasy.class, remap = false)
public class FantasyMixin {

    @Inject(
            method = "openTemporaryWorld",
            at = @At("RETURN"),
            remap = false
    )
    public void kibu$openTemporaryWorld(RuntimeWorldConfig config, CallbackInfoReturnable<RuntimeWorldHandle> cir) {
        RuntimeWorldHandle handle = cir.getReturnValue();
        KibuWorldsImpl impl = (KibuWorldsImpl) KibuWorlds.getInstance();

        impl.registerWorldHandle(handle);
    }

    @Inject(
            method = "getOrOpenPersistentWorld",
            at = @At("RETURN"),
            remap = false
    )
    public void kibu$getOrOpenPersistentWorld(Identifier key, RuntimeWorldConfig config, CallbackInfoReturnable<RuntimeWorldHandle> cir) {
        RuntimeWorldHandle handle = cir.getReturnValue();
        KibuWorldsImpl impl = (KibuWorldsImpl) KibuWorlds.getInstance();

        impl.registerWorldHandle(handle);
    }
}
