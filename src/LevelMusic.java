import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class LevelMusic {

    Clip clip;

    public LevelMusic (String filename) {
        try {
            File file = new File(filename);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(file);

            clip = AudioSystem.getClip();
            clip.open(audioInput);

        } catch (Exception e) {
            System.out.println("Failed to load sound: " + filename);
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-50.0f);


            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

}
