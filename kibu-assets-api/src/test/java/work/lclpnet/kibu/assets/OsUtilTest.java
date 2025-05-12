package work.lclpnet.kibu.assets;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OsUtilTest {

    @Test
    void getCacheDir() {
        Path cacheDir = OsUtil.getCacheDir();

        String userHome = System.getProperty("user.home");

        assertNotNull(userHome, "User home property isn't set");

        String os = System.getProperty("os.name").toLowerCase();

        // this test only support linux atm
        if (os.contains("win") || os.contains("mac")) return;

        String xdgCacheHome = System.getenv("XDG_CACHE_HOME");

        if (xdgCacheHome == null ||  xdgCacheHome.isEmpty()) {
            assertEquals(Path.of(userHome, ".cache"), cacheDir);
        } else {
            assertEquals(Path.of(xdgCacheHome), cacheDir);
        }
    }
}