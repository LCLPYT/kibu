package work.lclpnet.kibu.translate.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.bossevents.CustomBossEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import work.lclpnet.kibu.translate.util.TransientBossBars;

import java.util.Map;
import java.util.stream.Collectors;

@Mixin(CustomBossEvents.class)
public class CustomBossEventsMixin {

    @Shadow
    @Final
    private Map<Identifier, CustomBossEvent> events;

    @ModifyArg(
            method = "save",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Util;mapValues(Ljava/util/Map;Ljava/util/function/Function;)Ljava/util/Map;"
            )
    )
    private Map<Identifier, CustomBossEvent> kibu$excludeTransient(Map<Identifier, CustomBossEvent> bars) {
        return events.entrySet().stream()
                .filter(entry -> !TransientBossBars.isTransient(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
