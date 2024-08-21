package client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioController {
	
	AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
    TargetDataLine microphone;
    AudioInputStream audioInputStream;
    SourceDataLine sourceDataLine;
	ByteArrayOutputStream out;
	byte[] data;
	int CHUNK_SIZE = 1024;
	int numBytesRead;
	
	public AudioController() {
		try {
			microphone = AudioSystem.getTargetDataLine(format);
			out = new ByteArrayOutputStream();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            CHUNK_SIZE = 1024;
            data = new byte[microphone.getBufferSize() / 5];
            microphone.start();
		} catch (Exception e) {
	
		}
	}
	
	public static void playAudio(byte[] audioData) throws LineUnavailableException {
		System.out.println("data " + audioData);
		AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
		InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
        AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, format, audioData.length / format.getFrameSize());
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine sourceDataLine  = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open(format);
        sourceDataLine.start();
        int cnt;
        byte tempBuffer[] = new byte[10000];
        try {
            while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                if (cnt > 0) {
                    sourceDataLine.write(tempBuffer, 0, cnt);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        sourceDataLine.drain();
        sourceDataLine.close();
	}
    
    
	
}
