package dev.tobi.ts3bot.commands;

import com.github.manevolent.ts3j.api.Channel;
import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import dev.tobi.ts3bot.audio.MusicPlayer;
import dev.tobi.ts3bot.main.TS3Bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GuteFrageCommand extends Command{
    public GuteFrageCommand(String name) {
        super(name);
    }

    @Override
    public void execute(Client sender, String[] args) throws IOException, CommandException, InterruptedException, TimeoutException {
        if (args.length == 1) {
            TS3Bot.getInstance().getMusicPlayer().stopMusicPlayer();
            TS3Bot.getInstance().getClient().joinChannel(sender.getChannelId(), "");
            TS3Bot.getInstance().getMusicPlayer().playSong("Elotrix");
            List<Client> clientsInChannel = new ArrayList<>();

            for (Client client : TS3Bot.getInstance().getClient().listClients()) {
                if (client.getChannelId() == sender.getChannelId()) {
                    if(client.getId() != TS3Bot.getInstance().getClient().getClientId()) {
                        for (String ignoreUUIDs : TS3Bot.getInstance().getConfig().getProperties().getProperty("ignoreUUIDs").split(", ")) {
                            if (client.getUniqueIdentifier() != ignoreUUIDs) {
                                clientsInChannel.add(client);
                            }
                        }
                    }
                }
            }
            List<Channel> channels = new ArrayList<>();
            for (Channel channel : TS3Bot.getInstance().getClient().listChannels()) {
                if(channel.isEmpty()) {
                    channels.add(channel);
                }
            }


            Collections.shuffle(clientsInChannel);
            Collections.shuffle(channels);
            for(int i = 0; i < clientsInChannel.size(); i++)
                System.out.println(clientsInChannel.get(i).getNickname());
            System.out.println(channels);
            TimeUnit.SECONDS.sleep(5);
            TS3Bot.getInstance().getClient().clientMove(clientsInChannel.get(0).getId(), channels.get(0).getId(), "");
            TS3Bot.getInstance().getClient().joinChannel(channels.get(0).getId(), "");
        }
    }
}
