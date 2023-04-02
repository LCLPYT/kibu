package work.lclpnet.kibu.schematic.sponge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SpongeSchematicV2Test {

    @Test
    public void testWriter() {
        var format = new SpongeSchematicV2();
        assertNotNull(format.writer());
    }

    @Test
    public void testReader() {
        var format = new SpongeSchematicV2();
        assertNotNull(format.reader());
    }

    @Test
    public void testSerializer() {
        var format = new SpongeSchematicV2();
        assertNotNull(format.serializer());
    }

    @Test
    public void testDeserializer() {
        var format = new SpongeSchematicV2();
        assertNotNull(format.deserializer());
    }
}
