package src.utils.managers;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import src.screens.GameScreen;
import src.utils.constants.ConsoleColor;
import src.world.entities.Entity;
import src.world.statics.FloorPoly;
import src.world.statics.StaticFactory;

import java.util.ArrayList;
import java.util.Arrays;

import static src.utils.constants.Constants.PIXELS_IN_METER;

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

    public void parsedStaticMap(MapObjects objects) {
        for (MapObject object : objects) {
            if (object instanceof PolygonMapObject polygonObject) {
                Polygon polygon = polygonObject.getPolygon();

                // Obtener los atributos del polígono
                float[] vertices = polygon.getVertices();
                float x = polygon.getX();
                float y = polygon.getY();
                float rotation = polygon.getRotation();

                // Procesar los atributos según sea necesario
                System.out.println("Polígono encontrado con vértices: " + Arrays.toString(vertices));
                System.out.println("Posición: (" + x + ", " + y + ")");
                System.out.println("Rotación: " + rotation);

                Vector2[] verticesVector = new Vector2[vertices.length/2];
                for (int i = 0; i < vertices.length; i+=2) {
                    verticesVector[i/2] = new Vector2(vertices[i]/tiledSize, vertices[i+1]/tiledSize);
                }

                FloorPoly newFloorPoly = new FloorPoly(game.getWorld(),
                    new Rectangle(x/tiledSize, y/tiledSize, 1, 1),
                    verticesVector);
                game.addActor(newFloorPoly);
            }

            String type = object.getProperties().get("type", String.class);
            Float X = (Float) object.getProperties().get("x");
            Float Y = (Float) object.getProperties().get("y");
            Float W = (Float) object.getProperties().get("width");
            Float H = (Float) object.getProperties().get("height");

            if (type == null) {
                game.addStatic(StaticFactory.Type.FLOOR, new Rectangle(X/tiledSize, Y/tiledSize, W/tiledSize, H/tiledSize));
                continue;
            }
            try{
                game.addStatic(StaticFactory.Type.valueOf(type), new Rectangle(X/tiledSize, Y/tiledSize, W/tiledSize, H/tiledSize));
            }
            catch (IllegalArgumentException e) {
                System.out.println(ConsoleColor.GRAY +  "Tipo de static " + type + " no encontrado" + ConsoleColor.RESET);
            }

        }
    }

    public void parsedEntityMap(MapObjects objects) {
        ArrayList<MapObject> objectArray = new ArrayList<>();
        for (MapObject object : objects) {
            objectArray.add(object);
        }
        for (MapObject object : objectArray) {
            String type = object.getProperties().get("type", String.class);
            float X = object.getProperties().get("x", Float.class) / tiledSize;
            float Y = object.getProperties().get("y", Float.class )/ tiledSize;

            try{
                game.addEntity(Entity.Type.valueOf(type), new Vector2(X, Y), new Vector2(0,0));
            }catch (IllegalArgumentException e) {
                System.out.println(ConsoleColor.GRAY + "Tipo de entidad " + type + " no encontrado" + ConsoleColor.RESET);
            }
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

    public void parsedPlayer(MapObjects objects) {
        for (MapObject object : objects) {
            String type = object.getProperties().get("type", String.class);
            float X = object.getProperties().get("x", Float.class) / tiledSize;
            float Y = object.getProperties().get("y", Float.class )/ tiledSize;

            if (type == null) {
                game.spawnPlayer.add(new Vector2(X, Y));
                continue;
            }
            game.lobbyPlayer = new Vector2(X, Y);

        }
    }

    public void dispose() {
        tiledmap.dispose();
    }

    public void makeMap() {
        parsedPlayer(tiledmap.getLayers().get("playerSpawn").getObjects());
        parsedStaticMap(tiledmap.getLayers().get("static").getObjects());
        parsedSpawnMap(tiledmap.getLayers().get("spawn").getObjects());
    }

    public void makeEntities() {
        parsedEntityMap(tiledmap.getLayers().get("entity").getObjects());
        parsedEntityMap(tiledmap.getLayers().get("enemy").getObjects());
    }

    public void makeEnemy() {
        parsedEntityMap(tiledmap.getLayers().get("enemy").getObjects());
    }
}
