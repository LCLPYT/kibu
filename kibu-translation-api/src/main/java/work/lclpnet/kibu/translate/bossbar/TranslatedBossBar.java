package work.lclpnet.kibu.translate.bossbar;

import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import work.lclpnet.kibu.translate.TranslationService;
import work.lclpnet.kibu.translate.text.RootText;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class TranslatedBossBar extends ServerBossBar implements CustomBossBar {

    private final BossBarProvider bossBarProvider;
    private final Identifier identifier;
    private final TranslationService translations;
    private final String defaultLanguage;
    private final Map<String, CommandBossBar> localizedBars = new HashMap<>();
    private final Map<UUID, String> players = new HashMap<>();
    private String translationKey;
    private Object[] args;
    private boolean visible = true;
    private net.minecraft.text.Style titleStyle = net.minecraft.text.Style.EMPTY;

    public TranslatedBossBar(BossBarProvider bossBarProvider, Identifier identifier, TranslationService translations, String translationKey, Object[] args) {
        this(bossBarProvider, identifier, translations, "en_us", translationKey, args);
    }

    public TranslatedBossBar(BossBarProvider bossBarProvider, Identifier identifier, TranslationService translations, String defaultLanguage, String translationKey, Object[] args) {
        super(null, Color.WHITE, Style.PROGRESS);
        this.bossBarProvider = bossBarProvider;
        this.translations = translations;
        this.defaultLanguage = defaultLanguage;
        this.identifier = identifier;

        setTitle(translationKey, args);
    }

    @Override
    public void addPlayer(ServerPlayerEntity player) {
        final String language = translations.getLanguage(player);
        final UUID uuid = player.getUuid();

        final String oldLanguage = players.get(uuid);

        // check if language did change
        if (language.equals(oldLanguage)) return;

        if (oldLanguage != null) {
            // the language changed, remove the player from the old boss bar
            removePlayer(player);
        }

        CommandBossBar bossBar = getLocalizedBar(language);
        bossBar.addPlayer(player);

        players.put(uuid, language);
    }

    public void addPlayers(Iterable<? extends ServerPlayerEntity> players) {
        for (ServerPlayerEntity player : players) {
            addPlayer(player);
        }
    }

    @Override
    public void removePlayer(ServerPlayerEntity player) {
        String lang = players.remove(player.getUuid());
        if (lang == null) return;

        CommandBossBar bossBar = localizedBars.get(lang);
        if (bossBar != null) {
            bossBar.removePlayer(player);
        }
    }

    @Override
    public void clearPlayers() {
        for (CommandBossBar bossBar : localizedBars.values()) {
            bossBar.clearPlayers();
        }

        players.clear();
    }

    public void updatePlayerLanguage(ServerPlayerEntity player) {
        if (!players.containsKey(player.getUuid())) return;

        // adding the player will update the language
        addPlayer(player);
    }

    @Nonnull
    private CommandBossBar getLocalizedBar(String language) {
        return localizedBars.computeIfAbsent(language, this::createLocalizedBar);
    }

    @Nonnull
    private CommandBossBar createLocalizedBar(String language) {
        Text localizedTitle = getLocalizedTitle(language);

        String suffix = ("_" + language).replaceAll("[^a-z0-9/._-]", "");  // remove invalid characters
        Identifier localizedId = identifier.withSuffixedPath(suffix);

        return createBossBar(localizedId, localizedTitle);
    }

    @Nonnull
    private Text getLocalizedTitle(String language) {
        String titleLang = language;

        // if there is no translation for the language, fallback to default language
        if (!translations.getTranslator().hasTranslation(language, translationKey)) {
            titleLang = defaultLanguage;
        }

        RootText rootText = translations.translateText(titleLang, translationKey, args);
        rootText.setStyle(titleStyle);

        return rootText;
    }

    @Nonnull
    private CommandBossBar createBossBar(Identifier id, Text title) {
        CommandBossBar bossBar = bossBarProvider.createBossBar(id, title);

        bossBar.setPercent(percent);
        bossBar.setColor(color);
        bossBar.setStyle(style);
        bossBar.setVisible(visible);

        return bossBar;
    }

    public void setTitle(String translationKey, Object... args) {
        this.translationKey = translationKey;
        this.args = args;

        for (var entry : localizedBars.entrySet()) {
            Text localizedTitle = getLocalizedTitle(entry.getKey());

            CommandBossBar bossBar = entry.getValue();
            bossBar.setName(localizedTitle);
        }
    }

    @Override
    public void setPercent(float percent) {
        this.percent = percent;

        updateBars(bar -> bar.setPercent(this.percent));
    }

    @Override
    public void setColor(Color color) {
        this.color = color;

        updateBars(bar -> bar.setColor(this.color));
    }

    @Override
    public void setStyle(Style style) {
        this.style = style;

        updateBars(bar -> bar.setStyle(this.style));
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;

        updateBars(bar -> bar.setVisible(this.visible));
    }

    @Override
    public BossBar setDarkenSky(boolean darkenSky) {
        this.darkenSky = darkenSky;

        updateBars(bar -> bar.setDarkenSky(this.darkenSky));

        return this;
    }

    @Override
    public BossBar setDragonMusic(boolean dragonMusic) {
        this.dragonMusic = dragonMusic;

        updateBars(bar -> bar.setDragonMusic(this.dragonMusic));

        return this;
    }

    @Override
    public BossBar setThickenFog(boolean thickenFog) {
        this.thickenFog = thickenFog;

        updateBars(bar -> bar.setThickenFog(this.thickenFog));

        return this;
    }

    /**
     * Sets the name of this boss bar.
     * @param name The name.
     * @apiNote Use {@link TranslatedBossBar#setTitle(String, Object...)} instead for this class.
     */
    @Override
    public void setName(Text name) {
        throw new IllegalStateException("setName() invoked on TranslatedBossBar");
    }

    private void updateBars(Consumer<CommandBossBar> action) {
        for (var entry : localizedBars.entrySet()) {
            CommandBossBar bossBar = entry.getValue();
            action.accept(bossBar);
        }
    }

    public net.minecraft.text.Style getTitleStyle() {
        return titleStyle;
    }

    public void setTitleStyle(net.minecraft.text.Style style) {
        this.titleStyle = style;
    }

    /**
     * Updates the style of the title text.
     *
     * @see #getTitleStyle()
     * @see #setTitleStyle(net.minecraft.text.Style)
     *
     * @param styleUpdater the style updater
     */
    public TranslatedBossBar styled(UnaryOperator<net.minecraft.text.Style> styleUpdater) {
        this.setTitleStyle(styleUpdater.apply(this.getTitleStyle()));
        return this;
    }

    /**
     * Fills the absent parts of the title text's style with definitions from {@code styleOverride}.
     *
     * @see net.minecraft.text.Style#withParent(net.minecraft.text.Style)
     *
     * @param styleOverride the style that provides definitions for absent definitions in the title text's style
     */
    public TranslatedBossBar fillStyle(net.minecraft.text.Style styleOverride) {
        this.setTitleStyle(styleOverride.withParent(this.getTitleStyle()));
        return this;
    }

    /**
     * Adds some formattings to the title text's style.
     *
     * @param formattings an array of formattings
     */
    public TranslatedBossBar formatted(Formatting... formattings) {
        this.setTitleStyle(this.getTitleStyle().withFormatting(formattings));
        return this;
    }

    /**
     * Add a formatting to the title text's style.
     *
     * @param formatting a formatting
     */
    public TranslatedBossBar formatted(Formatting formatting) {
        this.setTitleStyle(this.getTitleStyle().withFormatting(formatting));
        return this;
    }
}
