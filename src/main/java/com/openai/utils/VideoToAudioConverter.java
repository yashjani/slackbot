package com.openai.utils;

import java.io.IOException;

public class VideoToAudioConverter {

    public static void convertMp4ToMp3(String inputFilePath, String outputFilePath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg", "-i", inputFilePath, "-q:a", "0", "-map", "a", outputFilePath);

        Process process = processBuilder.start();
        process.waitFor();
    }
}
