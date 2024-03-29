package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import work.lclpnet.kibu.hook.player.PlayerFoodHooks;
import work.lclpnet.kibu.hook.type.PlayerAware;

@Mixin(HungerManager.class)
public class HungerManagerMixin implements PlayerAware {

    @Shadow
    private int foodLevel;
    @Shadow
    private float exhaustion;
    @Shadow
    private float saturationLevel;

    @Unique
    private PlayerEntity player;

    @Redirect(
            method = "*",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/HungerManager;foodLevel:I",
                    opcode = Opcodes.PUTFIELD
            )
    )
    public void kibu$onChangeFoodLevel(HungerManager HungerManager, int foodLevel) {
        boolean cancel = PlayerFoodHooks.LEVEL_CHANGE.invoker().onChange(player, this.foodLevel, foodLevel);
        if (!cancel) this.foodLevel = foodLevel;
    }

    @Redirect(
            method = "*",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/HungerManager;exhaustion:F",
                    opcode = Opcodes.PUTFIELD
            )
    )
    public void kibu$onChangeExhaustion(HungerManager HungerManager, float exhaustion) {
        boolean cancel = PlayerFoodHooks.EXHAUSTION_CHANGE.invoker().onChange(player, this.exhaustion, exhaustion);
        if (!cancel) this.exhaustion = exhaustion;
    }

    @Redirect(
            method = "*",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/HungerManager;saturationLevel:F",
                    opcode = Opcodes.PUTFIELD
            )
    )
    public void kibu$onChangeFoodSaturationLevel(HungerManager HungerManager, float foodSaturationLevel) {
        boolean cancel = PlayerFoodHooks.SATURATION_CHANGE.invoker().onChange(player, this.saturationLevel, foodSaturationLevel);
        if (!cancel) this.saturationLevel = foodSaturationLevel;
    }

    @Override
    public void kibu$setPlayer(PlayerEntity player) {
        this.player = player;
    }
}
