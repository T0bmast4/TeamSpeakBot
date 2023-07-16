package dev.tobi.ts3bot.commands;

import com.github.manevolent.ts3j.api.Channel;
import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import dev.tobi.ts3bot.main.TS3Bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class GuteFrageCommand extends Command{
    public GuteFrageCommand(String name) {
        super(name);
    }

    @Override
    public void execute(Client sender, String[] args) throws IOException, CommandException, InterruptedException, TimeoutException {
        if (args.length == 1) {
            for (Channel channel : TS3Bot.getInstance().getClient().listChannels()) {
                List<Channel> channels = new ArrayList<>();
                channels.add(channel);
            }
        }
    }
}
