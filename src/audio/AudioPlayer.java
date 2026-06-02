package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

    public static int MENU = 0;
    public static int STAGE = 1;


    public static int HURT = 0;
    public static int KILLED = 1;
    public static int CLICK = 2;
    public static int SHOOT = 3;

    private Clip[] songs, effects;
    private int currentSongId;
    private float volume = 0.5f;
    private boolean songMute, effectMute;

    public AudioPlayer() {
        loadSongs();
        loadEffects();
        playSong(MENU);
    }

    private void loadSongs() {
        String[] names = { "menu", "stage" };
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++)
            songs[i] = getClip(names[i]);
    }

    private void loadEffects() {
        String[] effectNames = { "damaged","killed","click","shoot","spawn","place"};
        effects = new Clip[effectNames.length];
        for (int i = 0; i < effects.length; i++)
            effects[i] = getClip(effectNames[i]);

        updateEffectsVolume();

    }

    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

            e.printStackTrace();
        }

        return null;

    }

    public void setVolume(float volume) {
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }

    public void stopSong() {
        if (songs[currentSongId].isActive())
            songs[currentSongId].stop();
    }

    public void setLevelSong() {
            playSong(STAGE);
    }



    public void playEffect(int effect) {
        if (effects[effect].getMicrosecondPosition() > 0)
            effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    public void playSong(int song) {
        stopSong();
        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void toggleSongMute() {
        this.songMute = !songMute;
        for (Clip c : songs) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
        if (!effectMute)
            playEffect(CLICK);
    }

    public void toggleEffectMute() {
        this.effectMute = !effectMute;
        for (Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }

    }

//    private void updateSongVolume() {
//
//        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
//        float range = gainControl.getMaximum() - gainControl.getMinimum();
//        float gain = (range * volume) + gainControl.getMinimum();
//        gainControl.setValue(gain);
//
//    }
    private void updateSongVolume() {
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);

        float min = gainControl.getMinimum(); // thường là -80.0
        float max = gainControl.getMaximum(); // thường là 6.0

        // Giả sử volume = 0.0 -> min dB, 1.0 -> max dB
        float dB;
        if (volume == 0f)
            dB = min;
        else
            dB = (float) (Math.log10(volume) * 20.0); // Chuyển từ linear -> dB

        dB = Math.max(min, Math.min(dB, max)); // Clamp
        gainControl.setValue(dB);
    }


//    private void updateEffectsVolume() {
//        for (Clip c : effects) {
//            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
//            float range = gainControl.getMaximum() - gainControl.getMinimum();
//            float gain = (range * volume) + gainControl.getMinimum();
//            gainControl.setValue(gain);
//        }
//    }
    private void updateEffectsVolume() {
        for (Clip c : effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();

            float dB;
            if (volume == 0f)
                dB = min;
            else
                dB = (float)(Math.log10(volume) * 20.0);

            dB = Math.max(min, Math.min(dB, max));
            gainControl.setValue(dB);
        }
    }


}
