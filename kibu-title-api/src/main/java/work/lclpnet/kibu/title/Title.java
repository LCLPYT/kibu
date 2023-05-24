package work.lclpnet.kibu.title;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public interface Title {

    /**
     * Sets title times for the player.
     * @param in Fade-in time in ticks
     * @param stay Time to stay in ticks
     * @param out Fade-out time in ticks
     */
    void times(int in, int stay, int out);

    /**
     * Sends a title to the player.
     * @param title The text to display as title.
     */
    void title(Text title);

    /**
     * Sends a subtitle to the player.
     * @param subtitle The text to display as subtitle.
     */
    void subtitle(Text subtitle);

    /**
     * Clears the current title and subtitle for the player.
     * Optionally, this can also reset the title times.
     * @param resetTimes Whether to reset title times.
     */
    void clear(boolean resetTimes);

    /**
     * Clears the current title and subtitle for a player.
     */
    default void clear() {
        clear(false);
    }

    /**
     * Resets title times to the default values.
     * <code>in=10, stay=70, out=20</code>
     */
    default void resetTimes() {
        times(10, 70, 20);
    }

    /**
     * Sends the player a title and subtitle.
     * @param title The title text
     * @param subtitle The subtitle text
     * @param in Fade-in time in ticks
     * @param stay Stay time in ticks
     * @param out Fade-out time in ticks
     */
    default void title(Text title, Text subtitle, int in, int stay, int out) {
        times(in, stay, out);
        title(title);
        subtitle(subtitle);
    }

    /**
     * Sends the player a title and subtitle.
     * @param title The title text
     * @param subtitle The subtitle text
     */
    default void title(Text title, Text subtitle) {
        title(title);
        subtitle(subtitle);
    }

    static Title get(ServerPlayerEntity player) {
        return ((TitleAccess) player).kibu$getTitle();
    }
}
