package dev.tobi.ts3bot.commands;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import dev.tobi.ts3bot.main.TS3Bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class KeineAhnungCommand extends Command{
    public KeineAhnungCommand(String name) {
        super(name);
    }

    @Override
    public void execute(Client sender, String[] args) throws IOException, CommandException, InterruptedException, TimeoutException {
        if(args.length == 1) {
            TS3Bot.getInstance().getClient().joinChannel(sender.getChannelId(), "");
            List<Client> clientsInChannel = new ArrayList<>();

            for (Client client : TS3Bot.getInstance().getClient().listClients()) {
                if (client.getChannelId() == sender.getChannelId()) {
                    clientsInChannel.add(client);
                }
            }

            Collections.shuffle(clientsInChannel);
            if (clientsInChannel.get(0).getId() != TS3Bot.getInstance().getClient().getClientId()) {

            }
        }else{
            TS3Bot.getInstance().getClient().sendPrivateMessage(sender.getId(), "Bitte verwende !keineAhnung.");
        }
    }
}
