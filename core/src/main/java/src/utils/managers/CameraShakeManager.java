package src.utils.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraShakeManager {
    OrthographicCamera camera;
    private Float timeShake, forceShake;

    public CameraShakeManager(OrthographicCamera camera) {
        this.camera = camera;
        timeShake = 0f;
        forceShake = 0f;
    }

    public void addShake(Float time, Float force) {
        timeShake = time;
        forceShake = force;
    }

    public void update(Float delta) {
        if (timeShake > 0) {
            timeShake -= delta;
            camera.position.x += (float) (Math.random() * forceShake - forceShake / 2);
            camera.position.y += (float) (Math.random() * forceShake - forceShake / 2);
        }

    }
}
