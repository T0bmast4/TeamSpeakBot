package dev.tobi.ts3bot.commands;


import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class YTCommand extends Command{
    public YTCommand(String name) {
        super(name);
    }

    @Override
    public void execute(Client sender, String[] args) throws IOException, CommandException, InterruptedException, TimeoutException {
        if(args.length >= 3) {

        }
    }

    private static void convertVideoToMp3(File videoFile, File mp3File) {

    }
}
