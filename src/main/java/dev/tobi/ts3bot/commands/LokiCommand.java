package dev.tobi.ts3bot.commands;

import com.github.manevolent.ts3j.api.Channel;
import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.api.ServerGroup;
import com.github.manevolent.ts3j.command.CommandException;
import dev.tobi.ts3bot.config.Configs;
import dev.tobi.ts3bot.main.TS3Bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LokiCommand extends Command{

    public LokiCommand(String name) {
        super(name);
    }

    @Override
    public void execute(Client sender, String[] args) throws IOException, CommandException, InterruptedException, TimeoutException {
        if (args.length == 1) {
            TS3Bot.getInstance().getMusicPlayer().stopMusicPlayer();
            TimeUnit.SECONDS.sleep(1);
            TS3Bot.getInstance().getMusicPlayer().playSong("marcell davis");
            TS3Bot.getInstance().getClient().joinChannel(sender.getChannelId(), "");
            List<Client> clientsInChannel = new ArrayList<>();

            for (Client client : TS3Bot.getInstance().getClient().listClients()) {
                if (client.getChannelId() == sender.getChannelId()) {
                    for (String ignoreUUIDs : TS3Bot.getInstance().getConfig().getProperties().getProperty("ignoreUUIDs").split(", ")) {
                        System.out.println(ignoreUUIDs);
                        if (client.getUniqueIdentifier() != ignoreUUIDs) {
                            clientsInChannel.add(client);
                        }
                    }
                }
            }

            TimeUnit.SECONDS.sleep(6);
            Collections.shuffle(clientsInChannel);
            for(int i = 0; i < clientsInChannel.size(); i++)
                System.out.println(clientsInChannel.get(i).getNickname());
            try {
                TS3Bot.getInstance().getClient().banClient(clientsInChannel.get(0).getId(), 1, "Zeitüberschreitung, kauf dir besseres Internet von 1und1");
            } catch (ExecutionException e) {

            }
        }else{
            TS3Bot.getInstance().getClient().sendPrivateMessage(sender.getId(), "Bitte verwende !loki.");
        }
    }
}
