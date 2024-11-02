package src.screens.worldScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import src.net.packets.Packet;
import src.world.ActorBox2d;
import src.world.entities.Entity;
import src.world.entities.enemies.Enemy;
import src.world.entities.otherPlayer.OtherPlayer;
import src.world.player.Player;
import src.main.Main;

import java.util.LinkedList;
import java.util.Queue;

public class GameScreen extends WorldScreen {
    private final Vector2 lastPosition;
    private final Queue<Runnable> pendingActions;
    private Float sendTime = 0f;
    private final Label cronolabel;

    public GameScreen(Main main){
        super(main, -15f, "tiled/maps/kirbyPrueba.tmx");
        world.setContactListener(new GameContactListener());
        lastPosition = new Vector2();
        pendingActions = new LinkedList<>();
        cronolabel = new Label(crono.toString(), main.getSkin());
        cronolabel.setPosition(20, 20);
    }

    @Override
    public void show() {
        crono = 0f;
        stage.addActor(cronolabel);
        if (main.server != null || main.client == null) tiledManager.makeMap();
        if (main.server != null){
            main.client.send(Packet.newEnemy(null, null, null, null));
        }
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
        super.render(delta);
        cronolabel.setText(crono.toString());
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        world.step(delta, 6, 2);

        OrthographicCamera camera = (OrthographicCamera) stage.getCamera();

        camera.zoom = 1.3f;
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
        tiledRenderer.setView(camera);
        tiledRenderer.render();
        stage.draw();
        camera.zoom = 1f;

        while (!pendingActions.isEmpty()) {
            pendingActions.poll().run();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            main.changeScreen(Main.Screens.MENU);
            if (main.client != null) main.client.send(Packet.disconnectPlayer(-1));
        }

        if (main.client == null) return;
        sendTime += delta;

        Vector2 currentPosition = player.getBody().getPosition();
        if (!currentPosition.epsilonEquals(lastPosition, 0.1f)) { // Check for significant change
            main.client.send(Packet.position(-1,currentPosition.x, currentPosition.y));
            lastPosition.set(currentPosition);
        }

        if (main.server == null) return;

        if (sendTime >= 3f) {
            for (Entity e: entities.values()){
                if (e instanceof OtherPlayer) continue;
                main.client.send(Packet.position(e.getId(), e.getBody().getPosition().x, e.getBody().getPosition().y));
            }
            sendTime = 0f;
        }

    }

    public synchronized void addEnemy(Enemy.Type actor, Vector2 position, Integer id){
        pendingActions.add(() -> {
            ActorBox2d actorBox2d = enemyFactory.create(actor, world, position, id, crono);
            addActor(actorBox2d);
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
            entities.get(id).getBody().setTransform(x, y, 0);
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
