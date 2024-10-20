package src.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import src.screens.worldScreens.WorldScreen;
import src.world.ActorBox2dFactory;
import static src.utils.Constants.PIXELS_IN_METER;

public class TiledManager {
    private WorldScreen game;
    private TiledMap tiledmap;
    private Integer tiledSize;
    private ActorBox2dFactory actorFactory;

    public TiledManager(WorldScreen game) {
        this.game = game;
        actorFactory = new ActorBox2dFactory(game.main);
    }

    public OrthogonalTiledMapRenderer setupMap(String map) {
        tiledmap = new TmxMapLoader().load(map);
        tiledSize = tiledmap.getProperties().get("tilewidth", Integer.class);

        //parsedEntityMap(tiledmap.getLayers().get("objects").getObjects());
        parsedColisionMap(tiledmap.getLayers().get("colision").getObjects());

        return new OrthogonalTiledMapRenderer(tiledmap, PIXELS_IN_METER/tiledSize);
    }

    public void parsedColisionMap(MapObjects objects) {
        for (MapObject object : objects) {
            Float X = (Float) object.getProperties().get("x");
            Float Y = (Float) object.getProperties().get("y");
            Float W = (Float) object.getProperties().get("width");
            Float H = (Float) object.getProperties().get("height");
            game.addActor(actorFactory.createActor(ActorBox2dFactory.ActorType.FLOOR, game.getWorld(),
                new Rectangle(X/tiledSize, Y/tiledSize, W/tiledSize, H/tiledSize)));
        }
    }

    public void parsedEntityMap(MapObjects objects) {

        for (MapObject object : objects) {
            String type = object.getName();
            if (type.equals("player")){
                createMainPlayer(object);
            }else{
                createEntity(type, object);
            }
        }

    }

    public void createMainPlayer(MapObject object) {
        Float X = (Float) object.getProperties().get("x");
        Float Y = (Float) object.getProperties().get("y");
        //game.createMainPlayer( X, Y);
    }

    public void createEntity(String type, MapObject object) {
        Float X = (Float) object.getProperties().get("x");
        Float Y = (Float) object.getProperties().get("y");
        //game.entityManager.addEntity(EntityFactory.createEntity(type, X, Y));
    }

    public void dispose() {
        tiledmap.dispose();
    }

    public void reMakeMap() {
        //parsedEntityMap(tiledmap.getLayers().get("objects").getObjects());
        parsedColisionMap(tiledmap.getLayers().get("colision").getObjects());
    }
}
