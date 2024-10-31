package src.screens.worldScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.net.packets.Packet;
import src.world.ActorBox2d;
import src.world.entities.Entity;
import src.world.entities.EntityFactory;
import src.world.player.Player;
import src.main.Main;

import java.util.LinkedList;
import java.util.Queue;

public class GameScreen extends WorldScreen {
    private static Player player;
    private final Vector2 lastPosition;
    private final Queue<Runnable> pendingActions;

    public GameScreen(Main main){
        super(main, -15f, "maps/kirbyPrueba.tmx");
        world.setContactListener(new GameContactListener());
        lastPosition = new Vector2();
        pendingActions = new LinkedList<>();
    }

    @Override
    public void show() {
        player = new Player(world, main.getAssetManager(), new Rectangle(12, 10, 1.5f, 1.5f));
        stage.addActor(player);
        addEntity(EntityFactory.Type.BASIC, new Vector2(10,10), 1);
        addEntity(EntityFactory.Type.SLEEPY, new Vector2(10,12), 1);
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

        while (!pendingActions.isEmpty()) {
            pendingActions.poll().run();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            main.changeScreen(Main.Screens.MENU);
            if (main.client != null) main.client.send(Packet.disconnectPlayer(-1));
        }

        if (main.client == null) return;

        Vector2 currentPosition = player.getBody().getPosition();
        if (!currentPosition.epsilonEquals(lastPosition, 0.1f)) { // Check for significant change
            main.client.send(Packet.position(-1,currentPosition.x, currentPosition.y));
            lastPosition.set(currentPosition);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        tiledManager.dispose();
    }

    public synchronized void addEntity(EntityFactory.Type actor, Vector2 position, Integer id){
        pendingActions.add(() -> {
            ActorBox2d actorBox2d = entityFactory.create(actor, world, position, id);
            actors.add(actorBox2d);
            stage.addActor(actorBox2d);
        });
    }

    public synchronized void removeEntity(Integer id){
        pendingActions.add(() -> {
            for (ActorBox2d actor : actors) {
                if (!(actor instanceof Entity entity)) continue;
                if (entity.getId().equals(id)) {
                    entity.detach();
                    actors.remove(actor);
                    stage.getActors().removeValue(actor, true);
                    break;
                }
            }
        });
    }

    public synchronized void actEntity(Integer id, Float x, Float y){
        pendingActions.add(() -> {
            for (ActorBox2d actor : actors) {
                if (!(actor instanceof Entity entity)) continue;
                if (entity.getId().equals(id)) {
                    entity.getBody().setTransform(x, y, 0);
                    break;
                }
            }
        });
    }

    public Player getPlayer() {
        return player;
    }

    private static class GameContactListener implements ContactListener {

        private boolean areCollided(Contact contact, Object userA, Object userB) {
            Object userDataA = contact.getFixtureA().getUserData();
            Object userDataB = contact.getFixtureB().getUserData();

            if (userDataA == null || userDataB == null) return false;

            return (userDataA.equals(userA) && userDataB.equals(userB)) ||
                (userDataA.equals(userB) && userDataB.equals(userA));
        }

        @Override
        public void beginContact(Contact contact) {
            //System.out.println(contact.getFixtureA().getUserData() + " " + contact.getFixtureB().getUserData());
            /*if (areCollided(contact, "playerBottomSensor", "floor") && player.getStateMachine().getState().equals(player.getFallState())) {
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
