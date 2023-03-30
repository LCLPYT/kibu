package work.lclpnet.kibu.mc;

public interface BlockState {

    String getAsString();

    /**
     * Determines whether this block state is air and thus ignored by structures / schematics.
     * @return True, if this block state should be ignored by structures / schematics.
     */
    boolean isAir();
}
