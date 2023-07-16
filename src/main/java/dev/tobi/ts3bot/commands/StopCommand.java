package dev.tobi.ts3bot.commands;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import dev.tobi.ts3bot.main.TS3Bot;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class StopCommand extends Command{
    public StopCommand(String name) {
        super(name);
    }

    @Override
    public void execute(Client sender, String[] args) throws IOException, CommandException, InterruptedException, TimeoutException {
        if(args.length == 1) {
            if(TS3Bot.getInstance().getMusicPlayer().isRunning()) {
                TS3Bot.getInstance().getMusicPlayer().stopMusicPlayer();
                TS3Bot.getInstance().getQueue().clear();
                TS3Bot.getInstance().getClient().sendChannelMessage(sender.getChannelId(), "Der MusikPlayer wurde gestoppt.");
                System.out.println("Der MusikPlayer wurde von " + sender.getNickname() + " gestoppt.");
            }
        }else{
            TS3Bot.getInstance().getClient().sendChannelMessage(sender.getChannelId(), "Bitte verwende !stop um den MusikPlayer zu stoppen.");
        }
    }
}
