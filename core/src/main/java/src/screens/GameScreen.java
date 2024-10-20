package src.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import src.utils.TiledManager;
import src.world.ActorBox2d;
import src.world.ActorBox2dFactory;
import src.world.entities.player.Player;
import src.main.Main;
import java.util.ArrayList;

public class GameScreen extends BaseScreen{
    private final Stage stage;
    public final World world;
    private final OrthogonalTiledMapRenderer tiledRenderer;
    private final TiledManager tiledManager;
    private final ActorBox2dFactory actorFactory;

    private Player player;
    private ArrayList<ActorBox2d> actors = new ArrayList<>();

    /*
     *   JavaDoc
     */
    public GameScreen(Main main){
        super(main);
        actorFactory = new ActorBox2dFactory(main);

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(new GameContactListener());

        tiledManager = new TiledManager(this);
        tiledRenderer = tiledManager.setupMap();
    }

    public void addActor(ActorBox2d actor){
        actors.add(actor);
        stage.addActor(actor);
    }

    @Override
    public void show() {
        player = (Player) actorFactory.createActor(ActorBox2dFactory.ActorType.PLAYER, world, new Rectangle(0, 10, 0.5f, 0.5f));
        stage.addActor(player);
    }

    @Override
    public void hide() {
        player.detach();
        player.remove();
        for (ActorBox2d actor : actors) {
            actor.detach();
            actor.remove();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ((OrthographicCamera) stage.getCamera()).zoom = 1.2f;

        OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
        camera.position.set(player.getX(), player.getY(), 0);
        tiledRenderer.setView(camera);
        tiledRenderer.render();

        ((OrthographicCamera) stage.getCamera()).zoom = 1f;

        stage.act();
        world.step(delta, 6, 2);
        stage.draw();

    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        tiledManager.dispose();
    }

    private class GameContactListener implements ContactListener {

        private boolean areCollided(Contact contact, Object userA, Object userB) {
            Object userDataA = contact.getFixtureA().getUserData();
            Object userDataB = contact.getFixtureB().getUserData();

            if (userDataA == null || userDataB == null) return false;

            return (userDataA.equals(userA) && userDataB.equals(userB)) ||
                (userDataA.equals(userB) && userDataB.equals(userA));
        }

        @Override
        public void beginContact(Contact contact) {
            // The player has collided with the floor.
            if (areCollided(contact, "player", "floor")) {
                player.getStateMachine().setState(player.getIdleState());
            }
        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override public void preSolve(Contact contact, Manifold oldManifold) { }
        @Override public void postSolve(Contact contact, ContactImpulse impulse) { }
    }
}
