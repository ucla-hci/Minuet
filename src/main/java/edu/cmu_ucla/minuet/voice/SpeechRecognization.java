package edu.cmu_ucla.minuet.voice;
// Imports the Google Cloud client library
import com.google.cloud.speech.v1p1beta1.*;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpeechRecognization {

    public static List<SpeechRecognitionResult> translate(String filepath) throws IOException {
        try (SpeechClient speechClient = SpeechClient.create()) {

            Path path = Paths.get(filepath);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);
            List<String> corpus = new ArrayList<>(Arrays.asList("this","turn on","that","turn off"));
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .setModel("default")
//                    .setSpeechContexts(5,SpeechContext.newBuilder().setPhrases(0,"this"))
                    .addSpeechContexts(SpeechContext.newBuilder().addPhrases("this").addAllPhrases(corpus))
//                    .setMetadata(RecognitionMetadata.newBuilder().setInteractionType(VOICE_COMMAND).build())
                    .setEnableWordTimeOffsets(true)
                    .setUseEnhanced(true)
                    .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            RecognizeResponse response = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result : results) {
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                System.out.printf("Transcription: %s%n", alternative.getTranscript());
                for (WordInfo wordInfo : alternative.getWordsList()) {
                    System.out.println(wordInfo.getWord());
                    System.out.printf("\t%s.%s sec - %s.%s sec\n",
                            wordInfo.getStartTime().getSeconds(),
                            wordInfo.getStartTime().getNanos() / 100000000,
                            wordInfo.getEndTime().getSeconds(),
                            wordInfo.getEndTime().getNanos() / 100000000);
                }

            }
            return results;
        }
    }


}
