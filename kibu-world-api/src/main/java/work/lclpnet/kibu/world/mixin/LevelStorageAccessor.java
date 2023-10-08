package work.lclpnet.kibu.world.mixin;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.level.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelStorage.class)
public interface LevelStorageAccessor {

    @Invoker
    static <T> DataResult<WorldGenSettings> invokeReadGeneratorProperties(Dynamic<T> levelData, DataFixer dataFixer, int version) {
        throw new AssertionError();
    }
}
