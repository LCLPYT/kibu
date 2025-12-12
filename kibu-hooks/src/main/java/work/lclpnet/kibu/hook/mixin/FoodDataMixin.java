package work.lclpnet.kibu.hook.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import work.lclpnet.kibu.hook.player.PlayerFoodHooks;
import work.lclpnet.kibu.hook.type.PlayerAware;

@Mixin(FoodData.class)
public class FoodDataMixin implements PlayerAware {

    @Shadow
    private int foodLevel;
    @Shadow
    private float exhaustionLevel;
    @Shadow
    private float saturationLevel;

    @Unique
    private Player player;

    @Redirect(
            method = "*",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/food/FoodData;foodLevel:I",
                    opcode = Opcodes.PUTFIELD
            )
    )
    public void kibu$onChangeFoodLevel(FoodData HungerManager, int foodLevel) {
        boolean cancel = PlayerFoodHooks.LEVEL_CHANGE.invoker().onChange(player, this.foodLevel, foodLevel);
        if (!cancel) this.foodLevel = foodLevel;
    }

    @Redirect(
            method = {"tick", "addExhaustion"},
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/food/FoodData;exhaustionLevel:F",
                    opcode = Opcodes.PUTFIELD
            )
    )
    public void kibu$onChangeExhaustion(FoodData HungerManager, float exhaustion) {
        boolean cancel = PlayerFoodHooks.EXHAUSTION_CHANGE.invoker().onChange(player, this.exhaustionLevel, exhaustion);
        if (!cancel) this.exhaustionLevel = exhaustion;
    }

    @Redirect(
            method = "*",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/food/FoodData;saturationLevel:F",
                    opcode = Opcodes.PUTFIELD
            )
    )
    public void kibu$onChangeFoodSaturationLevel(FoodData HungerManager, float foodSaturationLevel) {
        boolean cancel = PlayerFoodHooks.SATURATION_CHANGE.invoker().onChange(player, this.saturationLevel, foodSaturationLevel);
        if (!cancel) this.saturationLevel = foodSaturationLevel;
    }

    @Override
    public void kibu$setPlayer(Player player) {
        this.player = player;
    }
}
