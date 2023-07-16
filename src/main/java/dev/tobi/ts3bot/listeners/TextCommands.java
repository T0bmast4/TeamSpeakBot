package dev.tobi.ts3bot.listeners;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.event.TS3Listener;
import com.github.manevolent.ts3j.event.TextMessageEvent;
import dev.tobi.ts3bot.main.TS3Bot;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class TextCommands implements TS3Listener {

    @Override
    public void onTextMessage(TextMessageEvent textMessageEvent) {
        try {
            Client invoker = TS3Bot.getInstance().getClient().getClientInfo(textMessageEvent.getInvokerId());
            if (textMessageEvent.getMessage().startsWith("!")) {
                parse(invoker, textMessageEvent.getMessage());
            }

            switch (textMessageEvent.getMessage().toLowerCase()) {

                case "come", "komm", "put put" -> {
                    TS3Bot.getInstance().getClient().joinChannel(invoker.getChannelId(), "");
                }
            }


        } catch (IOException | CommandException | TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void parse(Client executor, @NotNull String commandline) {
        String cmd = commandline.split(" ")[0];
        TS3Bot.getInstance().getCommands().forEach(command -> {
            if (command.getName().equals(cmd)) {
                try {
                    command.execute(executor, commandline.split(" "));
                } catch (IOException | CommandException | InterruptedException | TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
