package work.lclpnet.kibu.translate.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.bossevents.CustomBossEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import work.lclpnet.kibu.translate.util.TransientBossBars;

import java.util.Map;
import java.util.stream.Collectors;

@Mixin(CustomBossEvents.class)
public class CustomBossEventsMixin {

    @ModifyArg(
            method = "lambda$static$2",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Util;mapValues(Ljava/util/Map;Ljava/util/function/Function;)Ljava/util/Map;"
            )
    )
    private static Map<Identifier, CustomBossEvent> kibu$excludeTransient(Map<Identifier, CustomBossEvent> bars) {
        return bars.entrySet().stream()
                .filter(entry -> !TransientBossBars.isTransient(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
