package src.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import src.screens.worldScreens.WorldScreen;
import src.world.entities.Entity;
import src.world.entities.EnemyFactory;
import src.world.entities.enemies.Enemy;
import src.world.player.Player;
import src.world.statics.StaticFactory;

import static src.utils.Constants.PIXELS_IN_METER;

public class TiledManager {
    private final WorldScreen game;
    private TiledMap tiledmap;
    private Integer tiledSize;
    private final StaticFactory staticFactory;
    private final EnemyFactory enemyFactory;

    public TiledManager(WorldScreen game) {
        this.game = game;
        staticFactory = new StaticFactory(game.main);
        enemyFactory = new EnemyFactory(game.main);
    }

    public OrthogonalTiledMapRenderer setupMap(String map) {
        tiledmap = new TmxMapLoader().load(map);
        tiledSize = tiledmap.getProperties().get("tilewidth", Integer.class);

        parsedColisionMap(tiledmap.getLayers().get("colision").getObjects());
        parsedPlayer(tiledmap.getLayers().get("player").getObjects());

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
            game.addActor(staticFactory.create(StaticFactory.Type.FLOOR, game.getWorld(),
                new Rectangle(X/tiledSize, Y/tiledSize, W/tiledSize, H/tiledSize)));
        }
    }

    public void parsedEntityMap(MapObjects objects) {

        for (MapObject object : objects) {
            String type = object.getProperties().get("type", String.class);
            float X = object.getProperties().get("x", Float.class) / tiledSize;
            float Y = object.getProperties().get("y", Float.class )/ tiledSize;

            game.addActor(enemyFactory.create(Enemy.Type.valueOf(type), game.getWorld(),
                new Vector2(X, Y), game.main.getIds(), game.getCrono()));
        }

    }

    public void dispose() {
        tiledmap.dispose();
    }

    public void makeMap() {
        parsedEntityMap(tiledmap.getLayers().get("entity").getObjects());
        parsedColisionMap(tiledmap.getLayers().get("colision").getObjects());
        parsedPlayer(tiledmap.getLayers().get("player").getObjects());
    }
}
