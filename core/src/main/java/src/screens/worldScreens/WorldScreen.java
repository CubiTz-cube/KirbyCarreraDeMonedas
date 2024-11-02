package src.screens.worldScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import src.main.Main;
import src.screens.BaseScreen;
import src.utils.TiledManager;
import src.world.ActorBox2d;
import src.world.entities.EntityFactory;
import src.world.player.Player;
import src.world.statics.StaticFactory;

import java.util.ArrayList;

public class WorldScreen extends BaseScreen {
    protected Stage stage;
    protected World world;
    protected OrthogonalTiledMapRenderer tiledRenderer;
    protected TiledManager tiledManager;
    protected EntityFactory entityFactory;
    protected StaticFactory staticFactory;
    protected ArrayList<ActorBox2d> actors;
    protected Player player;

    /**
     * Crear una patalla con mundo y stage incluido ademas de cargar un tiledMap
     * @param main Clase principal para poder cambiar de pantalla
     * @param gravity Gravedad del mundo
     * @param map TileMap a cargar
     */
    public WorldScreen(Main main, Float gravity, String map){
        super(main);
        actors = new ArrayList<>();
        entityFactory = new EntityFactory(main);

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        world = new World(new Vector2(0, gravity), true);

        tiledManager = new TiledManager(this);
        tiledRenderer = tiledManager.setupMap(map);
    }

    public void addMainPlayer(Vector2 position){
        if (player != null) return;
        player = new Player(world, main.getAssetManager(), new Rectangle(position.x, position.y, 1.5f, 1.5f));
        stage.addActor(player);
    }

    public World getWorld() {
        return world;
    }

    public void addActor(ActorBox2d actor){
        actors.add(actor);
        stage.addActor(actor);
    }
}
