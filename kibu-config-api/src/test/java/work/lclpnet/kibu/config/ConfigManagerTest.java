package work.lclpnet.kibu.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.GenericBuilder;
import com.electronwill.nightconfig.core.serde.annotations.SerdeComment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ConfigManagerTest {

    Path path;
    ConfigManager<TestConfig> manager;

    @BeforeEach
    void setup() throws IOException {
        path = Files.createTempFile("kibu-config-api-tests", "cfg.toml");
        manager = new ConfigManager<>(path, new TestConfig(), GenericBuilder::sync);  // save synchronously
    }

    @AfterEach
    void cleanup() {
        manager.close();
    }

    @Test
    void load_newConfig_initWithDefault() {
        try (var cfg = CommentedFileConfig.of(path)) {
            assertTrue(cfg.isEmpty());
        }

        manager.load();

        try (var cfg = CommentedFileConfig.builder(path).sync().build()) {
            cfg.load();

            assertEquals(true, cfg.get("foo"));
            assertEquals(Math.PI, cfg.get("bar"));
            assertEquals(42, cfg.getInt("baz"));
            assertEquals("Hello World!", cfg.get("msg"));
            assertEquals(TestShape.TRIANGLE, TestShape.valueOf(cfg.get("shape")));
            assertEquals(0xdeadbeef, cfg.getInt("nested.symbol"));

            assertEquals("Whether foo is enabled", cfg.getComment("foo"));
            assertEquals("Hi", cfg.getComment("msg"));
            assertEquals("Configs can also be nested", cfg.getComment("nested"));
            assertEquals("Some character", cfg.getComment("nested.symbol"));
        }
    }

    @Test
    void load_incompleteConfig_missingEntriesAdded() {
        try (var managerTmp = new ConfigManager<>(path, new TestConfig(), GenericBuilder::sync)) {
            managerTmp.load();
        }

        try (var cfg = CommentedFileConfig.builder(path).sync().build()) {
            cfg.load();

            assertEquals("Hello World!", cfg.remove("msg"));
            assertEquals(0xdeadbeef, (int) cfg.remove("nested.symbol"));

            cfg.save();
        }

        try (var cfg = CommentedFileConfig.builder(path).sync().build()) {
            cfg.load();

            assertFalse(cfg.contains("msg"));
            assertFalse(cfg.contains("nested.symbol"));
        }

        manager.load();

        try (var cfg = CommentedFileConfig.builder(path).sync().build()) {
            cfg.load();

            assertEquals("Hello World!", cfg.get("msg"));
            assertEquals(0xdeadbeef, cfg.getInt("nested.symbol"));

            assertEquals("Hi", cfg.getComment("msg"));
            assertEquals("Some character", cfg.getComment("nested.symbol"));
        }
    }

    @Test
    void load_newConfig_enumCommentsAdded() {
        try (var cfg = CommentedFileConfig.of(path)) {
            assertTrue(cfg.isEmpty());
        }

        manager.load();

        try (var cfg = CommentedFileConfig.builder(path).sync().build()) {
            cfg.load();

            assertEquals("""
                    Favorite Shape
                    Possible options:
                        CIRCLE: A circle is round
                        SQUARE: A square is square
                        TRIANGLE: A triangle has three vertices""", cfg.getComment("shape"));
        }
    }

    @Test
    void save_changes_written() {
        manager.load();

        manager.config().foo = false;

        manager.save();

        try (var cfg = CommentedFileConfig.builder(path).sync().build()) {
            cfg.load();

            assertEquals(false, cfg.get("foo"));
        }
    }

    private static class TestConfig {

        @SerdeComment("Whether foo is enabled")
        boolean foo = true;

        double bar = Math.PI;

        int baz = 42;

        @SerdeComment("Hi")
        String msg = "Hello World!";

        @SerdeComment("Favorite Shape")
        TestShape shape = TestShape.TRIANGLE;

        @SerdeComment("Configs can also be nested")
        NestedConfig nested = new NestedConfig();
    }

    private enum TestShape {

        @SerdeComment("A circle is round")
        CIRCLE,

        @SerdeComment("A square is square")
        SQUARE,

        @SerdeComment("A triangle has three vertices")
        TRIANGLE

    }

    private static class NestedConfig {

        @SerdeComment("Some character")
        int symbol = 0xdeadbeef;
    }
}