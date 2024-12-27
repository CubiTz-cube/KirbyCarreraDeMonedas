package src.utils;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Queue;

public class SoundPicthUp implements Sound {
    private final Float playInterval;

    private final Sound sound;
    private Float pitch = 1.0f;
    private final Queue<Sound> soundQueue = new Queue<>();
    private Float resetTime;
    private Float timer = 0.0f;

    public SoundPicthUp(Sound sound, Float playInterval, Float resetTime) {
        this.sound = sound;
        this.playInterval = playInterval;
        this.resetTime = resetTime;
    }

    public void update(float delta) {
        timer += delta;
        resetTime += delta;
        if (timer >= playInterval && soundQueue.size > 0) {
            Sound nextSound = soundQueue.removeFirst();
            nextSound.play(1f, pitch, 0.0f);
            pitch += 0.1f;
            resetTime = 0.0f;
            timer = 0.0f;
        }
        if (resetTime >= 2.0f) {
            pitch = 1.0f;
        }
    }

    @Override
    public long play() {
        soundQueue.addLast(sound);
        return 1;
    }

    @Override
    public long play(float volume) {
        soundQueue.addLast(sound);
        return 1;
    }

    @Override
    public long play(float volume, float pitch, float pan) {
        soundQueue.addLast(sound);
        return 1;
    }

    @Override
    public long loop() {
        return sound.loop();
    }

    @Override
    public long loop(float volume) {
        return sound.loop(volume);
    }

    @Override
    public long loop(float volume, float pitch, float pan) {
        return sound.loop(volume, pitch, pan);
    }

    @Override
    public void stop() {
        sound.stop();
    }

    @Override
    public void pause() {
        sound.pause();
    }

    @Override
    public void resume() {
        sound.resume();
    }

    @Override
    public void dispose() {
        sound.dispose();
    }

    @Override
    public void stop(long soundId) {
        sound.stop(soundId);
    }

    @Override
    public void pause(long soundId) {
        sound.pause(soundId);
    }

    @Override
    public void resume(long soundId) {
        sound.resume(soundId);
    }

    @Override
    public void setLooping(long soundId, boolean looping) {
        sound.setLooping(soundId, looping);
    }

    @Override
    public void setPitch(long soundId, float pitch) {
        sound.setPitch(soundId, pitch);
    }

    @Override
    public void setVolume(long soundId, float volume) {
        sound.setVolume(soundId, volume);
    }

    @Override
    public void setPan(long soundId, float pan, float volume) {
        sound.setPan(soundId, pan, volume);
    }
}
