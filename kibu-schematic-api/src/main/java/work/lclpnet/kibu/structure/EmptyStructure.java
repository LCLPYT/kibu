package work.lclpnet.kibu.structure;

import work.lclpnet.kibu.mc.KibuBlockEntity;
import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.mc.KibuBlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

class EmptyStructure implements BlockStructure {

    private final List<KibuBlockPos> positions = Collections.emptyList();
    private final KibuBlockPos pos = new KibuBlockPos(0, 0, 0);

    @Override
    public @Nullable KibuBlockEntity getBlockEntity(KibuBlockPos pos) {
        return null;
    }

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
        return null;
    }

    @Override
    public Iterable<KibuBlockPos> getBlockPositions() {
        return positions;
    }

    @Override
    public int getBlockCount() {
        return 0;
    }
}
