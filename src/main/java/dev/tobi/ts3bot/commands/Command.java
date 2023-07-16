package dev.tobi.ts3bot.commands;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RequiredArgsConstructor
@Getter
public abstract class Command {
    private final String name;

    public abstract void execute(Client sender, String[] args) throws IOException, CommandException, InterruptedException, TimeoutException;
}