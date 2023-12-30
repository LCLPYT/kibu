package work.lclpnet.kibu.structure;

import work.lclpnet.kibu.mc.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

class EmptyStructure implements BlockStructure {

    private final KibuBlockPos pos = new KibuBlockPos(0, 0, 0);

    @Override
    public int getDataVersion() {
        return 0;
    }

    @Override
    public KibuBlockPos getOrigin() {
        return pos;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public void setBlockState(KibuBlockPos pos, KibuBlockState state) {
    }

    @Nonnull
    @Override
    public KibuBlockState getBlockState(KibuBlockPos pos) {
        return BuiltinKibuBlockState.AIR;
    }

    @Override
    public Iterable<KibuBlockPos> getBlockPositions() {
        return Collections.emptyList();
    }

    @Override
    public int getBlockCount() {
        return 0;
    }

    @Override
    public @Nullable KibuBlockEntity getBlockEntity(KibuBlockPos pos) {
        return null;
    }

    @Override
    public boolean addEntity(KibuEntity entity) {
        return false;
    }

    @Override
    public boolean removeEntity(KibuEntity entity) {
        return false;
    }

    @Override
    public Collection<? extends KibuEntity> getEntities() {
        return Collections.emptyList();
    }

    @Override
    public void setBlockEntity(KibuBlockPos pos, KibuBlockEntity blockEntity) {

    }

    @Override
    public int getBlockEntityCount() {
        return 0;
    }
}
