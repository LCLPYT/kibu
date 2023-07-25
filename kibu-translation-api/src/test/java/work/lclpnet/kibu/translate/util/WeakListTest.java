package work.lclpnet.kibu.translate.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeakListTest {

    @Test
    void testIterator() {
        Holder holder = new Holder();
        holder.item = new Object();

        WeakList<Object> list = new WeakList<>();
        list.add(holder.item);

        for (int i = 0; i < 2; i++) {
            var iterator = list.iterator();
            assertTrue(iterator.hasNext());
            assertEquals(holder.item, iterator.next());
            assertFalse(iterator.hasNext());
        }
    }

    @Test
    void testWeakReference() {
        Holder holder = new Holder();
        holder.item = new Object();

        assertNotNull(holder.item);

        WeakList<Object> list = new WeakList<>();
        list.add(holder.item);

        int i = 0;

        for (Object ignored : list) {
            i++;
        }

        assertEquals(1, i);

        holder.item = null;
        System.gc();

        i = 0;

        for (Object ignored : list) {
            i++;
        }

        assertEquals(0, i);
    }

    private static class Holder {
        Object item;
    }
}