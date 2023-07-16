package dev.tobi.ts3bot.main;

import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.identity.LocalIdentity;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import com.sapher.youtubedl.YoutubeDLException;
import dev.tobi.ts3bot.audio.MusicPlayer;
import dev.tobi.ts3bot.commands.*;
import dev.tobi.ts3bot.config.Configs;
import dev.tobi.ts3bot.listeners.FunListeners;
import dev.tobi.ts3bot.listeners.TextCommands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

public class TS3Bot {

    private LocalTeamspeakClientSocket client;
    private final List<Command> commands = new ArrayList<>();
    private Queue<String> queue;

    private MusicPlayer musicPlayer;
    private Configs config;
    private static TS3Bot _instance;

    public static void main(String[] args) throws YoutubeDLException {
        TS3Bot ts3Bot = new TS3Bot();
        ts3Bot.start();
    }


    public void start() throws YoutubeDLException {
        _instance = this;
        config = new Configs();
        musicPlayer = new MusicPlayer();
        queue = new PriorityQueue<>();

        try {
            client = new LocalTeamspeakClientSocket();

            LocalIdentity identity;
            File file = Path.of("identity.properties").toFile();
            if(!file.exists()) {
                file.createNewFile();

                identity = LocalIdentity.generateNew(24);
                identity.save(file);
                System.out.println("LocalIdentity created.");
            }else{
                identity = LocalIdentity.read(file);
                System.out.println("LocalIdentity loaded.");
            }


            client.setIdentity(identity);
            client.setNickname("sai-boT");
            client.addListener(new FunListeners());
            client.addListener(new TextCommands());
            registerCommands();

            client.connect("38.242.141.75", 10000L);



            client.subscribeAll();

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        musicPlayer = new MusicPlayer();
        musicPlayer.playSong(musicPlayer.getRandomSong());
        YouTubeDownloader.downloadVideo("https://www.youtube.com/watch?v=BB5eqc1ZyK0");
    }

    public void stop() {
        _instance = null;
        this.stop();
    }

    private void registerCommands() {
        getCommands().add(new PlayCommand("!play"));
        getCommands().add(new VolumeCommand("!volume"));
        getCommands().add(new StopCommand("!stop"));
        getCommands().add(new LokiCommand("!loki"));
        getCommands().add(new YTCommand("!yt"));
        getCommands().add(new IgnoreCommand("!ignore"));
    }

    public LocalTeamspeakClientSocket getClient() {
        return client;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Queue<String> getQueue() {
        return queue;
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public Configs getConfig() {
        return config;
    }

    public static TS3Bot getInstance() {
        return _instance;
    }
}

