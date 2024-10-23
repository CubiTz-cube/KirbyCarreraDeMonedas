package src.screens.worldScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.world.ActorBox2d;
import src.world.ActorBox2dFactory;
import src.world.entities.player.Player;
import src.main.Main;

public class GameScreen extends WorldScreen {
    private Player player;

    public GameScreen(Main main){
        super(main, -15f, "maps/kirbyPrueba.tmx");
        world.setContactListener(new GameContactListener());
    }

    @Override
    public void show() {
        player = (Player) actorFactory.createActor(ActorBox2dFactory.ActorType.PLAYER, world, new Rectangle(0, 10, 0.5f, 0.5f));
        stage.addActor(player);
        tiledManager.reMakeMap();
    }

    @Override
    public void hide() {
        stage.clear();

        player.detach();
        actors.forEach(ActorBox2d::detach);
        actors.clear();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ((OrthographicCamera) stage.getCamera()).zoom = 1.3f;

        OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
        camera.position.set(player.getX(), player.getY(), 0);
        tiledRenderer.setView(camera);
        tiledRenderer.render();

        ((OrthographicCamera) stage.getCamera()).zoom = 1f;

        stage.act();
        world.step(delta, 6, 2);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            main.changeScreen(Main.Screens.MENU);
        }
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
            System.out.println(contact.getFixtureA().getUserData() + " " + contact.getFixtureB().getUserData());
            /*if (areCollided(contact, "playerBottomSensor", "floor") && player.getStateMachine().getState().equals(player.getJumpState())) {
                player.getStateMachine().setState(player.getIdleState());
            }*/
        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override public void preSolve(Contact contact, Manifold oldManifold) { }
        @Override public void postSolve(Contact contact, ContactImpulse impulse) { }
    }
}
