package src.utils.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundManager implements Music.OnCompletionListener {
    public Float volume = 1f;
    public Float volumeMusic = 1f;
    public Float volumeSound = 1f;

    private Random random;
    private HashMap<String,ArrayList<Music>> soundTracks;
    private String currentSoundTrack;

    private Music currentMusic;
    private final ExecutorService musicThread;

    public SoundManager(){
        random = new Random();
        soundTracks = new HashMap<>();
        musicThread = Executors.newSingleThreadExecutor();
    }

    public void playSound(Sound sound, Float pitch){
        sound.play(volume * volumeSound, pitch, 0);
    }

    public synchronized void playMusic(Music music){
        music.setVolume(0);
        music.setOnCompletionListener(this);
        musicThread.submit(() -> {
            fadeOutMusic();
            currentMusic = music;
            music.play();

            while (music.getVolume() < volume * volumeMusic) {
                music.setVolume(music.getVolume() + 0.005f);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Gdx.app.log("SoundManager", "Error al iniciar la música");
                }
            }
        });
    }

    public synchronized void stopMusic(){
        musicThread.submit(() -> {
            fadeOutMusic();
        });
    }

    private void fadeOutMusic() {
        if (currentMusic != null) {
            while (currentMusic.getVolume() > 0) {
                currentMusic.setVolume(currentMusic.getVolume() - 0.005f);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Gdx.app.log("SoundManager", "Error al detrener la música");
                }
            }
            currentMusic.stop();
        }
    }

    public void addSoundTrack(String name){
        soundTracks.put(name,new ArrayList<>());
    }

    public void setSoundTracks(String name){
        currentSoundTrack = name;
        playSoundTrack();
    }

    public void addMusicToSoundTrack(Music music, String soundTrack){
        soundTracks.get(soundTrack).add(music);
    }

    private void playSoundTrack(){
        int select = random.nextInt(soundTracks.get(currentSoundTrack).size());
        playMusic(soundTracks.get(currentSoundTrack).get(select));
    }

    @Override
    public void onCompletion(Music music) {
        if (currentSoundTrack != null){
            playSoundTrack();
        }
    }

    public void dispose(){
        if (currentMusic != null) {
            currentMusic.stop();
        }
        musicThread.shutdown();
    }
}
