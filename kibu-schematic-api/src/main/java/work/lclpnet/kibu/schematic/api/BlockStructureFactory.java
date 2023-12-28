package work.lclpnet.kibu.schematic.api;

import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.structure.BlockStructure;

public interface BlockStructureFactory {

    BlockStructure create(int width, int height, int length, KibuBlockPos origin, int dataVersion);
}
