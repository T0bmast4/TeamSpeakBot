package dev.tobi.ts3bot.commands;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import dev.tobi.ts3bot.audio.MusicPlayer;
import dev.tobi.ts3bot.main.TS3Bot;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class PlayCommand extends Command {

    public PlayCommand(String name) {
        super(name);
    }

    @Override
    public void execute(Client sender, String[] args) throws IOException, CommandException, InterruptedException, TimeoutException {
        if(args.length >= 2) {
            MusicPlayer musicPlayer = TS3Bot.getInstance().getMusicPlayer();
            String songName = String.join(" ",Arrays.stream(args).toList().subList(1,args.length));

            if(musicPlayer.songExists(songName)) {
                if(musicPlayer.isRunning()) {
                    TS3Bot.getInstance().getQueue().add(songName);
                    TS3Bot.getInstance().getClient().sendChannelMessage(sender.getChannelId(),
                            "Der Song " + songName + " von " + sender.getNickname() + " wurde zur Warteschlange hinzugefügt \n" +
                            "(" + TS3Bot.getInstance().getQueue().size() + " Song(s) in Queue)");
                    System.out.println("Nächster Song in Queue: " + songName + " (" + TS3Bot.getInstance().getQueue().size() + ". Platz in Queue)");
                    return;
                }

                musicPlayer.playSong(songName);
                TS3Bot.getInstance().getClient().sendChannelMessage(sender.getChannelId(),
                        "Der Song " + songName + " eingereicht von" + sender.getNickname() + " wird nun abgespielt.");

            }else{
                TS3Bot.getInstance().getClient().sendChannelMessage(sender.getChannelId(), "Dieser Song existiert nicht!!");
            }
        }else{
            TS3Bot.getInstance().getClient().sendChannelMessage(sender.getChannelId(), "Bitte gib einen Song an!");
        }
    }
}
