package edu.cmu_ucla.minuet.apiTest;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import java.io.IOException;

public class App {
    public static void demo() {

        // Target data line
        TargetDataLine line = null;
        AudioInputStream audio = null;

        // Capture Microphone Audio Data
        try {

            // Signed PCM AudioFormat with 16kHz, 16 bit sample size, mono
            int sampleRate = 16000;
            AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // Check if Microphone is Supported
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }

            // Get the target data line
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            // Audio Input Stream
            audio = new AudioInputStream(line);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // ------------------------ WHAT I NEED -----------------------------

        // Send audio from Microphone to Google Servers and return Text
        try (SpeechClient client = SpeechClient.create()) {

            ResponseObserver<StreamingRecognizeResponse> responseObserver =
                    new ResponseObserver<StreamingRecognizeResponse>() {

                        public void onStart(StreamController controller) {
                            // do nothing
                        }

                        public void onResponse(StreamingRecognizeResponse response) {
                            System.out.println(response);
                        }

                        public void onComplete() {}

                        public void onError(Throwable t) {
                            System.out.println(t);
                        }
                    };

            ClientStream<StreamingRecognizeRequest> clientStream =
                    client.streamingRecognizeCallable().splitCall(responseObserver);

            RecognitionConfig recConfig =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setLanguageCode("en-US")
                            .setSampleRateHertz(16000)
                            .build();
            StreamingRecognitionConfig config =
                    StreamingRecognitionConfig.newBuilder().setConfig(recConfig).build();

            StreamingRecognizeRequest request =
                    StreamingRecognizeRequest.newBuilder()
                            .setStreamingConfig(config)
                            .build(); // The first request in a streaming call has to be a config

            clientStream.send(request);

            while (true) {
                byte[] data = new byte[10];
                try {
                    audio.read(data);
                } catch (IOException e) {
                    System.out.println(e);
                }
                request =
                        StreamingRecognizeRequest.newBuilder()
                                .setAudioContent(ByteString.copyFrom(data))
                                .build();
                clientStream.send(request);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        demo();
    }
}
