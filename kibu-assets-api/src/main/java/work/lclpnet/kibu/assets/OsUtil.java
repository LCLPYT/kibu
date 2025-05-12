package work.lclpnet.kibu.assets;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Objects;

public class OsUtil {

    public static @NotNull Path getOsDataDir() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");

        Objects.requireNonNull(userHome, "Property user.home doesn't exist");

        if (os.contains("win")) {
            // Windows: %LOCALAPPDATA% (default ~\AppData\Local)
            String appData = System.getenv("LOCALAPPDATA");

            if (appData == null || appData.isEmpty()) {
                return Path.of(userHome, "AppData", "Local");
            }

            return Path.of(appData);
        }

        if (os.contains("mac")) {
            // macOS: ~/Library/Application Support
            return Path.of(userHome, "Library", "Application Support");
        }

        // Linux: $XDG_DATA_HOME (default ~/.local/share)
        // https://specifications.freedesktop.org/basedir-spec/latest/
        String xdgDataHome = System.getenv("XDG_DATA_HOME");

        if (xdgDataHome == null || xdgDataHome.isEmpty()) {
            return Path.of(userHome, ".local", "share");
        }

        return Path.of(xdgDataHome);
    }
}
