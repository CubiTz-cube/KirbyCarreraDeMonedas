package src.utils.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SpawnManager {
    private final ArrayList<Vector2> spawnPoints;
    private final HashMap<Integer, Vector2> takenSpawnPoints;
    private final Random random;

    public SpawnManager() {
        spawnPoints = new ArrayList<>();
        takenSpawnPoints = new HashMap<>();
        random = new Random();
    }

    public void add(Vector2 spawnPoint) {
        spawnPoints.add(spawnPoint);
    }

    public Vector2 takeSpawnPoint(int id) {
        if (spawnPoints.isEmpty()) {
            Gdx.app.log("SpawnManager", "No hay spawn points disponibles");
            return null;
        }

        Vector2 newTakenSpawnPoint = new Vector2(spawnPoints.remove(random.nextInt(spawnPoints.size())));
        takenSpawnPoints.put(id, new Vector2(newTakenSpawnPoint));
        return newTakenSpawnPoint;
    }

    public void unTakeSpawnPoint(int id) {
        if (!takenSpawnPoints.containsKey(id)) {
            Gdx.app.log("SpawnManager", "id " + id + " no encontrada");
            return;
        }

        add(new Vector2(takenSpawnPoints.remove(id)));
    }

    public Vector2 reSpawn(int id){
        boolean contain = takenSpawnPoints.containsKey(id);

        Vector2 lastSpawnPoint = null;
        if (contain) lastSpawnPoint = new Vector2(takenSpawnPoints.remove(id));

        Vector2 newSpawnPoint = takeSpawnPoint(id);
        if (contain) add(lastSpawnPoint);
        return newSpawnPoint;
    }

    public void clear() {
        spawnPoints.clear();
        takenSpawnPoints.clear();
    }
}
