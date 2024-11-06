package src.utils;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Queue;

public class ThreadSecureWorld {
    private final World world;
    private final Queue<Runnable> modificationQueue;

    public ThreadSecureWorld(World world) {
        this.world = world;
        this.modificationQueue = new Queue<>();
    }

    public void step(float delta, int velocityIterations, int positionIterations) {
        world.step(delta, velocityIterations, positionIterations);
        while (!modificationQueue.isEmpty()) {
            modificationQueue.removeFirst().run();
        }
    }

    public void addModification(Runnable modification) {
        modificationQueue.addLast(modification);
    }
}
