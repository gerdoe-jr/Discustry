package discustry;

import arc.files.Fi;
import arc.files.ZipFi;
import arc.util.serialization.JsonReader;
import arc.util.serialization.JsonValue;

import java.util.*;

public class PluginLocales {
    final static private List<String> supportedLocales = Arrays.asList("en", "ua", "ru");
    final static private List<String> jsonNames = Arrays.asList("on_join", "on_leave", "on_game_start", "on_wave_start", "on_game_over");

    private static boolean isInitialised = false;

    public static Map<String, String> locale = new HashMap<>();

    public static Map<String, String> getLocale() {
        if(!isInitialised) {
            load();

            isInitialised = true;
        }

        return locale;
    }

    public static void load() {
        try {
            if (!supportedLocales.contains(PluginSettings.getCurrentLocale())) {
                PluginSettings.setCurrentLocale("en");
            }

            final Fi zip = new ZipFi(new Fi("config/mods/Discustry.jar"));

            final JsonValue value = new JsonReader().parse(zip
                            .child("translations")
                            .child(String.format("%s.json", PluginSettings.getCurrentLocale())));

            if(!locale.isEmpty()) {
                locale.clear();
            }

            for (String name : jsonNames) {
                locale.put(name, value.getString(name));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
