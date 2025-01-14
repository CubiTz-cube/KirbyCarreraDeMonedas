package src.utils.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import src.main.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundManager implements Music.OnCompletionListener {
    private Float volume = 1f;
    private Float volumeMusic = 1f;
    private Float volumeSound = 1f;

    private final Random random;
    private final HashMap<Main.SoundTrackType,ArrayList<Music>> soundTracks;
    private Main.SoundTrackType currentSoundTrack;

    private Music currentMusic;
    private final ExecutorService musicThread;

    public void setVolume(Float volume) {
        this.volume = volume;
        if (currentMusic != null) {
            currentMusic.setVolume(volume * volumeMusic);
        }
    }

    public Float getVolume() {
        return volume;
    }

    public void setVolumeMusic(Float volumeMusic) {
        this.volumeMusic = volumeMusic;
        if (currentMusic != null) {
            currentMusic.setVolume(volume * volumeMusic);
        }
    }

    public Float getVolumeMusic() {
        return volumeMusic;
    }

    public void setVolumeSound(Float volumeSound) {
        this.volumeSound = volumeSound;
    }

    public Float getVolumeSound() {
        return volumeSound;
    }

    public SoundManager(){
        random = new Random();
        soundTracks = new HashMap<>();
        musicThread = Executors.newSingleThreadExecutor();
    }

    public void playSound(Sound sound){
        sound.play(volume * volumeSound, 1f, 0);
    }
    public void playSound(Sound sound, Float pitch){
        sound.play(volume * volumeSound, pitch, 0);
    }
    public void playSound(Sound sound, Float pitch, Float volumeSound){
        sound.play(volumeSound * volume * volumeSound, pitch, 0);
    }

    public void playMusic(Music music){
        music.setVolume(0);
        music.setOnCompletionListener(this);
        musicThread.submit(() -> {
            fadeOutMusic();
            currentMusic = music;
            Gdx.app.postRunnable(() -> {
                music.play();
                System.out.println("Music started");
            });

            while (music.getVolume() < volume * volumeMusic) {
                float newVolume = music.getVolume() + 0.005f;
                if (newVolume > volume * volumeMusic) newVolume = volume * volumeMusic;
                music.setVolume(newVolume);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Gdx.app.log("SoundManager", "Error al iniciar la música");
                }
            }
        });
    }

    public synchronized void stopMusic(){
        musicThread.submit(this::fadeOutMusic);
    }

    private void fadeOutMusic() {
        if (currentMusic == null) return;
        while (currentMusic.getVolume() > 0) {
            float newVolume = currentMusic.getVolume() - 0.005f;
            if (newVolume < 0) newVolume = 0;
            currentMusic.setVolume(newVolume);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Gdx.app.log("SoundManager", "Error al detener la música");
            }
        }
        currentMusic.stop();
    }

    public void addSoundTrack(Main.SoundTrackType type){
        soundTracks.put(type,new ArrayList<>());
    }

    public void setSoundTracks(Main.SoundTrackType type){
        currentSoundTrack = type;
        playSoundTrack();
    }

    public void addMusicToSoundTrack(Music music, Main.SoundTrackType soundTrack){
        soundTracks.get(soundTrack).add(music);
    }

    private void playSoundTrack(){
        int select = random.nextInt(soundTracks.get(currentSoundTrack).size());
        playMusic(soundTracks.get(currentSoundTrack).get(select));
    }

    @Override
    public void onCompletion(Music music) {
        playSoundTrack();
    }

    public void dispose(){
        if (currentMusic != null) {
            currentMusic.stop();
        }
        musicThread.shutdown();
    }
}
