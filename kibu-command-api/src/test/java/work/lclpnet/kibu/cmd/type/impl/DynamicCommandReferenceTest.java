package work.lclpnet.kibu.cmd.type.impl;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DynamicCommandReferenceTest {

    @Test
    void getCommand_empty_empty() {
        var ref = new DynamicCommandReference<>(cmd -> {});
        assertTrue(ref.getCommand().isEmpty());
    }

    @Test
    void getCommand_present_present() {
        var ref = new DynamicCommandReference<>(cmd -> {});
        ref.acceptCommand(mock());
        assertTrue(ref.getCommand().isPresent());
    }

    @Test
    void whenReady_preAccept_executed() {
        var ref = new DynamicCommandReference<>(cmd -> {});

        var future = ref.whenReady();
        assertFalse(future.isDone());

        ref.acceptCommand(mock());

        assertTrue(future.isDone());
    }

    @Test
    void whenReady_postAccept_executedInstantly() {
        var ref = new DynamicCommandReference<>(cmd -> {});

        ref.acceptCommand(mock());

        assertTrue(ref.whenReady().isDone());
    }

    @Test
    void whenReady_acceptMultiple_onlyOnce() {
        var ref = new DynamicCommandReference<>(cmd -> {});

        AtomicInteger counter = new AtomicInteger(0);
        ref.whenReady().thenRun(counter::incrementAndGet);

        ref.acceptCommand(mock());
        ref.acceptCommand(mock());

        assertEquals(1, counter.get());
    }

    @Test
    void unregister_empty_noop() {
        var ref = new DynamicCommandReference<>(cmd -> fail("Unregister should not have been called"));
        ref.unregister();
    }

    @Test
    void unregister_notEmpty_called() {
        AtomicBoolean executed = new AtomicBoolean(false);
        var ref = new DynamicCommandReference<>(cmd -> executed.set(true));

        ref.acceptCommand(mock());
        ref.unregister();

        assertTrue(executed.get());
    }

    @Test
    void unregister_notEmpty_willBeEmpty() {
        var ref = new DynamicCommandReference<>(cmd -> {});

        ref.acceptCommand(mock());
        ref.unregister();

        assertTrue(ref.getCommand().isEmpty());
    }
}