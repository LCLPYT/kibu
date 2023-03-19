package work.lclpnet.kibu.scheduler;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicksTest {

    @Test
    void ticksInt() {
        assertWithFactor(1, (UnaryOperator<Integer>) Ticks::ticks);
    }

    @Test
    void secondsInt() {
        assertWithFactor(20, (UnaryOperator<Integer>) Ticks::seconds);
    }

    @Test
    void minutesInt() {
        assertWithFactor(20 * 60, (UnaryOperator<Integer>) Ticks::minutes);
    }

    @Test
    void ticksLong() {
        assertWithFactor(1L, Ticks::ticks);
    }

    @Test
    void secondsLong() {
        assertWithFactor(20L, Ticks::seconds);
    }

    @Test
    void minutesLong() {
        assertWithFactor(20L * 60, Ticks::minutes);
    }

    private static void assertWithFactor(final int factor, UnaryOperator<Integer> fun) {
        Consumer<Integer> val = l -> assertEquals(l * factor, fun.apply(l));

        val.accept(1);

        val.accept(20);
        val.accept(60);

        final var random = new SecureRandom();

        for (int i = 0; i < 100; i++) {
            val.accept(random.nextInt(Integer.MAX_VALUE / factor));
        }
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