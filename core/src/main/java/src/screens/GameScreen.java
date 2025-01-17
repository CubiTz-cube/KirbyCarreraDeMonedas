package src.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import src.net.packets.Packet;
import src.screens.components.*;
import src.screens.components.chat.Chat;
import src.screens.uiScreens.UIScreen;
import src.utils.*;
import src.utils.indicators.BorderIndicator;
import src.utils.indicators.IndicatorManager;
import src.utils.constants.ConsoleColor;
import src.utils.managers.CameraShakeManager;
import src.utils.managers.SpawnManager;
import src.utils.managers.TiledManager;
import src.utils.sound.SingleSoundManager;
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
import src.world.entities.player.powers.PowerUp;
import src.world.particles.ParticleFactory;
import src.world.statics.StaticFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import static src.utils.constants.Constants.TIME_MINUTES_GAME;

public class GameScreen extends UIScreen {
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
    public SpawnManager spawnMirror;
    public ArrayList<Vector2> spawnPlayer;

    // UI
    private final CameraShakeManager cameraShakeManager;
    private IndicatorManager mirrorIndicators;
    private BorderIndicator maxScoreIndicator;
    private Integer idTargetMaxScore;

    private LayersManager layersManager;
    private Label odsPointsLabel;
    private Label gameTimeLabel;
    private Chat chat;
    private PowerView imagePower;
    private OptionTable optionTable;
    private Boolean menuVisible;

    //Sounds
    private Sound mirrorChangeSound;
    private Sound pauseSound;
    private Sound pauseExitSound;

    private final Box2DDebugRenderer debugRenderer;
    private Boolean isLoad;

    public GameScreen(Main main){
        super(main);
        actors = new ArrayList<>();
        entities = new HashMap<>();

        entityFactory = new EntityFactory(this);
        staticFactory = new StaticFactory(this);
        particleFactory = new ParticleFactory();

        stage = new Stage(new ScreenViewport());
        world = new World(new Vector2(0, -30f), true);
        threadSecureWorld = new ThreadSecureWorld(world);

        tiledManager = new TiledManager(this);
        tiledRenderer = tiledManager.setupMap("tiled/maps/gameMap.tmx");

        world.setContactListener(new GameContactListener(this));
        lastPosition = new Vector2();
        sendTime = 0f;
        scorePlayers = new HashMap<>();
        timeGame = new SecondsTimer(TIME_MINUTES_GAME, 0);

        random = new Random();
        spawnMirror = new SpawnManager();
        spawnPlayer = new ArrayList<>();

        idTargetMaxScore = -1;

        initSounds();

        debugRenderer = new Box2DDebugRenderer();
        menuVisible = false;

        cameraShakeManager = new CameraShakeManager((OrthographicCamera) stage.getCamera());
        isLoad = false;
    }

    private void initSounds(){
        mirrorChangeSound = main.getAssetManager().get("sound/portalChange.wav", Sound.class);
        pauseExitSound = main.getAssetManager().get("sound/ui/pauseExit.wav", Sound.class);
        pauseSound = main.getAssetManager().get("sound/ui/pause.wav", Sound.class);
    }

    public void setScore(Integer score) {
        scorePlayers.get(-1).score = score;
        sendPacket(Packet.actScore(-1, score));
    }

    public Integer getScore() {
        ScorePlayer scorePlayer = scorePlayers.get(-1);
        if (scorePlayer != null) return scorePlayers.get(-1).score;
        else return null;
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
        chat.addMessage(name + ": " + message);
    }

    public void addMainPlayer(){
        if (player != null) return;
        int index = random.nextInt(spawnPlayer.size());
        Vector2 position = new Vector2(spawnPlayer.get(index));

        player = new Player(world, position.x, position.y, main.getAssetManager(), this, main.playerColor);
        stage.addActor(player);

        if (main.client == null) scorePlayers.put(-1, new ScorePlayer(-1,"TU"));
        else scorePlayers.put(-1, new ScorePlayer(-1,main.client.getName()));
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
        if (actor instanceof OtherPlayer o) scorePlayers.put(o.getId(), new ScorePlayer(o.getId(),o.getName()));
        if (actor instanceof Mirror m) mirrorIndicators.add(m.getId(),m.getBody().getPosition());

        stage.addActor(actor);
    }

