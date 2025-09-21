package work.lclpnet.kibu.translate.mixin;

import net.minecraft.entity.boss.BossBarManager;
import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import work.lclpnet.kibu.translate.util.TransientBossBars;

import java.util.Map;
import java.util.stream.Collectors;

@Mixin(BossBarManager.class)
public class BossBarManagerMixin {

    @Shadow
    @Final
    private Map<Identifier, CommandBossBar> commandBossBars;

    @ModifyArg(
            method = "toNbt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Util;transformMapValues(Ljava/util/Map;Ljava/util/function/Function;)Ljava/util/Map;"
            )
    )
    private Map<Identifier, CommandBossBar> kibu$excludeTransient(Map<Identifier, CommandBossBar> bars) {
        return commandBossBars.entrySet().stream()
                .filter(entry -> !TransientBossBars.isTransient(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
