package discustry;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;

import discustry.struct.WaveInfo;
import mindustry.game.SpawnGroup;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JDAListener extends ListenerAdapter {
    private final Main mindustry;
    private JDA instance;
    private WebhookClient client;

    JDAListener(Main plugin) {
        this.mindustry = plugin;

        try {
            this.instance = JDABuilder.createDefault(PluginSettings.getToken()).build();
            this.instance.addEventListener(this);

            this.client = WebhookClient.withUrl(PluginSettings.getUrl());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot() || !event.getChannel().getId().equals(PluginSettings.getChannel()))
            return;

        this.mindustry.sendMessage(event.getAuthor().getName(), event.getMessage().getContentDisplay());
    }

    public void sendMessage(String from, String message) {
        WebhookMessage msg = new WebhookMessageBuilder()
                .setUsername(from).setContent(message)
                .setAvatarUrl("https://avatars.githubusercontent.com/u/37991703?s=200&v=4") // use this avatar
                .build();

        client.send(msg);
    }

    public void sendOnJoin(String who) {
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setTitle(new WebhookEmbed.EmbedTitle(String.format(PluginLocales.getLocale().get("on_join"), who), null))
                .setColor(PluginSettings.getColorOnJoin())
                .build();
        this.client.send(embed);
    }

    public void sendOnLeave(String who) {
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setTitle(new WebhookEmbed.EmbedTitle(String.format(PluginLocales.getLocale().get("on_leave"), who), null))
                .setColor(PluginSettings.getColorOnLeave())
                .build();
        this.client.send(embed);
    }

    public void sendOnWaveStart(WaveInfo info) {
        String finalString = "";

        for(SpawnGroup group : info.spawnGroups) {
            finalString.concat(String.format("**%s:** %d\n", group.type.name.toUpperCase(), group.unitAmount));
        }

        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setTitle(new WebhookEmbed.EmbedTitle(String.format(PluginLocales.getLocale().get("on_wave_start"), info.waveNumber) + " " + info.waveTime, null))
                .setDescription(finalString)
                .setColor(PluginSettings.getColorOnWaveStart())
                .build();

        this.client.send(embed);
    }

    public void sendOnNewGame(String mapName, String mapAuthor) {
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setTitle(new WebhookEmbed.EmbedTitle(String.format(PluginLocales.getLocale().get("on_game_start"), mapName, mapAuthor), null))
                .setColor(PluginSettings.getColorOnGameStart())
                .build();
        this.client.send(embed);
    }

    public void sendOnGameOver() {
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setTitle(new WebhookEmbed.EmbedTitle(PluginLocales.getLocale().get("on_game_over"), null))
                .setColor(PluginSettings.getColorOnGameOver())
                .build();
        this.client.send(embed);
    }

    public void sendOnPause(boolean paused) {
        String text = paused ? PluginLocales.getLocale().get("on_pause") : PluginLocales.getLocale().get("on_unpause");
        Integer color = paused ? PluginSettings.getColorOnPause() : PluginSettings.getColorOnUnpause();

        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setTitle(new WebhookEmbed.EmbedTitle(new String(text), null))
                .setColor(color)
                .build();

        this.client.send(embed);
    }
}

