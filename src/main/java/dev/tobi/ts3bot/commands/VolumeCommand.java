package dev.tobi.ts3bot.commands;


import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import dev.tobi.ts3bot.audio.MusicPlayer;
import dev.tobi.ts3bot.main.TS3Bot;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class VolumeCommand extends Command{

    public VolumeCommand(String name) {
        super(name);
    }

    @Override
    public void execute(Client sender, String[] args) throws IOException, CommandException, InterruptedException, TimeoutException {
        if(args.length == 2) {
            MusicPlayer musicPlayer = TS3Bot.getInstance().getMusicPlayer();
            if(args[1].equalsIgnoreCase("up")) {
                if(musicPlayer.getVolume() >= 1.0D){
                    TS3Bot.getInstance().getClient().sendChannelMessage(sender.getChannelId(), "Volumen hat das Maximum erreicht.");
                } else {
                    musicPlayer.setVolume((musicPlayer.getVolume() + 0.1D));
                    TS3Bot.getInstance().getClient().sendChannelMessage(sender.getChannelId(),
                            "Volume wurde von " + sender.getNickname() + " erfolgreich auf " + Math.round((musicPlayer.getVolume() + 10.0D) * 10.0) / 10.0 + " gesetzt.");
                }
            }else if(args[1].equalsIgnoreCase("down")){
                if(Math.round(musicPlayer.getVolume()) <= 0.1D){
                    TS3Bot.getInstance().getClient().sendChannelMessage(sender.getChannelId(), "Volumen hat das Minimum erreicht.");
                } else {
                    musicPlayer.setVolume((musicPlayer.getVolume() - 0.1D));
                    TS3Bot.getInstance().getClient().sendChannelMessage(sender.getChannelId(),
                            "Volume wurde von " + sender.getNickname() + " erfolgreich auf " + musicPlayer.getVolume() + " gesetzt.");
                }
            }
        }else{
            TS3Bot.getInstance().getClient().sendChannelMessage(sender.getChannelId(), "!volume <up|down>");
        }
    }
}
