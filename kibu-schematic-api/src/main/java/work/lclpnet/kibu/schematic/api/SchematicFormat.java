package work.lclpnet.kibu.schematic.api;

public interface SchematicFormat {

    SchematicWriter writer();

    SchematicReader reader();
}
