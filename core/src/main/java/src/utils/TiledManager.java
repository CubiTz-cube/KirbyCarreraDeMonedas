package src.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import src.screens.BaseScreen;
import src.screens.GameScreen;
import src.world.entities.Entity;
import src.world.entities.EntityFactory;
import src.world.statics.StaticFactory;

import static src.utils.Constants.PIXELS_IN_METER;

public class TiledManager {
    private final GameScreen game;
    private TiledMap tiledmap;
    private Integer tiledSize;

    public TiledManager(GameScreen game) {
        this.game = game;
    }

    public OrthogonalTiledMapRenderer setupMap(String map) {
        tiledmap = new TmxMapLoader().load(map);
        tiledSize = tiledmap.getProperties().get("tilewidth", Integer.class);

        return new OrthogonalTiledMapRenderer(tiledmap, PIXELS_IN_METER/tiledSize);
    }

    public void parsedPlayer(MapObjects objects) {
        for (MapObject object : objects) {
            float X = object.getProperties().get("x", Float.class) / tiledSize;
            float Y = object.getProperties().get("y", Float.class )/ tiledSize;
            game.addMainPlayer(new Vector2(X, Y));
        }
    }

    public void parsedColisionMap(MapObjects objects) {
        for (MapObject object : objects) {
            Float X = (Float) object.getProperties().get("x");
            Float Y = (Float) object.getProperties().get("y");
            Float W = (Float) object.getProperties().get("width");
            Float H = (Float) object.getProperties().get("height");

            game.addStatic(StaticFactory.Type.FLOOR, new Rectangle(X/tiledSize, Y/tiledSize, W/tiledSize, H/tiledSize));
        }
    }

    public void parsedEntityMap(MapObjects objects) {
        for (MapObject object : objects) {
            String type = object.getProperties().get("type", String.class);
            float X = object.getProperties().get("x", Float.class) / tiledSize;
            float Y = object.getProperties().get("y", Float.class )/ tiledSize;

            game.addEntity(Entity.Type.valueOf(type), new Vector2(X, Y), game.main.getIds());
        }
    }

    public void parsedSpawnMap(MapObjects objects) {
        for (MapObject object : objects) {
            String type = object.getProperties().get("type", String.class);
            float X = object.getProperties().get("x", Float.class) / tiledSize;
            float Y = object.getProperties().get("y", Float.class )/ tiledSize;

            if (Entity.Type.valueOf(type) == Entity.Type.MIRROR) {
                game.spawnMirror.add(new Vector2(X, Y));
            }
        }
    }

    public void dispose() {
        tiledmap.dispose();
    }

    public void makeMap() {
        parsedPlayer(tiledmap.getLayers().get("player").getObjects());
        parsedColisionMap(tiledmap.getLayers().get("colision").getObjects());
        parsedSpawnMap(tiledmap.getLayers().get("spawn").getObjects());
    }

    public void makeEntities() {
        parsedEntityMap(tiledmap.getLayers().get("entities").getObjects());
    }
}
