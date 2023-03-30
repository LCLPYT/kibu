package work.lclpnet.kibu.jnbt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListTagTest {

    @Test
    void testEmpty() {
        var list = new ListTag(NBTConstants.TYPE_STRING);
        assertTrue(list.getValue().isEmpty());
    }

    @Test
    void testAddContains() {
        var list = new ListTag(IntTag.class);

        final IntTag tag = new IntTag(1234);
        assertFalse(list.contains(tag));
        list.add(tag);
        assertTrue(list.contains(tag));
    }

    @Test
    void testRemoveNoLongerContains() {
        var list = new ListTag(IntTag.class);

        final IntTag tag = new IntTag(1234);
        list.add(tag);
        assertTrue(list.contains(tag));
        list.remove(tag);
        assertFalse(list.contains(tag));
    }
}