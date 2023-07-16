package dev.tobi.ts3bot.commands;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import dev.tobi.ts3bot.main.TS3Bot;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class IgnoreCommand extends Command{
    public IgnoreCommand(String name) {
        super(name);
    }

    @Override
    public void execute(Client sender, String[] args) throws IOException, CommandException, InterruptedException, TimeoutException {
        if(args.length == 1) {
            Properties config = TS3Bot.getInstance().getConfig().getProperties();
            String ignoreUUIDs = config.getProperty("ignoreUUIDs");
            if (ignoreUUIDs != null && ignoreUUIDs.contains(String.valueOf(sender.getUniqueIdentifier()))) {
                TS3Bot.getInstance().getClient().sendPrivateMessage(sender.getId(), "Ab jetzt werde ich dich NICHT mehr bei jedem Spaß-Command ignorieren.");

                String nameToRemove = sender.getUniqueIdentifier() + ", ";

                ignoreUUIDs = ignoreUUIDs.replace(nameToRemove, "");
                config.setProperty("ignoreUUIDs", ignoreUUIDs);
                TS3Bot.getInstance().getConfig().writeConfigToFile(config);
            } else {
                TS3Bot.getInstance().getClient().sendPrivateMessage(sender.getId(), "Ab jetzt werde ich dich bei jedem Spaß-Command ignorieren.");

                String nameToAdd = sender.getUniqueIdentifier() + ", ";

                String newIgnoreUUIDs = "";

                if (ignoreUUIDs != null) {
                    newIgnoreUUIDs = ignoreUUIDs + nameToAdd;
                } else {
                    newIgnoreUUIDs = nameToAdd;
                }

                config.setProperty("ignoreUUIDs", newIgnoreUUIDs);
                TS3Bot.getInstance().getConfig().writeConfigToFile(config);
            }
        } else {
            TS3Bot.getInstance().getClient().sendPrivateMessage(sender.getId(), "Bitte verwende !ignore.");
        }
    }
}
