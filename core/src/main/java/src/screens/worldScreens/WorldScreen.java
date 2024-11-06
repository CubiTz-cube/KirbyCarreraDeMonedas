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
import src.screens.uiScreens.UIScreen;
import src.utils.ThreadSecureWorld;
import src.utils.TiledManager;
import src.world.ActorBox2d;
import src.world.entities.Entity;
import src.world.entities.EnemyFactory;
import src.world.entities.otherPlayer.OtherPlayer;
import src.world.player.Player;
import src.world.statics.StaticFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class WorldScreen extends BaseScreen {
    protected Stage stage;
    protected World world;
    protected ThreadSecureWorld threadSecureWorld;
    protected OrthogonalTiledMapRenderer tiledRenderer;
    protected TiledManager tiledManager;
    protected EnemyFactory enemyFactory;
    protected StaticFactory staticFactory;
    protected ArrayList<ActorBox2d> actors;
    protected HashMap<Integer, Entity> entities;
    protected static Player player;
    protected Float crono = 0f;

    /**
     * Crear una patalla con mundo y stage incluido ademas de cargar un tiledMap
     * @param main Clase principal para poder cambiar de pantalla
     * @param gravity Gravedad del mundo
     * @param map TileMap a cargar
     */
    public WorldScreen(Main main, Float gravity, String map){
        super(main);
        actors = new ArrayList<>();
        entities = new HashMap<>();
        enemyFactory = new EnemyFactory(main);

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        world = new World(new Vector2(0, gravity), true);
        threadSecureWorld = new ThreadSecureWorld(world);

        tiledManager = new TiledManager(this);
        tiledRenderer = tiledManager.setupMap(map);
    }

    public Float getCrono() {
        return crono;
    }

    public void setCrono(Float crono) {
            this.crono = crono;
    }

    public World getWorld() {
        return world;
    }

    public HashMap<Integer, Entity> getEntities() {
        return entities;
    }

    public void addMainPlayer(Vector2 position){
        if (player != null) return;
        player = new Player(world, main.getAssetManager(), new Rectangle(position.x, position.y, 1.5f, 1.5f));
        stage.addActor(player);
    }

    public void addActor(ActorBox2d actor){
        if (actor instanceof Entity e) entities.put(e.getId(), e);
        actors.add(actor);
        stage.addActor(actor);
    }

    public void removeEntity(Entity entity){
        threadSecureWorld.addModification(() -> {
            entity.detach();
            entities.remove(entity.getId());
            actors.remove(entity);
            stage.getActors().removeValue(entity, true);
        });
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        tiledManager.dispose();
    }
}
