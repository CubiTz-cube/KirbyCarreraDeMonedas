package src.screens.worldScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import src.net.packets.Packet;
import src.world.ActorBox2d;
import src.world.entities.Entity;
import src.world.entities.enemies.Enemy;
import src.world.entities.otherPlayer.OtherPlayer;
import src.world.player.Player;
import src.main.Main;

public class GameScreen extends WorldScreen {
    private final Vector2 lastPosition;
    private Float sendTime = 0f;

    public GameScreen(Main main){
        super(main, -20f, "tiled/maps/mainMap.tmx");
        world.setContactListener(new GameContactListener());
        lastPosition = new Vector2();
    }

    @Override
    public void show() {
        if (main.server != null || main.client == null) tiledManager.makeMap();
        //Manda a enviar todas las entidades desde el servirdor
        if (main.server != null) main.client.send(Packet.newEnemy(null, null, null, null));
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

        stage.act();
        threadSecureWorld.step(delta, 6, 2);

        OrthographicCamera camera = (OrthographicCamera) stage.getCamera();

        camera.zoom = 1.3f;
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
        tiledRenderer.setView(camera);
        tiledRenderer.render();
        stage.draw();
        camera.zoom = 1f;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            main.changeScreen(Main.Screens.MENU);
            if (main.client != null) main.client.send(Packet.disconnectPlayer(-1));
        }

        for (ActorBox2d actor : actors) {
            if (actor instanceof Enemy enemy) {
                if (player.getCurrentState() == Player.StateType.ABSORB) {
                    if (player.getSprite().getBoundingRectangle().overlaps(enemy.getSprite().getBoundingRectangle())) {
                        player.setPowerUp(enemy);
                        removeEntity(enemy);
                    }
                }
            }
        }

        if (main.client == null) return;
        sendTime += delta;

        Vector2 currentPosition = player.getBody().getPosition();
        if (!currentPosition.epsilonEquals(lastPosition, 0.05f)) { // Check for significant change
            main.client.send(Packet.position(-1,currentPosition.x, currentPosition.y));
            lastPosition.set(currentPosition);
        }
        if (player.checkChangeAnimation()) main.client.send(Packet.actOtherPlayer(-1, player.getCurrentAnimation(), player.isFlipX()));

        if (main.server == null) return;

        if (sendTime >= 2f) {
            for (Entity e: entities.values()){
                if (e instanceof OtherPlayer) continue;
                main.client.send(Packet.position(e.getId(), e.getBody().getPosition().x, e.getBody().getPosition().y));
                if (!(e instanceof Enemy enemy)) continue;
                if (enemy.checkChangeState()) main.client.send(Packet.enemyState(e.getId(), enemy.getState(), enemy.getActCrono(), enemy.isFlipX()));
            }
            sendTime = 0f;
        }

    }

    public void addEntity(Enemy.Type actor, Vector2 position, Integer id){
        threadSecureWorld.addModification(() -> {
            ActorBox2d actorBox2d = enemyFactory.create(actor, world, position, id);
            addActor(actorBox2d);
        });
    }

    public void removeEntity(Integer id){
        removeEntity(entities.get(id));
    }

    public void actOtherPlayerAnimation(Integer id, Player.AnimationType animationType, Boolean flipX){
        Entity entity = entities.get(id);
        if (entity == null) {
            System.out.println("Animation OtherPlayer Entity " + id + " no encontrada en la lista");
            return;
        }
        if (!(entity instanceof OtherPlayer otherPlayer)) {
            System.out.println("Animation OtherPlayer Entity " + id + " no es un OtherPlayer");
            return;
        }
        otherPlayer.setAnimation(animationType);
        otherPlayer.setFlipX(flipX);
    }

    public void actPosEntity(Integer id, Float x, Float y){
        Entity entity = entities.get(id);
        if (entity == null) {
            System.out.println("Posision Entity " + id + " no encontrada en la lista");
            return;
        }
        Body body = entity.getBody();
        threadSecureWorld.addModification(() -> {
            body.setTransform(x, y, 0);
        });
    }

    public void actStateEnemy(Integer id, Enemy.StateType state,Float cronno, Boolean flipX){
        Enemy enemy = (Enemy) entities.get(id);
        enemy.setState(state);
        enemy.setActCrono(cronno);
        enemy.setFlipX(flipX);
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
            //Fixture A = contact.getFixtureA();
            //Fixture B = contact.getFixtureB();
            //System.out.println(A.getUserData() + " " + B.getUserData());
            if (areCollided(contact, "player", "enemy")) {
                player.setState(Player.StateType.STUNT);

                Body enemyBody = contact.getFixtureA().getUserData().equals("enemy") ? contact.getFixtureA().getBody() : contact.getFixtureB().getBody();

                Vector2 pushDirection = player.getBody().getPosition().cpy().sub(enemyBody.getPosition()).nor();

                player.getBody().applyLinearImpulse(pushDirection.scl(10.0f), player.getBody().getWorldCenter(), true);
            }
        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override public void preSolve(Contact contact, Manifold oldManifold) { }
        @Override public void postSolve(Contact contact, ContactImpulse impulse) { }
    }
}
