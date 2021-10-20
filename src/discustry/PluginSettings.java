package discustry;

import arc.files.Fi;
import arc.util.serialization.JsonReader;
import arc.util.serialization.JsonValue;
import arc.util.serialization.JsonWriter;

public class PluginSettings {
    public final static String settingsPath = "config/mods/discustry.json";

    private static boolean isInitialised = false;

    private static String currentLocale;
    private static String token;
    private static String channel;
    private static String url;

    private static Integer colorOnJoin;
    private static Integer colorOnLeave;
    private static Integer colorOnGameStart;
    private static Integer colorOnWaveStart;
    private static Integer colorOnGameOver;
    private static Integer colorOnPause;
    private static Integer colorOnUnpause;

    private static String messagePattern;

    private static void init() {
        isInitialised = true;

        load();
    }

    public static void setChannel(String channel) {
        if (!isInitialised) {
            init();
        }

        PluginSettings.channel = channel;
    }

    public static void setCurrentLocale(String currentLocale) {
        if (!isInitialised) {
            init();
        }

        PluginSettings.currentLocale = currentLocale;
    }

    public static void setToken(String token) {
        if (!isInitialised) {
            init();
        }

        PluginSettings.token = token;
    }

    public static void setUrl(String url) {
        if (!isInitialised) {
            init();
        }

        PluginSettings.url = url;
    }

    public static void setColorOnJoin(Integer colorOnJoin) {
        if (!isInitialised) {
            init();
        }

        PluginSettings.colorOnJoin = colorOnJoin;
    }

    public static void setColorOnLeave(Integer colorOnLeave) {
        if (!isInitialised) {
            init();
        }

        PluginSettings.colorOnLeave = colorOnLeave;
    }

    public static void setColorOnGameStart(Integer colorOnGameStart) {
        if (!isInitialised) {
            init();
        }

        PluginSettings.colorOnGameStart = colorOnGameStart;
    }

    public static void setColorOnWaveStart(Integer colorOnWaveStart) {
        if (!isInitialised) {
            init();
        }

        PluginSettings.colorOnWaveStart = colorOnWaveStart;
    }

    public static void setColorOnGameOver(Integer colorOnGameOver) {
        if (!isInitialised) {
            init();
        }

        PluginSettings.colorOnGameOver = colorOnGameOver;
    }

    public static void setColorOnPause(Integer colorOnPause) {
        if (!isInitialised) {
            init();
        }

        PluginSettings.colorOnPause = colorOnPause;
    }

    public static void setColorOnUnpause(Integer colorOnUnpause) {
        if (!isInitialised) {
            init();
        }

        PluginSettings.colorOnUnpause = colorOnUnpause;
    }

    public static void setMessagePattern(String messagePattern) {
        if (!isInitialised) {
            init();
        }

        PluginSettings.messagePattern = messagePattern;
    }


    public static String getCurrentLocale() {
        if (!isInitialised) {
            init();
        }

        return currentLocale;
    }

    public static String getChannel() {
        if (!isInitialised) {
            init();
        }

        return channel;
    }

    public static String getToken() {
        if (!isInitialised) {
            init();
        }

        return token;
    }

    public static String getUrl() {
        if (!isInitialised) {
            init();
        }

        return url;
    }

    public static Integer getColorOnJoin() {
        if (!isInitialised) {
            init();
        }

        return colorOnJoin;
    }

    public static Integer getColorOnLeave() {
        if (!isInitialised) {
            init();
        }

        return colorOnLeave;
    }

    public static Integer getColorOnGameStart() {
        if (!isInitialised) {
            init();
        }

        return colorOnGameStart;
    }

    public static Integer getColorOnWaveStart() {
        if (!isInitialised) {
            init();
        }

        return colorOnWaveStart;
    }

    public static Integer getColorOnGameOver() {
        if (!isInitialised) {
            init();
        }

        return colorOnGameOver;
    }

    public static Integer getColorOnPause() {
        if (!isInitialised) {
            init();
        }

        return colorOnPause;
    }

    public static Integer getColorOnUnpause() {
        if (!isInitialised) {
            init();
        }

        return colorOnUnpause;
    }

    public static String getMessagePattern() {
        if (!isInitialised) {
            init();
        }

        return messagePattern;
    }

    public static void load() {
        try {
            JsonValue full = new JsonReader().parse(new Fi(settingsPath));

            JsonValue value = full.get("main");

            currentLocale = value.getString("locale");
            token = value.getString("token");
            channel = value.getString("channel-id");
            url = value.getString("webhook-url");

            value = full.get("appearance").get("discord").get("embeds");

            colorOnJoin = Integer.parseInt(value.getString("on_join", "820000"), 16);
            colorOnLeave = Integer.parseInt(value.getString("on_leave", "738fff"), 16);

            colorOnGameStart = Integer.parseInt(value.getString("on_game_start", "73ff7c"), 16);
            colorOnWaveStart = Integer.parseInt(value.getString("on_wave_start", "516952"), 16);
            colorOnGameOver = Integer.parseInt(value.getString("on_game_over", "ff5959"), 16);
            colorOnPause = Integer.parseInt(value.getString("on_pause", "111111"), 16);
            colorOnUnpause = Integer.parseInt(value.getString("on_unpause", "211111"), 16);

            value = full.get("appearance").get("mindustry");

            messagePattern = value.getString("message_pattern", "[blue][D][[[cyan]%s[cyan][blue]]:[blue][white] %s");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}