package edu.cmu_ucla.minuet.voice;

import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecordAudio {
    private List<SpeechRecognitionResult> results;

    private static final long RECORD_TIME = 3000;
    private String pathName = "src/resources/recordAndTrans.wav";
    // path of the wav file
    private File wavFile = new File(pathName);

    // format of audio file
    private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    // the line from which audio data is captured
    private TargetDataLine line;

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;

        return new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
    }

    private void start() {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();   // start capturing

            System.out.println("Start capturing...");

            AudioInputStream ais = new AudioInputStream(line);

            System.out.println("Start recording...");

            // start recording
            AudioSystem.write(ais, fileType, wavFile);

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Closes the target data line to finish capturing and recording
     */
    private void finish() {
        line.stop();
        line.close();
        System.out.println("Finished");
        System.out.println("Start speech to text:");
        List<SpeechRecognitionResult> results = new ArrayList<>();
        try {
            results = SpeechRecognization.translate(pathName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("end");
        this.results = results;
    }


    public List<SpeechRecognitionResult> getRecordResult() {

        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(RECORD_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                finish();


            }
        });

        stopper.start();
        start();
        try {
            stopper.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.results;
    }

}
