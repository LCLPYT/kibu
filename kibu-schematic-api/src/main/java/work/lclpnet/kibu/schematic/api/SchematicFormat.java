package work.lclpnet.kibu.schematic.api;

public interface SchematicFormat {

    SchematicSerializer serializer();

    SchematicDeserializer deserializer();

    SchematicWriter writer();

    SchematicReader reader();
}
