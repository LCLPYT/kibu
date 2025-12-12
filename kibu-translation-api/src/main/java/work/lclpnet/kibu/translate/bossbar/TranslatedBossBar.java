package work.lclpnet.kibu.translate.bossbar;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import org.jetbrains.annotations.NotNull;
import work.lclpnet.kibu.translate.Translations;
import work.lclpnet.kibu.translate.text.RootText;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class TranslatedBossBar extends ServerBossEvent implements CustomBossBar {

    private final BossBarProvider bossBarProvider;
    private final ResourceLocation identifier;
    private final Translations translations;
    private final String defaultLanguage;
    private final Map<String, CustomBossEvent> localizedBars = new HashMap<>();
    private final Map<UUID, String> players = new HashMap<>();
    private String translationKey;
    private Object[] args;
    private boolean visible = true;
    private net.minecraft.network.chat.Style titleStyle = net.minecraft.network.chat.Style.EMPTY;

    public TranslatedBossBar(BossBarProvider bossBarProvider, ResourceLocation identifier, Translations translations, String translationKey, Object[] args) {
        this(bossBarProvider, identifier, translations, "en_us", translationKey, args);
    }

    public TranslatedBossBar(BossBarProvider bossBarProvider, ResourceLocation identifier, Translations translations, String defaultLanguage, String translationKey, Object[] args) {
        super(null, BossBarColor.WHITE, BossBarOverlay.PROGRESS);
        this.bossBarProvider = bossBarProvider;
        this.translations = translations;
        this.defaultLanguage = defaultLanguage;
        this.identifier = identifier;

        setTitle(translationKey, args);
    }

    @Override
    public void addPlayer(ServerPlayer player) {
        //noinspection ConstantValue -- dont trust the annotation assumptions...
        if (player.connection == null) return;  // prevent NPE if someone calls this too early in the join process

        final String language = translations.getLanguage(player);
        final UUID uuid = player.getUUID();

        final String oldLanguage = players.get(uuid);

        // check if language did change
        if (language.equals(oldLanguage)) return;

        if (oldLanguage != null) {
            // the language changed, remove the player from the old boss bar
            removePlayer(player);
        }

        CustomBossEvent bossBar = getLocalizedBar(language);
        bossBar.addPlayer(player);

        players.put(uuid, language);
    }

    public void addPlayers(Iterable<? extends ServerPlayer> players) {
        for (ServerPlayer player : players) {
            addPlayer(player);
        }
    }

    @Override
    public void removePlayer(ServerPlayer player) {
        String lang = players.remove(player.getUUID());
        if (lang == null) return;

        CustomBossEvent bossBar = localizedBars.get(lang);
        if (bossBar != null) {
            bossBar.removePlayer(player);
        }
    }

    @Override
    public void removeAllPlayers() {
        for (CustomBossEvent bossBar : localizedBars.values()) {
            bossBar.removeAllPlayers();
        }

        players.clear();
    }

    public void updatePlayerLanguage(ServerPlayer player) {
        if (!players.containsKey(player.getUUID())) return;

        // adding the player will update the language
        addPlayer(player);
    }

    @NotNull
    private CustomBossEvent getLocalizedBar(String language) {
        return localizedBars.computeIfAbsent(language, this::createLocalizedBar);
    }

    @NotNull
    private CustomBossEvent createLocalizedBar(String language) {
        Component localizedTitle = getLocalizedTitle(language);

        String suffix = ("_" + language).replaceAll("[^a-z0-9/._-]", "");  // remove invalid characters
        ResourceLocation localizedId = identifier.withSuffix(suffix);

        return createBossBar(localizedId, localizedTitle);
    }

    @NotNull
    private Component getLocalizedTitle(String language) {
        RootText rootText = translations.translateText(language, translationKey, args);
        rootText.setStyle(titleStyle);

        return rootText;
    }

    @NotNull
    private CustomBossEvent createBossBar(ResourceLocation id, Component title) {
        CustomBossEvent bossBar = bossBarProvider.createBossBar(id, title);

        bossBar.setProgress(progress);
        bossBar.setColor(color);
        bossBar.setOverlay(overlay);
        bossBar.setVisible(visible);

        return bossBar;
    }

    public void setTitle(String translationKey, Object... args) {
        this.translationKey = translationKey;
        this.args = args;

        for (var entry : localizedBars.entrySet()) {
            Component localizedTitle = getLocalizedTitle(entry.getKey());

            CustomBossEvent bossBar = entry.getValue();
            bossBar.setName(localizedTitle);
        }
    }

    @Override
    public void setProgress(float percent) {
        this.progress = percent;

        updateBars(bar -> bar.setProgress(this.progress));
    }

    @Override
    public void setColor(BossBarColor color) {
        this.color = color;

        updateBars(bar -> bar.setColor(this.color));
    }

    @Override
    public void setOverlay(BossBarOverlay style) {
        this.overlay = style;

        updateBars(bar -> bar.setOverlay(this.overlay));
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;

        updateBars(bar -> bar.setVisible(this.visible));
    }

    @Override
    public BossEvent setDarkenScreen(boolean darkenSky) {
        this.darkenScreen = darkenSky;

        updateBars(bar -> bar.setDarkenScreen(this.darkenScreen));

        return this;
    }

    @Override
    public BossEvent setPlayBossMusic(boolean dragonMusic) {
        this.playBossMusic = dragonMusic;

        updateBars(bar -> bar.setPlayBossMusic(this.playBossMusic));

        return this;
    }

    @Override
    public BossEvent setCreateWorldFog(boolean thickenFog) {
        this.createWorldFog = thickenFog;

        updateBars(bar -> bar.setCreateWorldFog(this.createWorldFog));

        return this;
    }

    /**
     * Sets the name of this boss bar.
     * @param name The name.
     * @apiNote Use {@link TranslatedBossBar#setTitle(String, Object...)} instead for this class.
     */
    @Override
    public void setName(Component name) {
        throw new IllegalStateException("setName() invoked on TranslatedBossBar");
    }

    private void updateBars(Consumer<CustomBossEvent> action) {
        for (var entry : localizedBars.entrySet()) {
            CustomBossEvent bossBar = entry.getValue();
            action.accept(bossBar);
        }
    }

    public net.minecraft.network.chat.Style getTitleStyle() {
        return titleStyle;
    }

    public void setTitleStyle(net.minecraft.network.chat.Style style) {
        this.titleStyle = style;
    }

    /**
     * Updates the style of the title text.
     *
     * @see #getTitleStyle()
     * @see #setTitleStyle(net.minecraft.network.chat.Style)
     *
     * @param styleUpdater the style updater
     */
    public TranslatedBossBar styled(UnaryOperator<net.minecraft.network.chat.Style> styleUpdater) {
        this.setTitleStyle(styleUpdater.apply(this.getTitleStyle()));
        return this;
    }

    /**
     * Fills the absent parts of the title text's style with definitions from {@code styleOverride}.
     *
     * @see net.minecraft.network.chat.Style#applyTo(net.minecraft.network.chat.Style)
     *
     * @param styleOverride the style that provides definitions for absent definitions in the title text's style
     */
    public TranslatedBossBar fillStyle(net.minecraft.network.chat.Style styleOverride) {
        this.setTitleStyle(styleOverride.applyTo(this.getTitleStyle()));
        return this;
    }

    /**
     * Adds some formattings to the title text's style.
     *
     * @param formattings an array of formattings
     */
    public TranslatedBossBar formatted(ChatFormatting... formattings) {
        this.setTitleStyle(this.getTitleStyle().applyFormats(formattings));
        return this;
    }

    /**
     * Add a formatting to the title text's style.
     *
     * @param formatting a formatting
     */
    public TranslatedBossBar formatted(ChatFormatting formatting) {
        this.setTitleStyle(this.getTitleStyle().applyFormat(formatting));
        return this;
    }
}