    private void createEntityLogic(Entity.Type type, Vector2 position, Vector2 force, Integer id, Boolean flipX){
        if (entities.get(id) != null) {
            System.out.println(ConsoleColor.RED + "Entity " + type + ":" + id + " ya existe en la lista" + ConsoleColor.RESET);
            return;
        }
        //System.out.println("Creando Entidad " + id + " Tipo: " + type);
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
    public void addEntityNoPacket(Entity.Type type, Vector2 position, Vector2 force, Boolean flipX){
        int id = main.getIds();
        createEntityLogic(type, position, force, id, flipX);
    }

    public void addEntity(Entity.Type type, Vector2 position, Vector2 force, Boolean flipX){
        int id = main.getIds();
        createEntityLogic(type, position, force, id, flipX);
        sendPacket(Packet.newEntity(id, type, position.x, position.y, force.x, force.y, flipX));
    }
    public void addEntity(Entity.Type type, Vector2 position, Vector2 force){
        addEntity(type, position, force, false);
    }

    public void addEntitySpawn(Entity.Type type, Vector2 force, SpawnManager spawnManager){
        int id = main.getIds();
        Vector2 position = spawnManager.takeSpawnPoint(id);
        createEntityLogic(type, position, force, id, false);
        sendPacket(Packet.newEntity(id, type, position.x, position.y, force.x, force.y, false));
    }

    public void actOtherPlayerAnimation(Integer id, Player.AnimationType animationType, Boolean flipX, PlayerCommon.StateType stateType, PowerUp.Type powerType){
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
        otherPlayer.setCurrentPowerUp(powerType);
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
        if (enemy.getCurrentStateType() == Enemy.StateType.ATTACK) return;
        enemy.setState(state);
        enemy.setFlipX(flipX);
        enemy.setActCrono(cronno);
    }

    public void actDamageEnemy(Integer receiverId, Body attacker, Integer damage, Float knockback) {
        if (!entities.containsKey(receiverId)) {
            System.out.println("Entity " + receiverId + " no encontrada en la lista para actualizar dano");
            return;
        }
        Body receiver = entities.get(receiverId).getBody();
        Vector2 pushDirection = attacker.getPosition().cpy().sub(receiver.getPosition()).nor();

        actDamageEnemyNoPacket(receiverId, damage, pushDirection.x, pushDirection.y, knockback);

        sendPacket(Packet.actDamageEnemy(receiverId, damage, pushDirection.x, pushDirection.y, knockback));
    }

    public void actDamageEnemyNoPacket(Integer id, Integer damage, Float forceX, Float forceY, Float knockback){
        if (!entities.containsKey(id)) {
            System.out.println("Entity " + id + " no encontrada en la lista para actualizar dano");
            return;
        }
        Enemy enemy = (Enemy) entities.get(id);
        if (enemy.getCurrentStateType() == Enemy.StateType.DAMAGE) return;
        threadSecureWorld.addModification(() -> {
            enemy.takeDamage(damage);
            enemy.getBody().setLinearVelocity(0,0);
            enemy.getBody().applyLinearImpulse(forceX* -knockback, forceY* -knockback, enemy.getBody().getWorldCenter().x, enemy.getBody().getWorldCenter().y, true);
            enemy.getBody().applyLinearImpulse(0,knockback, enemy.getBody().getWorldCenter().x, enemy.getBody().getWorldCenter().y, true);
        });
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

        ScorePlayer maxScorePlayer = scorePlayers.values().stream()
            .max(Comparator.comparingInt(s -> s.score))
            .orElse(null);

        if (!isLoad) return;

        if (maxScorePlayer != null && maxScorePlayer.id != -1) {
            idTargetMaxScore = maxScorePlayer.id;
            maxScoreIndicator.setVisible(true);
        }
        else maxScoreIndicator.setVisible(false);

    }

    public void actEntityColor(Integer id, float r, float g, float b, float a){
        Entity entity = entities.get(id);
        if (entity == null) {
            System.out.println("Entity " + id + " no encontrada en la lista para cambio de color");
            return;
        }
        entity.setColor(r,g,b,a);
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
        removeEntityNoPacket(id);
        sendPacket(Packet.removeEntity(id));
    }

    /**
     * Elimina una entidad del juego. Sin enviar paquete.
     * @param id Id de la entidad a eliminar.
     */
    public void removeEntityNoPacket(Integer id){
        threadSecureWorld.addModification(() -> {
            Entity entity = entities.get(id);
            if (entity == null) {
                System.out.println(ConsoleColor.RED + "Entity " + id + " no se pudo eliminar ,no encontrada en la lista" + ConsoleColor.RESET);
                return;
            }
            entities.remove(entity.getId());
            actors.remove(entity);
            removeActor(entity);
            entity.detach();
        });
    }

    public void addParticle(ParticleFactory.Type type, Vector2 position){
        threadSecureWorld.addModification(() -> {
            addActor(particleFactory.create(type, position, this));
        });
    }

    public void clearAll(){
        for (ActorBox2d actor : actors) actor.detach();
        if (player != null) player.detach();
        player = null;

        stage.clear();
        stageUI.clear();
        actors.clear();
        entities.clear();
        spawnMirror.clear();

    }

    public void endGame(){
        threadSecureWorld.clearModifications();
        main.closeClient();
        main.closeServer();
        threadSecureWorld.addModification(() -> {
            clearAll();
            timeGame.resetTimer();
            main.changeScreen(Main.Screens.ENDGAME);
            isLoad = false;
        });
    }

    public void playMinigame(){
        getPlayer().getBody().setTransform(lobbyPlayer.x, lobbyPlayer.y, 0);
        player.setPaused(true);
        int select = random.nextInt(2);

        switch (select){
            case 0:
                main.changeScreen(Main.Screens.MINIFIRE);
                break;
            case 1:
                main.changeScreen(Main.Screens.MINIODSPLEASE);
                break;
            /*case 2:
                main.changeScreen(Main.Screens.MINIDUCK);
                break;*/
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stageUI);
        SingleSoundManager.getInstance().setSoundTracks(Main.SoundTrackType.GAME);
        if (player != null) {
            player.setPaused(false);
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
            setMenuVisible(false);
            if (main.server != null || main.client == null){
                tiledManager.makeEntities();
                addEntitySpawn(Entity.Type.MIRROR, new Vector2(0,0), spawnMirror);
            }
            isLoad = true;
        }
    }

    private void initUI(){
        layersManager = new LayersManager(stageUI, 6);

        Image timeImage = new Image(main.getAssetManager().get("ui/icons/clock.png", Texture.class));

        gameTimeLabel = new Label(timeGame.toString(), new Label.LabelStyle(main.getBriBorderFont(), null));
        gameTimeLabel.setAlignment(Align.left);
        gameTimeLabel.setFontScale(1);

        Image coinImage = new Image(main.getAssetManager().get("ui/icons/coinIcon.png", Texture.class));
        coinImage.setScaling(Scaling.fit);

        odsPointsLabel = new Label("0", new Label.LabelStyle(main.getBriBorderFont(), null));
        odsPointsLabel.setAlignment(Align.left);
        odsPointsLabel.setFontScale(0.8f);

        chat = new Chat(new Label.LabelStyle(main.getInterFont(), null));

        mirrorIndicators = new IndicatorManager(main.getAssetManager().get("ui/indicators/mirrorIndicator.png", Texture.class));
        maxScoreIndicator = new BorderIndicator(main.getAssetManager().get("ui/indicators/maxScoreIndicator.png", Texture.class), new Vector2(0,0));
        maxScoreIndicator.setVisible(false);

        imagePower = new PowerView(main.getAssetManager());

        Image pauseBg = new Image(main.getAssetManager().get("ui/bg/whiteBg.png", Texture.class));

        ImageTextButton exitButton = new ImageTextButton("Salir", myImageTextbuttonStyle);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                endGame();
            }
        });
        exitButton.addListener(hoverListener);

        stage.addActor(mirrorIndicators);
        stage.addActor(maxScoreIndicator);

        layersManager.setZindex(0);
        optionTable = new OptionTable(main.getSkin(), layersManager.getLayer(), main.getBriFont());
        layersManager.getLayer().add(exitButton).width(200).padTop(10);
        layersManager.getLayer().setVisible(false);

        layersManager.setZindex(1);
        layersManager.getLayer().add(pauseBg).grow();
        layersManager.getLayer().setVisible(false);

        layersManager.setZindex(2);
        layersManager.getLayer().top().pad(10);
        layersManager.getLayer().add(timeImage).padRight(5).size(64);
        layersManager.getLayer().add(gameTimeLabel);
        layersManager.getLayer().add().expandX();
        layersManager.getLayer().row().padTop(5);
        layersManager.getLayer().add(coinImage).padRight(5).size(48);
        layersManager.getLayer().add(odsPointsLabel).left();

        layersManager.setZindex(3);
        layersManager.getLayer().add(chat).grow();

        layersManager.setZindex(4);
        layersManager.getLayer().bottom();
        layersManager.getLayer().add().expandX();
        layersManager.getLayer().add(imagePower).width(182).height(50).row();

        layersManager.setZindex(2);
        layersManager.getLayer().setVisible(true);
    }

    private void setMenuVisible(Boolean visible){
        menuVisible = visible;
        optionTable.update();
        layersManager.setZindex(0);
        layersManager.getLayer().setVisible(visible);
        layersManager.setZindex(1);
        layersManager.getLayer().setVisible(visible);
    }
    private void setMenuVisibleSound(Boolean visible){
        setMenuVisible(visible);
        if (visible) SingleSoundManager.getInstance().playSound(pauseSound);
        else SingleSoundManager.getInstance().playSound(pauseExitSound);
    }

    /**
     * Ejecuta todo lo necesario para que el juego funcione en segundo plano.
     * @param delta Tiempo en segundos desde el ultimo renderizado.
     */
    public void actLogic(float delta){
        if (timeGame.isFinished()) endGame();
        if (main.client != null){
            sendTime += delta;

            Vector2 currentPosition = player.getBody().getPosition();
            if (!currentPosition.epsilonEquals(lastPosition, 0.05f)) {
                sendPacket(Packet.actEntityPosition(-1,currentPosition.x, currentPosition.y));
                lastPosition.set(currentPosition);
            }

            if (!main.client.isRunning()) endGame();

            if (main.server != null){

                if (sendTime >= 1f) {
                    for (Entity e: entities.values()){
                        if (e instanceof NoAutoPacketEntity) continue;
                        Body body = e.getBody();
                        sendPacket(Packet.actEntityPosition(e.getId(), body.getPosition().x, body.getPosition().y,
                            body.getLinearVelocity().x , body.getLinearVelocity().y));
                        if (!(e instanceof Enemy enemy)) continue;
                        if (enemy.getCurrentStateType() == Enemy.StateType.IDLE || enemy.getCurrentStateType() == Enemy.StateType.DAMAGE) continue;
                        if (enemy.checkChangeState()) sendPacket(Packet.actEnemy(e.getId(), enemy.getCurrentStateType(), enemy.getActCrono(), enemy.isFlipX()));
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
        if (idTargetMaxScore != -1 && entities.containsKey(idTargetMaxScore)) {
            maxScoreIndicator.setTargetPosition(entities.get(idTargetMaxScore).getBody().getPosition());
        }
        maxScoreIndicator.setCenterPosition(player.getBody().getPosition());
        mirrorIndicators.setCenterPositions(player.getBody().getPosition());

        odsPointsLabel.setText(getScore());
        gameTimeLabel.setText(timeGame.toString());
        imagePower.setPower(player.getCurrentpowerUptype());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!isLoad) return;

        OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
        OrthographicCamera cameraUI = (OrthographicCamera) stageUI.getCamera();

        camera.position.x = MathUtils.lerp(camera.position.x, player.getX() + (player.isFlipX() ? -32 : 32), 0.10f);
        camera.position.y = MathUtils.lerp(camera.position.y, player.getY(), 0.3f);

        cameraUI.position.x = camera.position.x;
        cameraUI.position.y = camera.position.y;

        cameraShakeManager.update(delta);
        camera.update();
        cameraUI.update();

        layersManager.setPosition(cameraUI.position.x - layersManager.getLayer().getWidth()/2, cameraUI.position.y - layersManager.getLayer().getHeight()/2);
        tiledRenderer.setView(camera);

        tiledRenderer.render();
        actUI();
        stage.draw();
        stageUI.draw();
        //debugRenderer.render(world, camera.combined.scale(PIXELS_IN_METER, PIXELS_IN_METER, 1));

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) setMenuVisibleSound(!menuVisible);

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

    @Override
    public void resize(int width, int height) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                float cameraZoom = 1280.0f / width;
                if (cameraZoom > 1.3f) cameraZoom = 1.3f;
                OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
                camera.zoom = cameraZoom;

                stage.getViewport().update(width, height, false);
                stageUI.getViewport().update(width, height, false);

                if (player == null) return;

                camera.position.x = player.getX() + (player.isFlipX() ? -32 : 32);
                camera.position.y = player.getY();
            }
        });
    }

    public void randomMirror(Integer id){
        Vector2 position = spawnMirror.reSpawn(id);
        mirrorChangeSound.play();
        if (main.client != null) sendPacket(Packet.message("Servidor",main.client.getName() + " ha entrado a un espejo."));

        actEntityPos(id, position.x, position.y, 0f, 0f);
        sendPacket(Packet.actEntityPosition(id, position.x, position.y));
        mirrorIndicators.changeTargetPosition(id ,position);

        respawnEnemy();
    }

    public void sendPacket(Object[] packet) {
        if (main.client != null) main.client.send(packet);
    }

    public void respawnEnemy(){
        for (Entity e: entities.values()){
            if (e instanceof Enemy){
                removeEntity(e.getId());
            }
        }
        tiledManager.makeEnemy();
    }

    public void playProximitySound(Sound sound, Vector2 soundPosition, float maxDistance) {
        float distance = soundPosition.dst(player.getBody().getPosition());
        float volume = Math.max(0, 1 - (distance / maxDistance));
        SingleSoundManager.getInstance().playSound(sound, 1f, volume);
    }

    public void addCameraShake(Float time, Float force){
        cameraShakeManager.addShake(time,force);
    }

    public void addCameraShakeProximity(Vector2 position, float maxDistance, float time, float maxForce) {
        float distance = position.dst(player.getBody().getPosition());
        float force = Math.max(0, 1 - (distance / maxDistance));
        cameraShakeManager.addShake(time, maxForce * force);
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
