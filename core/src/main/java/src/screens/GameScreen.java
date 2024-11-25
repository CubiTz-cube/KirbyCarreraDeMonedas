package src.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import src.net.packets.Packet;
import src.utils.variables.ConsoleColor;
import src.utils.ThreadSecureWorld;
import src.utils.TiledManager;
import src.world.ActorBox2d;
import src.world.entities.Entity;
import src.world.entities.EntityFactory;
import src.world.entities.breakBlocks.BreakBlock;
import src.world.entities.enemies.Enemy;
import src.world.entities.mirror.Mirror;
import src.world.entities.otherPlayer.OtherPlayer;
import src.world.entities.player.Player;
import src.main.Main;
import src.world.particles.ParticleFactory;
import src.world.statics.StaticFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameScreen extends BaseScreen {
    private final Stage stage;
    private final World world;
    public ThreadSecureWorld threadSecureWorld;

    private final OrthogonalTiledMapRenderer tiledRenderer;
    private final TiledManager tiledManager;

    public final EntityFactory entityFactory;
    public final StaticFactory staticFactory;
    public final ParticleFactory particleFactory;

    private Player player;
    private final ArrayList<ActorBox2d> actors;
    private final HashMap<Integer, Entity> entities;

    private final Vector2 lastPosition;
    private Float sendTime;
    private Integer score;

    private final Random random;
    public Vector2 lobbyPlayer;
    public ArrayList<Vector2> spawnMirror;
    public ArrayList<Vector2> spawnPlayer;

    public GameScreen(Main main){
        super(main);
        actors = new ArrayList<>();
        entities = new HashMap<>();

        entityFactory = new EntityFactory(this);
        staticFactory = new StaticFactory();
        particleFactory = new ParticleFactory();

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        world = new World(new Vector2(0, -30f), true);
        threadSecureWorld = new ThreadSecureWorld(world);

        tiledManager = new TiledManager(this);
        tiledRenderer = tiledManager.setupMap("tiled/maps/gameMap.tmx");

        world.setContactListener(new GameContactListener(this));
        lastPosition = new Vector2();
        sendTime = 0f;
        score = 0;

        random = new Random();
        spawnMirror = new ArrayList<>();
        spawnPlayer = new ArrayList<>();


    }

    public void setScore(Integer score) {
        this.score = score;
        System.out.println("Score: " + score);
    }

    public Integer getScore() {
        return score;
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return player;
    }

    public HashMap<Integer, Entity> getEntities() {
        return entities;
    }

    public void addScore(Integer score) {
        setScore(getScore() + score);
    }

    public void addMainPlayer(){
        if (player != null) return;
        Vector2 position = spawnPlayer.get(random.nextInt(spawnPlayer.size()));
        player = new Player(world, new Rectangle(position.x, position.y, 1.5f, 1.5f), main.getAssetManager(), this);
        stage.addActor(player);
    }

    public void addStatic(StaticFactory.Type type, Rectangle bounds){
        threadSecureWorld.addModification(() -> {
            ActorBox2d actorBox2d = staticFactory.create(type, world, bounds);
            addActor(actorBox2d);
        });
    }

    public void addActor(Actor actor){
        if (actor instanceof Entity e) entities.put(e.getId(), e);
        if (actor instanceof ActorBox2d a) actors.add(a);
        stage.addActor(actor);
    }

    private void createEntityLogic(Entity.Type type, Vector2 position, Integer id){
        if (entities.get(id) != null) {
            System.out.println(ConsoleColor.RED + "Entity " + type + ":" + id + " ya existe en la lista" + ConsoleColor.RESET);
            return;
        }
        System.out.println("Entity " + type + ":" + id + " se mando a crear");
        ActorBox2d actorBox2d = entityFactory.create(type, world, position, id);
        addActor(actorBox2d);
    }

    public void addEntityNoPacket(Entity.Type type, Vector2 position, Integer id){
        threadSecureWorld.addModification(() -> {
            createEntityLogic(type, position, id);
            main.setIds(id);
        });
    }

    public void addEntity(Entity.Type type, Vector2 position){
        int id = main.getIds();
        threadSecureWorld.addModification(() -> {
            createEntityLogic(type, position, id);
            sendPacket(Packet.newEntity(id, type, position.x, position.y));
        });
    }

    public void addEntityWithForce(Entity.Type type, Vector2 position, Vector2 force){
        int id = main.getIds();
        threadSecureWorld.addModification(() -> {
            createEntityLogic(type, position, id);
            sendPacket(Packet.newEntity(id, type, position.x, position.y));
            Entity entity = entities.get(id);
            entity.getBody().applyLinearImpulse(force, entity.getBody().getWorldCenter(), true);
        });
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
        threadSecureWorld.addModification(() -> body.setTransform(x, y, 0));
    }

    public void actStateEnemy(Integer id, Enemy.StateType state,Float cronno, Boolean flipX){
        Enemy enemy = (Enemy) entities.get(id);
        if (enemy == null) {
            System.out.println("Entity " + id + " no encontrada en la lista");
            return;
        }
        enemy.setState(state);
        enemy.setActCrono(cronno);
        enemy.setFlipX(flipX);
    }

    public void actBreakBlock(Integer id, BreakBlock.StateType stateType){
        BreakBlock breakBlock = (BreakBlock) entities.get(id);
        if (breakBlock == null) {
            System.out.println("Entity " + id + " no encontrada en la lista");
            return;
        }
        breakBlock.setState(stateType);
    }

    /**
     * Elimina un actor del juego. Sin enviar paquete.
     * @param actor Actor a eliminar.
     */
    public void removeActorBox2d(ActorBox2d actor){
        threadSecureWorld.addModification(() -> {
            if (actor == null) {
                System.out.println(ConsoleColor.RED + "Actor no se pudo eliminar , es nulo" + ConsoleColor.RESET);
                return;
            }
            if (actor instanceof Entity e) {
                entities.remove(e.getId());
            }
            actors.remove(actor);
            removeActor(actor);
            actor.detach();
        });
    }

    public void removeActor(Actor actor){
        stage.getActors().removeValue(actor, true);
    }

    /**
     * Elimina una entidad del juego. Envia paquete.
     * @param id Id de la entidad a eliminar.
     */
    public void removeEntity(Integer id){
        Entity entity = entities.get(id);
        if (entity == null) {
            System.out.println(ConsoleColor.RED + "Entity " + id + " no se pudo eliminar ,no encontrada en la lista" + ConsoleColor.RESET);
            return;
        }
        sendPacket(Packet.removeEntity(entity.getId()));
        removeActorBox2d(entity);
    }

    /**
     * Elimina una entidad del juego. Sin enviar paquete.
     * @param id Id de la entidad a eliminar.
     */
    public void removeEntityNoPacket(Integer id){
        Entity entity = entities.get(id);
        if (entity == null) {
            System.out.println(ConsoleColor.RED + "Entity " + id + " no se pudo eliminar ,no encontrada en la lista" + ConsoleColor.RESET);
            return;
        }
        removeActorBox2d(entity);
    }

    public void addParticle(ParticleFactory.Type type, Vector2 position){
        threadSecureWorld.addModification(() -> {
            addActor(particleFactory.create(type, position, this));
        });
    }

    public void clearAll(){
        stage.clear();
        player.detach();
        player = null;
        for (ActorBox2d actor : actors) actor.detach();
        actors.clear();
        entities.clear();
        score = 0;
        spawnMirror.clear();
    }

    @Override
    public void show() {
        if (player != null) {
            threadSecureWorld.addModification(() -> {
                Vector2 position = spawnPlayer.get(random.nextInt(spawnPlayer.size()));
                player.getBody().setTransform(position.x, position.y, 0);
                player.getBody().setLinearVelocity(0,0);
                player.setState(Player.StateType.IDLE);
            });
        }else{
            tiledManager.makeMap();
            addMainPlayer();
            if (main.server != null || main.client == null){
                tiledManager.makeEntities();
                Vector2 position = spawnMirror.get(random.nextInt(spawnMirror.size()));
                addEntity(Entity.Type.MIRROR, position);
            }
        }
    }

    /**
     * Ejecuta todo lo necesario para que el juego funcione en segundo plano.
     * @param delta Tiempo en segundos desde el ultimo renderizado.
     */
    public void actLogic(float delta){
        stage.act();
        threadSecureWorld.step(delta, 6, 2);

        if (main.client == null) return;
        sendTime += delta;

        Vector2 currentPosition = player.getBody().getPosition();
        if (!currentPosition.epsilonEquals(lastPosition, 0.05f)) { // Check for significant change
            main.client.send(Packet.position(-1,currentPosition.x, currentPosition.y));
            lastPosition.set(currentPosition);
        }
        if (player.checkChangeAnimation()) main.client.send(Packet.actOtherPlayer(-1, player.getCurrentAnimationType(), player.isFlipX()));

        if (main.server == null) return;

        if (sendTime >= 2f) {
            for (Entity e: entities.values()){
                if (e instanceof OtherPlayer) continue;
                main.client.send(Packet.position(e.getId(), e.getBody().getPosition().x, e.getBody().getPosition().y));
                if (!(e instanceof Enemy enemy)) continue;
                if (enemy.checkChangeState()) main.client.send(Packet.actEnemy(e.getId(), enemy.getCurrentStateType(), enemy.getActCrono(), enemy.isFlipX()));
            }
            sendTime = 0f;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        actLogic(delta);

        OrthographicCamera camera = (OrthographicCamera) stage.getCamera();

        camera.zoom = 1.3f;
        camera.position.x = MathUtils.lerp(camera.position.x, player.getX(), 0.2f);
        camera.position.y = MathUtils.lerp(camera.position.y, player.getY(), 0.3f);
        camera.update();
        tiledRenderer.setView(camera);
        tiledRenderer.render();
        stage.draw();
        camera.zoom = 1f;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            main.changeScreen(Main.Screens.MENU);
            sendPacket(Packet.disconnectPlayer(-1));
            clearAll();
        }

        for (ActorBox2d actor : actors) {
            if (actor instanceof BreakBlock breakBlock) {
                if (player.getCurrentStateType() == Player.StateType.DASH) {
                    if (player.getSprite().getBoundingRectangle().overlaps(breakBlock.getSprite().getBoundingRectangle())) {
                        sendPacket(Packet.actBreakBlock(breakBlock.getId(), BreakBlock.StateType.BREAK));
                        breakBlock.setState(BreakBlock.StateType.BREAK);
                    }
                }
            }
        }
    }

    public void sendPacket(Object[] packet) {
        if (main.client != null) main.client.send(packet);
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        tiledManager.dispose();
    }

    public void randomMirror(){
        if (main.server == null) return;
        for (Entity entity : entities.values()){
            if (entity instanceof Mirror mirror) {
                int index = random.nextInt(spawnMirror.size());
                Vector2 position = spawnMirror.get(index);
                actPosEntity(mirror.getId(), position.x, position.y);
                return;
            }
        }
    }

    private static class GameContactListener implements ContactListener {
        private final GameScreen game;

        public GameContactListener(GameScreen gameScreen) {
            this.game = gameScreen;
        }

        @Override
        public void beginContact(Contact contact) {
            ActorBox2d actorA = (ActorBox2d) contact.getFixtureA().getUserData();
            ActorBox2d actorB = (ActorBox2d) contact.getFixtureB().getUserData();

            if (actorA == null || actorB == null) return;
            actorA.beginContactWith(actorB, game);
            actorB.beginContactWith(actorA, game);
        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override public void preSolve(Contact contact, Manifold oldManifold) { }
        @Override public void postSolve(Contact contact, ContactImpulse impulse) { }
    }
}
