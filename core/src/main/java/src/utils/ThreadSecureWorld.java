package src.utils;

import com.badlogic.gdx.physics.box2d.World;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadSecureWorld {
    private final World world;
    private final ConcurrentLinkedQueue<Runnable> modificationQueue;

    public ThreadSecureWorld(World world) {
        this.world = world;
        this.modificationQueue = new ConcurrentLinkedQueue<>();
    }

    public void step(float delta, int velocityIterations, int positionIterations) {
        world.step(delta, velocityIterations, positionIterations);
        while (!modificationQueue.isEmpty()) {
            Runnable run = modificationQueue.poll();
            if (run == null) continue;
            run.run();
        }
    }

    public void addModification(Runnable modification) {
        modificationQueue.add(modification);
    }
}
