package src.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;
import src.entities.player.Player;
import src.entities.player.states.IdleState;
import src.entities.player.states.JumpState;
import src.main.Main;
import src.world.Floor;

public class GameScreen extends BaseScreen{
    private Stage stage;
    private World world;

    private Player player;
    private Floor floor;

    public GameScreen(Main game){
        super(game);
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(new GameContactListener());
    }

    @Override
    public void show() {
        player = new Player(world, game.getAssetManager().get("yoshi.jpg"), new Vector2(1, 3));
        stage.addActor(player);
        floor = new Floor(world, game.getAssetManager().get("floor.png"), new Vector2(0, 0), new Vector2(20, 2));
        stage.addActor(floor);
    }

    @Override
    public void hide() {
        player.detach();
        player.remove();
        floor.detach();
        floor.remove();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getCamera().position.set(player.getX(), player.getY(), 0);

        stage.act();
        world.step(delta, 6, 2);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
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
