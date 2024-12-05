package src.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import src.net.packets.Packet;
import src.utils.SecondsTimer;
import src.utils.borderIndicator.MirrorIndicatorManager;
import src.utils.variables.ConsoleColor;
import src.utils.ThreadSecureWorld;
import src.utils.TiledManager;
import src.world.ActorBox2d;
import src.world.entities.Entity;
import src.world.entities.EntityFactory;
import src.world.entities.NoAutoPacketEntity;
import src.world.entities.blocks.Block;
import src.world.entities.blocks.BreakBlock;
import src.world.entities.enemies.Enemy;
import src.world.entities.mirror.Mirror;
import src.world.entities.otherPlayer.OtherPlayer;
import src.world.entities.player.Player;
import src.main.Main;
import src.world.entities.player.PlayerCommon;
import src.world.particles.ParticleFactory;
import src.world.statics.StaticFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameScreen extends BaseScreen {
    // Game
    private final Stage stage;
    private final World world;
    public ThreadSecureWorld threadSecureWorld;

    // Tiled
    private final OrthogonalTiledMapRenderer tiledRenderer;
    private final TiledManager tiledManager;

    // Factories
    public final EntityFactory entityFactory;
    public final StaticFactory staticFactory;
    public final ParticleFactory particleFactory;

    // Entities
    private Player player;
    private final ArrayList<ActorBox2d> actors;
    private final HashMap<Integer, Entity> entities;

    // Network
    private final Vector2 lastPosition;
    private Float sendTime;
    private final SecondsTimer timeGame;
    private final HashMap<Integer, ScorePlayer> scorePlayers;

    // Spawn
    private final Random random;
    public Vector2 lobbyPlayer;
    public ArrayList<Vector2> spawnMirror;
    public ArrayList<Vector2> spawnPlayer;

    // UI
    private MirrorIndicatorManager mirrorIndicators;
    private Table tableUI;
    private Label odsPointsLabel;
    private Label gameTimeLabel;
    private ChatWidget chatWidget;

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
        scorePlayers = new HashMap<>();
        timeGame = new SecondsTimer(0,15,0);

        random = new Random();
        spawnMirror = new ArrayList<>();
        spawnPlayer = new ArrayList<>();
    }

    private void initUI(){
        tableUI = new Table();
        tableUI.setFillParent(true);
        stage.addActor(tableUI);

        odsPointsLabel = new Label("ODS POINTS\n"+0,main.getSkin());
        odsPointsLabel.setAlignment(Align.topRight);
        odsPointsLabel.setFontScale(2);

        gameTimeLabel = new Label("Game Time\n" + timeGame, main.getSkin());
        gameTimeLabel.setAlignment(Align.topLeft);
        gameTimeLabel.setFontScale(2);

        chatWidget = new ChatWidget(main.getSkin());

        mirrorIndicators = new MirrorIndicatorManager(main.getAssetManager().get("yoshi.jpg", Texture.class));
        stage.addActor(mirrorIndicators);

        tableUI.top();
        tableUI.add(gameTimeLabel);
        tableUI.add().expandX();
        tableUI.add(odsPointsLabel);
        tableUI.row();
        tableUI.add(chatWidget).padTop(400).height(200).width(300).fill();
    }

    public void setScore(Integer score) {
        scorePlayers.get(-1).score = score;
        sendPacket(Packet.actScore(-1, score));
    }

    public Integer getScore() {
        return scorePlayers.get(-1).score;
    }

    public HashMap<Integer, ScorePlayer> getScorePlayers() {
        return scorePlayers;
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

    public void addMessage(String name, String message){
        chatWidget.addMessage(name + ": " + message);
    }

    public void addMainPlayer(){
        if (player != null) return;
        int index = random.nextInt(spawnPlayer.size());
        Vector2 position = new Vector2(spawnPlayer.get(index));

        player = new Player(world, new Rectangle(position.x, position.y, 1.5f, 1.5f), main.getAssetManager(), this);
        stage.addActor(player);

        if (main.client == null) scorePlayers.put(-1, new ScorePlayer("TU"));
        else scorePlayers.put(-1, new ScorePlayer(main.client.getName()));
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
        if (actor instanceof OtherPlayer o) scorePlayers.put(o.getId(), new ScorePlayer(o.getName()));
        if (actor instanceof Mirror m) mirrorIndicators.add(m.getId(),m.getBody().getPosition());

        stage.addActor(actor);
    }

    private void createEntityLogic(Entity.Type type, Vector2 position, Vector2 force, Integer id, Boolean flipX){
        if (entities.get(id) != null) {
            System.out.println(ConsoleColor.RED + "Entity " + type + ":" + id + " ya existe en la lista" + ConsoleColor.RESET);
            return;
        }
        threadSecureWorld.addModification(() -> {
            Entity newEntity = entityFactory.create(type, world, position, id);
            newEntity.setFlipX(flipX);
            newEntity.getBody().applyLinearImpulse(force, newEntity.getBody().getWorldCenter(), true);
            addActor(newEntity);
        });
    }

    public void addEntityNoPacket(Entity.Type type, Vector2 position, Vector2 force, Integer id, Boolean flipX){
        createEntityLogic(type, position, force, id, flipX);
        main.setIds(id);
    }

    public void addEntity(Entity.Type type, Vector2 position, Vector2 force){
        int id = main.getIds();
        createEntityLogic(type, position, force, id, false);
        sendPacket(Packet.newEntity(id, type, position.x, position.y, force.x, force.y, false));
    }
    public void addEntity(Entity.Type type, Vector2 position, Vector2 force, Boolean flipX){
        int id = main.getIds();
        createEntityLogic(type, position, force, id, flipX);
        sendPacket(Packet.newEntity(id, type, position.x, position.y, force.x, force.y, flipX));
    }

    public void actOtherPlayerAnimation(Integer id, Player.AnimationType animationType, Boolean flipX, PlayerCommon.StateType stateType){
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
        otherPlayer.setCurrentState(stateType);
        otherPlayer.setFlipX(flipX);
    }

    public void actEntityPos(Integer id, Float x, Float y, Float fx, Float fy){
        Entity entity = entities.get(id);
        if (entity == null) {
            System.err.println("Entity " + id + " no encontrada en la lista para cambiar su posicion");
            return;
        }
        Body body = entity.getBody();
        threadSecureWorld.addModification(() -> {
            body.setTransform(x, y, 0);
            body.setLinearVelocity(fx, fy);
        });
    }

    public void actEnemy(Integer id, Enemy.StateType state, Float cronno, Boolean flipX){
        Enemy enemy = (Enemy) entities.get(id);
        if (enemy == null) {
            System.out.println("Entity " + id + " no encontrada en la lista");
            return;
        }
        enemy.setState(state);
        enemy.setFlipX(flipX);
        enemy.setActCrono(cronno);
    }

    public void actBlock(Integer id, Block.StateType stateType){
        Block block = (Block) entities.get(id);
        if (block == null) {
            System.out.println("Entity " + id + " no encontrada en la lista");
            return;
        }
        block.setStateNoPacket(stateType);
    }

    public void actScore(Integer id, Integer score){
        ScorePlayer scorePlayer = scorePlayers.get(id);
        if (scorePlayer == null){
            System.out.println("ScorePlayer " + id + " no encontrada en la lista");
            return;
        }
        scorePlayer.score = score;
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
        if (actor instanceof Mirror m) mirrorIndicators.remove(m.getId());
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
        System.out.println("Eliminando entidad " + id);
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
        actors.clear();
        entities.clear();
        spawnMirror.clear();
        for (ActorBox2d actor : actors) actor.detach();
        if (player != null) player.detach();
        player = null;

    }

    public void endGame(){
        threadSecureWorld.clearModifications();
        main.closeClient();
        main.closeServer();
        threadSecureWorld.addModification(() -> {
            main.changeScreen(Main.Screens.ENDGAME);
            clearAll();
        });
    }

    public void playMinigame(){
        getPlayer().getBody().setTransform(lobbyPlayer.x, lobbyPlayer.y, 0);
        main.changeScreen(Main.Screens.MINIODSPLEASE);
       /* int select = random.nextInt(2);

        switch (select){
            case 0:
                main.changeScreen(Main.Screens.MINIDUCK);
                break;
            case 1:
                main.changeScreen(Main.Screens.MINIODSPLEASE);
                break;
        }*/
    }

    @Override
    public void show() {
        if (player != null) {
            threadSecureWorld.addModification(() -> {
                Vector2 position = spawnPlayer.get(random.nextInt(spawnPlayer.size()));
                player.getBody().setTransform(position.x, position.y, 0);
                player.getBody().setLinearVelocity(0,0);
                player.setCurrentState(Player.StateType.IDLE);
            });
        }else{
            tiledManager.makeMap();
            addMainPlayer();
            initUI();
            setScore(3);
            if (main.server != null || main.client == null){
                tiledManager.makeEntities();

                Vector2 position = spawnMirror.get(random.nextInt(spawnMirror.size()));
                addEntity(Entity.Type.MIRROR, position, new Vector2(0,0));
            }
        }
    }

    /**
     * Ejecuta todo lo necesario para que el juego funcione en segundo plano.
     * @param delta Tiempo en segundos desde el ultimo renderizado.
     */
    public void actLogic(float delta){
        if (main.client != null){
            sendTime += delta;

            Vector2 currentPosition = player.getBody().getPosition();
            if (!currentPosition.epsilonEquals(lastPosition, 0.05f)) { // Check for significant change
                main.client.send(Packet.actEntityPosition(-1,currentPosition.x, currentPosition.y));
                lastPosition.set(currentPosition);
            }

            if (player.checkChangeAnimation()) main.client.send(Packet.actOtherPlayer(-1, player.getCurrentAnimationType(), player.isFlipX(), player.getCurrentStateType()));

            if (!main.client.isRunning()) endGame();

            if (main.server != null){

                if (sendTime >= 2f) {
                    for (Entity e: entities.values()){
                        if (e instanceof NoAutoPacketEntity) continue;
                        Body body = e.getBody();
                        main.client.send(Packet.actEntityPosition(e.getId(), body.getPosition().x, body.getPosition().y,
                            body.getLinearVelocity().x , body.getLinearVelocity().y));
                        if (!(e instanceof Enemy enemy)) continue;
                        if (enemy.getCurrentStateType() == Enemy.StateType.IDLE) continue;
                        if (enemy.checkChangeState()) main.client.send(Packet.actEnemy(e.getId(), enemy.getCurrentStateType(), enemy.getActCrono(), enemy.isFlipX()));
                    }
                    sendTime = 0f;
                }
            }
        }
        timeGame.update(delta);
        stage.act();
        threadSecureWorld.step(delta, 6, 2);
    }

    private void actUI(){
        mirrorIndicators.setCenterPositions(player.getBody().getPosition());
        odsPointsLabel.setText("ODS POINTS\n"+getScore()+"\nx: "+(int)player.getX()+" y: "+(int)player.getY());
        gameTimeLabel.setText("Game Time\n" + timeGame);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        OrthographicCamera camera = (OrthographicCamera) stage.getCamera();

        camera.zoom = 1.3f;
        camera.position.x = MathUtils.lerp(camera.position.x, player.getX(), 0.2f);
        camera.position.y = MathUtils.lerp(camera.position.y, player.getY(), 0.3f);
        camera.update();
        tableUI.setPosition(camera.position.x - tableUI.getWidth()/2, camera.position.y - tableUI.getHeight()/2);
        tiledRenderer.setView(camera);
        tiledRenderer.render();
        actUI();
        stage.draw();
        camera.zoom = 1f;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) endGame();

        for (ActorBox2d actor : actors) {
            if (actor instanceof BreakBlock breakBlock) {
                if (player.getCurrentStateType() == Player.StateType.DASH) {
                    if (player.getSprite().getBoundingRectangle().overlaps(breakBlock.getSprite().getBoundingRectangle())) {
                        breakBlock.setState(BreakBlock.StateType.BREAK);
                    }
                }
            }
        }

        actLogic(delta);
    }

    public void randomMirror(Integer id){
        int index = random.nextInt(spawnMirror.size());
        Vector2 position = new Vector2(spawnMirror.get(index));

        actEntityPos(id, position.x, position.y, 0f, 0f);
        sendPacket(Packet.actEntityPosition(id, position.x, position.y));
        mirrorIndicators.changeTargetPosition(id ,position);
    }

    public void sendPacket(Object[] packet) {
        if (main.client != null) main.client.send(packet);
    }

    @Override
    public void dispose() {
        clearAll();
        world.dispose();
        tiledManager.dispose();
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
