package dev.tobi.ts3bot.listeners;

import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.event.TS3Listener;
import com.github.manevolent.ts3j.event.TextMessageEvent;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import dev.tobi.ts3bot.main.TS3Bot;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class FunListeners implements TS3Listener {


    @Override
    public void onTextMessage(TextMessageEvent textMessageEvent) {
        LocalTeamspeakClientSocket client = TS3Bot.getInstance().getClient();
        if (textMessageEvent.getInvokerId() == client.getClientId()) return;
        try {
            switch (textMessageEvent.getMessage().toLowerCase()) {

                case "echo" -> {
                    client.sendPrivateMessage(textMessageEvent.getInvokerId(), "Echo!");
                    client.clientPoke(textMessageEvent.getInvokerId(), "Echo!");
                    client.joinChannel(client.getClientInfo(textMessageEvent.getInvokerId()).getChannelId(), "");
                }


                case "hallo" -> {
                    client.sendPrivateMessage(textMessageEvent.getInvokerId(), "Hallo, ich bin " + client.getNickname() + ". Ich wurde von Tobi/T0bmast4 entwickelt, um diesen Teamspeak mit Musik zu unterhalten...");
                }
            }

            if(textMessageEvent.getMessage().toLowerCase().contains("wer bist du")) {
                client.sendPrivateMessage(textMessageEvent.getInvokerId(), "Ich bin Jack.");
            }else if(textMessageEvent.getMessage().toLowerCase().contains("was bist du")) {
                client.sendPrivateMessage(textMessageEvent.getInvokerId(), "Ich bin eine KI, welche von Amazon beauftragt wurde diesen Teamspeak auf ungewöhnliche Aktivitäten zu überprüfen und aufzunehmen (während ihr alle Musik hört hahahahahahHa).");
            }
        } catch (IOException | TimeoutException | InterruptedException | CommandException e) {

        }
    }
}
