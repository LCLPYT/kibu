package work.lclpnet.kibu.hook.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record BlockModification(
        BlockModificationType type,
        World world,
        BlockPos pos,
        Entity entity
) {

    public boolean isPlayer() {
        return entity instanceof PlayerEntity;
    }

    public PlayerEntity player() {
        return (PlayerEntity) entity;
    }
}
