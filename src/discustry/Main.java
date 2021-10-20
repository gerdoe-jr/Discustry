package discustry;

import arc.Core;
import arc.Events;
import arc.util.*;

import discustry.struct.WaveInfo;
import mindustry.*;
import mindustry.core.GameState;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.mod.*;

import java.util.Collection;

public class Main extends Plugin {
    private JDAListener discord;
    private boolean previousServerPauseState = false;

    @Override
    public void init() {
        this.reloadDiscord();

        Core.app.post(() -> {
            boolean paused = Vars.state.serverPaused;

            if(previousServerPauseState != paused) {
                this.discord.sendOnPause(paused);
                previousServerPauseState = paused;
            }
        });

        // player events
        Events.on(EventType.PlayerJoin.class, event -> {
            this.discord.sendOnJoin(event.player.name);
        });

        Events.on(EventType.PlayerLeave.class, event -> {
            this.discord.sendOnLeave(event.player.name);
        });

        // game events
        Events.on(EventType.GameOverEvent.class, event -> {
            if(((Collection<?>)(Vars.net.getConnections())).size() == 0) {
                return;
            }

            // TODO: add more information about lasted game (e.g buildings amount)
            this.discord.sendOnGameOver();

            System.out.println(Vars.state.map.name() + " " + Vars.state.map.author());
        });

        Events.on(EventType.WaveEvent.class, event -> {
            if(((Collection<?>)(Vars.net.getConnections())).size() == 0) {
                return;
            }

            WaveInfo info = new WaveInfo();
            info.waveNumber = Vars.state.wave - 1;
            info.waveTime = Vars.state.wavetime;
            info.spawnGroups = Vars.waves.get();

            this.discord.sendOnWaveStart(info);

            // TODO: add more information about started wave (e.g unit amount, unit type and so on)
            // this.discord.sendOnWaveStart(Vars.state.wave - 1, Vars.state.wavetime);
        });

        Events.on(EventType.WorldLoadEvent.class, event -> {
            if(((Collection<?>)(Vars.net.getConnections())).size() == 0) {
                return;
            }

            this.discord.sendOnNewGame(Vars.state.map.name(), Vars.state.map.author());
        });

        Events.on(EventType.StateChangeEvent.class, event -> {
            if(event.from.equals(GameState.State.paused) && event.to.equals(GameState.State.playing))
                this.discord.sendOnPause(event.to.equals(GameState.State.paused));

            System.out.println(event.from.toString() + event.to.toString());
        });

        // translate mindustry chat to discord chat
        Vars.netServer.admins.addChatFilter((player, text) -> {
            this.discord.sendMessage(player.name, text);

            return text;
        });
    }

    @Override
    public void registerServerCommands(CommandHandler handler) {
        handler.register("discustry", "<command> [argument]","Discustry settings", arg -> {
            if(arg.length > 0) {
                switch (arg[0]) {
                    case "set":
                        if(arg.length > 2) {
                            switch (arg[1]) {
                                case "token":
                                    PluginSettings.setToken(arg[2]);
                                    break;
                                case "channel-id":
                                    PluginSettings.setChannel(arg[2]);
                                    break;
                                case "webhook-url":
                                    PluginSettings.setUrl(arg[2]);
                                    break;
                            }
                        }
                        break; // if you want to reload instance on every change, delete this line

                    case "settings":
                        if(arg.length > 1) {
                            switch (arg[1]) {
                                case "load":
                                    PluginSettings.load();
                                    break;
                            }
                        }
                    case "reload":
                        this.reloadDiscord();
                        break;
                }
            }
        });
    }

    private void reloadDiscord() {
        this.discord = new JDAListener(this);
    }

    public void sendMessage(String from, String message) {
        Log.info("[D] @: @", from, message);
        Call.sendMessage(String.format(PluginSettings.getMessagePattern(), from, message));
    }
}
