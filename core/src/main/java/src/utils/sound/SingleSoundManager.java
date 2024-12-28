package src.utils.sound;

public class SingleSoundManager {
    private static SoundManager instance = null;

    private SingleSoundManager() {
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
}
