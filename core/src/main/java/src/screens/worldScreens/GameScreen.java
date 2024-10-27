package src.screens.worldScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.net.packets.Packet;
import src.world.ActorBox2d;
import src.world.entities.Entity;
import src.world.entities.EntityFactory;
import src.world.player.Player;
import src.main.Main;

public class GameScreen extends WorldScreen {
    private Player player;

    public GameScreen(Main main){
        super(main, -15f, "maps/kirbyPrueba.tmx");
        world.setContactListener(new GameContactListener());
    }

    @Override
    public void show() {
        player = new Player(world, main.getAssetManager().get("yoshi.jpg"), new Rectangle(8, 10, 0.5f, 0.5f));
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

    public void addEntity(EntityFactory.Type actor, Rectangle shape, Integer id){
        ActorBox2d actorBox2d = entityFactory.create(actor, world, shape, id);
        actors.add(actorBox2d);
        stage.addActor(actorBox2d);
    }

    public void removeEntity(Integer id){
        for (ActorBox2d actor : actors) {
            if (!(actor instanceof Entity entity)) continue;
            if (entity.getId().equals(id)) {
                System.out.println("Eliminado " + id);
                entity.detach();
                actors.remove(actor);
                break;
            }
        }

    }

    public void actEntity(Integer id, Float x, Float y){
        for (ActorBox2d actor : actors) {
            if (!(actor instanceof Entity entity)) continue;
            System.out.println(entity.getId() + " " + id);
            if (entity.getId().equals(id)) {
                entity.getBody().setTransform(x, y, 0);
                break;
            }
        }
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
