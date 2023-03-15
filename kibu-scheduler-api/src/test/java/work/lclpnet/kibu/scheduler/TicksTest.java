package work.lclpnet.kibu.scheduler;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicksTest {

    @Test
    void ticks() {
        assertWithFactor(1, Ticks::ticks);
    }

    @Test
    void seconds() {
        assertWithFactor(20, Ticks::seconds);
    }

    @Test
    void minutes() {
        assertWithFactor(20 * 60, Ticks::minutes);
    }

    private static void assertWithFactor(final long factor, UnaryOperator<Long> fun) {
        Consumer<Long> val = l -> assertEquals(l * factor, fun.apply(l));

        val.accept(1L);

        val.accept(20L);
        val.accept(60L);

        final var random = new SecureRandom();

        for (int i = 0; i < 100; i++) {
            val.accept(random.nextLong(Long.MAX_VALUE / factor));
        }
    }
}