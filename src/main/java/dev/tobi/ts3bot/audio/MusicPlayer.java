package dev.tobi.ts3bot.audio;

import com.github.manevolent.ffmpeg4j.*;
import com.github.manevolent.ffmpeg4j.filter.audio.FFmpegAudioResampleFilter;
import com.github.manevolent.ffmpeg4j.source.FFmpegAudioSourceSubstream;
import com.github.manevolent.ffmpeg4j.stream.source.FFmpegSourceStream;
import dev.tobi.ts3bot.main.TS3Bot;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

import static dev.tobi.ts3bot.audio.TeamspeakFastMixerSink.AUDIO_FORMAT;

public class MusicPlayer {
    private double volume = 0.5D;;
    private int bitrate = 96000;

    private boolean isRunning = false;

    Queue<AudioFrame> frameQueue = new LinkedBlockingQueue<>();
    AudioFrame currentFrame = null;
    int frameOffset = 0;
    long wake = System.nanoTime();
    long delay = 150 * 1_000_000;
    long sleep;
    long start = System.nanoTime();

    private TeamspeakFastMixerSink sink;

    public void playSong(String songName) {
        if (songExists(songName)) {
            System.out.println("Nun wird: '" + songName + "' gespielt.");
            try {
                FFmpeg.register();
                FFmpegInput input = FFmpegIO.openInput(new File("music/" + songName + ".mp3"));
                FFmpegSourceStream stream = input.open(FFmpeg.getInputFormatByName("mp3"));
                FFmpegAudioSourceSubstream audioSourceSubstream =
                        (FFmpegAudioSourceSubstream) stream.registerStreams()
                                .stream()
                                .filter(x -> x.getMediaType() == MediaType.AUDIO)
                                .findFirst().orElse(null);

                if (audioSourceSubstream == null) throw new NullPointerException();

                // Create a sink
                sink = new TeamspeakFastMixerSink(
                        AUDIO_FORMAT,
                        (int) AUDIO_FORMAT.getSampleRate() * AUDIO_FORMAT.getChannels() * 4 /*4=32bit float*/,
                        new OpusParameters(
                                20,
                                bitrate,
                                10,
                                0,
                                false,
                                false,
                                true
                        )
                );

                TS3Bot.getInstance().getClient().setMicrophone(sink);
                if(sink.isRunning()) {
                    sink.stop();
                }
                sink.start();
                isRunning = true;
                int bufferSize = AUDIO_FORMAT.getChannels() * (int) AUDIO_FORMAT.getSampleRate(); // Just to keep it orderly
                FFmpegAudioResampleFilter resampleFilter = new FFmpegAudioResampleFilter(
                        audioSourceSubstream.getFormat(),
                        new AudioFormat(
                                (int) AUDIO_FORMAT.getSampleRate(),
                                AUDIO_FORMAT.getChannels(),
                                FFmpeg.guessFFMpegChannelLayout(AUDIO_FORMAT.getChannels())
                        ),
                        bufferSize
                );

                CompletableFuture.runAsync(() -> {
                    while (true) {
                        double volume = getVolume();
                        int available = sink.availableInput();
                        if (available > 0) {
                            if (currentFrame == null || frameOffset >= currentFrame.getLength()) {
                                if (frameQueue.peek() == null) {
                                    try {
                                        AudioFrame frame = audioSourceSubstream.next();
                                        for (int i = 0; i < frame.getLength(); i++)
                                            frame.getSamples()[i] *= volume;
                                        Collection<AudioFrame> frameList = resampleFilter.apply(frame);
                                        frameQueue.addAll(frameList);
                                    } catch (IOException ex) {
                                        // flush currentFrame
                                        break;
                                    }
                                }
                                if (frameQueue.size() == 0) continue;
                                currentFrame = frameQueue.remove();
                                frameOffset = 0;
                            }
                            int write = Math.min(currentFrame.getLength() - frameOffset, available);
                            sink.write(
                                    currentFrame.getSamples(),
                                    frameOffset,
                                    write
                            );
                            frameOffset += write;
                        }
                    /*wake += delay;
                    sleep = (wake - System.nanoTime()) / 1_000_000;
                    System.err.println(((double) (System.nanoTime() - start) / 1_000_000_000D) + " " + (sink.getPacketsSent() / 50D) + " " + sink.getUnderflows());

                   if (sleep > 0) Thread.sleep(sleep);

                     */

                    }
                    if(!TS3Bot.getInstance().getQueue().isEmpty()) {
                        playSong(TS3Bot.getInstance().getQueue().poll());
                    }else{
                        String rdm = getRandomSong();
                        TS3Bot.getInstance().getQueue().add(rdm);
                        playSong(rdm);
                    }
                });
                //sink.drain();

            } catch (IOException | FFmpegException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopMusicPlayer() {
        sink.stop();
        isRunning = false;
    }

    public boolean songExists(String songName) {
        File file = new File("music/" + songName + ".mp3");
        if(file.exists()) return true;

        return false;
    }

    public String getRandomSong() {
        File dir = new File("music/");
        File[] files = dir.listFiles();
        String[] fileArray = new String[files.length];

        for(int i = 0; i < files.length; i++) {
            int rdm = (int)(Math.random()* files.length);
            fileArray[i] = files[rdm].getName();
        }

        String songName = fileArray[0].replace(".mp3", "");
        if(songName.equals("Elotrix") || songName.equals("marcell davis")) {
            songName = getRandomSong();
        }
        return songName;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getVolume() {
        return volume;
    }

    public boolean isRunning() {
        return isRunning;
    }
}