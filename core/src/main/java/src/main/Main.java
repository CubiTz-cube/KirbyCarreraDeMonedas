package src.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import src.net.Client;
import src.net.Server;
import src.screens.game.GameScreen;
import src.screens.minigames.fireFighter.FireFighterScreen;
import src.screens.minigames.odsPlease.OdsPleaseScreen;
import src.screens.uiScreens.IntroScreen;
import src.screens.minigames.duckFeed.MiniDuckScreen;
import src.screens.uiScreens.*;
import src.utils.Fonts;
import src.utils.sound.SingleSoundManager;
import src.utils.sound.SoundManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Game {
    private AssetManager assetManager;
    private ArrayList<Screen> screensList;
    private Skin skin;
    public enum Screens {
        INTRO,
        INTRO2,
        MENU,
        MULTIPLAYER,
        JOIN,
        SERVER,
        OPTION,
        INFO,
        LOBBY,
        CONNECTING,
        GAME,
        ENDGAME,
        MINIDUCK,
        MINIFIRE,
        MINIODSPLEASE,
    }

    public Boolean skipIntro;

    private AtomicInteger ids;
    public Color playerColor;
    private String playerName;
    private String ip;
    private Integer port;

    public Server server;
    public Client client;
    private ExecutorService serverThread = Executors.newSingleThreadExecutor();
    private ExecutorService clientThread = Executors.newSingleThreadExecutor();

    public Fonts fonts;

    private SoundManager soundManager;
    public enum SoundTrackType {
        MENU,
        GAME,
    }

    @Override
    public void create() {
        skipIntro = false;

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        ids = new AtomicInteger(0);
        playerColor = Color.WHITE;
        playerName = Modificable.NOMBRE_JUGADOR;

        initAssets();

        soundManager = SingleSoundManager.getInstance();
        soundManager.setVolumeMusic(0.1f);
        initSounds();
        fonts = new Fonts();
        initScreens();

        changeScreen(Screens.INTRO);
    }

    private void initAssets(){
        assetManager = new AssetManager();
        assetManager.load("yoshi.jpg", Texture.class);
        assetManager.load("logo.png", Texture.class);
        assetManager.load("ucab.png", Texture.class);
        assetManager.load("world/entities/sleepy/sleepyIdle.png", Texture.class);
        assetManager.load("world/entities/sleepy/sleepyDamage.png", Texture.class);
        assetManager.load("world/entities/fly/flyIdle.png", Texture.class);
        assetManager.load("world/entities/fly/flyDamage.png", Texture.class);
        assetManager.load("world/entities/sword/swordEnemyWalk.png", Texture.class);
        assetManager.load("world/entities/sword/swordEnemyDamage.png", Texture.class);
        assetManager.load("world/entities/sword/swordEnemyAttack.png", Texture.class);
        assetManager.load("world/entities/sword/swordEnemyIdle.png", Texture.class);
        assetManager.load("world/entities/wheel/wheelIdle.png", Texture.class);
        assetManager.load("world/entities/wheel/wheelWalk.png", Texture.class);
        assetManager.load("world/entities/wheel/wheelDamage.png", Texture.class);
        assetManager.load("world/entities/dragon/dragonIdle.png", Texture.class);
        assetManager.load("world/entities/dragon/dragonWalk.png", Texture.class);
        assetManager.load("world/entities/dragon/dragonDamage.png", Texture.class);
        assetManager.load("world/entities/dragon/dragonAttack.png", Texture.class);
        assetManager.load("world/entities/bomb/bombIdle.png", Texture.class);
        assetManager.load("world/entities/bomb/bombWalk.png", Texture.class);
        assetManager.load("world/entities/bomb/bombDamage.png", Texture.class);
        assetManager.load("world/entities/bomb/bombAttack.png", Texture.class);
        assetManager.load("world/entities/turret/turretEnemy.png", Texture.class);
        assetManager.load("ui/colorWheel.png", Texture.class);
        assetManager.load("ui/indicators/maxScoreIndicator.png", Texture.class);
        assetManager.load("ui/indicators/mirrorIndicator.png", Texture.class);
        assetManager.load("ui/icons/powerIcons.png", Texture.class);
        assetManager.load("ui/icons/clock.png", Texture.class);
        assetManager.load("ui/icons/coinIcon.png", Texture.class);
        assetManager.load("ui/buttons/info.png", Texture.class);
        assetManager.load("ui/buttons/infoHover.png", Texture.class);
        assetManager.load("ui/buttons/exit.png", Texture.class);
        assetManager.load("ui/buttons/exitHover.png", Texture.class);
        assetManager.load("ui/buttons/button.png", Texture.class);
        assetManager.load("ui/buttons/buttonHover.png", Texture.class);
        assetManager.load("ui/buttons/input.png", Texture.class);
        assetManager.load("ui/bg/lineBg.png", Texture.class);
        assetManager.load("ui/bg/pinkLineBg.png", Texture.class);
        assetManager.load("ui/bg/aroBg.png", Texture.class);
        assetManager.load("ui/bg/aroColorBg.png", Texture.class);
        assetManager.load("ui/bg/menuBg.png", Texture.class);
        assetManager.load("ui/bg/kirbyBg.png", Texture.class);
        assetManager.load("ui/bg/kirbyIdleBg.png", Texture.class);
        assetManager.load("ui/bg/aroColorPlayerBg.png", Texture.class);
        assetManager.load("ui/bg/whiteBg.png", Texture.class);
        assetManager.load("ui/bg/whiteContainerBg.png", Texture.class);

        assetManager.load("background/backgroundBeach.png", Texture.class);
        assetManager.load("world/entities/breakBlock.png", Texture.class);
        assetManager.load("world/entities/movingPlatform.png", Texture.class);
        assetManager.load("world/entities/coin.png", Texture.class);
        assetManager.load("world/entities/bomb.png", Texture.class);
        assetManager.load("world/entities/ice.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyWalk.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyIdle.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyJump.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyFall.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyFallSimple.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyDown.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyRun.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyFly.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyFlyEnd.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyInFly.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyUpFly.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyDash.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyAbsorb.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyDamage.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyConsume.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyConsumePower.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyChangeRun.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbySleep.png", Texture.class);
        assetManager.load("world/entities/kirby/sleep/sleep.png", Texture.class);
        assetManager.load("world/entities/kirby/hold/kirbyDownHold.png", Texture.class);
        assetManager.load("world/entities/kirby/hold/kirbyIdleHold.png", Texture.class);
        assetManager.load("world/entities/kirby/hold/kirbyUpFlyHold.png", Texture.class);
        assetManager.load("world/entities/kirby/hold/kirbyWalkHold.png", Texture.class);
        assetManager.load("world/entities/kirby/absorb/kirbyAbsorbIdle.png", Texture.class);
        assetManager.load("world/entities/kirby/absorb/kirbyAbsorbWalk.png", Texture.class);
        assetManager.load("world/entities/kirby/absorb/kirbyAbsorbFall.png", Texture.class);
        assetManager.load("world/entities/kirby/absorb/kirbyAbsorbJump.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbyIdleSword.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbyWalkSword.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbyChangeRunSword.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbyDashSword.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbyDownSword.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbyFallSimpleSword.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbyFallSword.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbyFlyEndSword.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbyFlySword.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbyInFlySword.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbyJumpSword.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbyRunSword.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbySwordRapidSlash.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbySwordSlash.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbySwordSpin.png", Texture.class);
        assetManager.load("world/entities/kirby/sword/kirbyUpFlySword.png", Texture.class);
        assetManager.load("world/entities/kirby/wheel/kirbyDashWheel.png", Texture.class);
        assetManager.load("world/entities/kirby/wheel/kirbyRunWheel.png", Texture.class);
        assetManager.load("world/entities/kirby/wheel/kirbyIdleWheel.png", Texture.class);
        assetManager.load("world/entities/kirby/wheel/kirbyWalkWheel.png", Texture.class);
        assetManager.load("world/entities/kirby/wheel/kirbyDownWheel.png", Texture.class);
        assetManager.load("world/entities/kirby/wheel/kirbyFlyWheel.png", Texture.class);
        assetManager.load("world/entities/kirby/wheel/kirbyInFlyWheel.png", Texture.class);
        assetManager.load("world/entities/kirby/wheel/kirbyUpFlyWheel.png", Texture.class);
        assetManager.load("world/entities/kirby/wheel/kirbyFlyEndWheel.png", Texture.class);
        assetManager.load("world/entities/kirby/bomb/kirbyAttackBomb.png", Texture.class);
        assetManager.load("world/entities/kirby/bomb/kirbyChangeRunBomb.png", Texture.class);
        assetManager.load("world/entities/kirby/bomb/kirbyDashBomb.png", Texture.class);
        assetManager.load("world/entities/kirby/bomb/kirbyDownBomb.png", Texture.class);
        assetManager.load("world/entities/kirby/bomb/kirbyFallBomb.png", Texture.class);
        assetManager.load("world/entities/kirby/bomb/kirbyFallSimpleBomb.png", Texture.class);
        assetManager.load("world/entities/kirby/bomb/kirbyFlyBomb.png", Texture.class);
        assetManager.load("world/entities/kirby/bomb/kirbyFlyEndBomb.png", Texture.class);
        assetManager.load("world/entities/kirby/bomb/kirbyIdleBomb.png", Texture.class);
        assetManager.load("world/entities/kirby/bomb/kirbyInFlyBomb.png", Texture.class);
        assetManager.load("world/entities/kirby/bomb/kirbyJumpBomb.png", Texture.class);
        assetManager.load("world/entities/kirby/bomb/kirbyRunBomb.png", Texture.class);
        assetManager.load("world/entities/kirby/bomb/kirbyUpFlyBomb.png", Texture.class);
        assetManager.load("world/entities/kirby/bomb/kirbyWalkBomb.png", Texture.class);
        assetManager.load("world/entities/powers/bombPowerItem.png", Texture.class);
        assetManager.load("world/entities/powers/swordPowerItem.png", Texture.class);
        assetManager.load("world/entities/powers/wheelPowerItem.png", Texture.class);
        assetManager.load("world/entities/mirror/mirrorLoop.png", Texture.class);
        assetManager.load("world/entities/basic/basicIdle.png", Texture.class);
        assetManager.load("world/entities/basic/basicWalk.png", Texture.class);
        assetManager.load("world/entities/basic/basicDamage.png", Texture.class);
        assetManager.load("world/entities/blocks/fallBlock.png", Texture.class);
        assetManager.load("world/entities/blocks/fallBlockBreak.png", Texture.class);
        assetManager.load("world/entities/blocks/swordBreakBlock.png", Texture.class);
        assetManager.load("world/entities/blocks/bombBreakBlock.png", Texture.class);
        assetManager.load("world/particles/cloudParticle.png", Texture.class);
        assetManager.load("world/particles/dustParticle.png", Texture.class);
        assetManager.load("world/particles/starParticle.png", Texture.class);
        assetManager.load("world/particles/swordParticle.png", Texture.class);
        assetManager.load("world/particles/iceParticle.png", Texture.class);
        assetManager.load("world/particles/bombParticle.png", Texture.class);
        assetManager.load("world/particles/turretParticle.png", Texture.class);
        assetManager.load("world/particles/kirbySwordParticle.png", Texture.class);

        assetManager.load("miniGames/fireFighter/plane.png", Texture.class);
        assetManager.load("miniGames/fireFighter/fire.png", Texture.class);
        assetManager.load("miniGames/fireFighter/forest.jpg", Texture.class);
        assetManager.load("miniGames/odsPlease/Desk.png", Texture.class);
        assetManager.load("miniGames/odsPlease/CheckpointBack.png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (1).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (2).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (3).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (4).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (5).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (6).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (7).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (8).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (9).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (10).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (11).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (12).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (13).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (14).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (15).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (16).png", Texture.class);
        assetManager.load("miniGames/odsPlease/odsPng/ods (17).png", Texture.class);
        assetManager.load("miniGames/odsPlease/wrongOds/wrongOds (1).png", Texture.class);
        assetManager.load("miniGames/odsPlease/wrongOds/wrongOds (2).png", Texture.class);
        assetManager.load("miniGames/odsPlease/wrongOds/wrongOds (3).png", Texture.class);
        assetManager.load("miniGames/odsPlease/wrongOds/wrongOds (3B).png", Texture.class);
        assetManager.load("miniGames/odsPlease/wrongOds/wrongOds (4).png", Texture.class);
        assetManager.load("miniGames/odsPlease/wrongOds/wrongOds (5).png", Texture.class);
        assetManager.load("miniGames/odsPlease/wrongOds/wrongOds (6).png", Texture.class);
        assetManager.load("miniGames/odsPlease/wrongOds/wrongOds (7).png", Texture.class);
        assetManager.load("miniGames/odsPlease/wrongOds/wrongOds (8).png", Texture.class);
        assetManager.load("miniGames/odsPlease/wrongOds/wrongOds (9).png", Texture.class);
        assetManager.load("miniGames/odsPlease/wrongOds/wrongOds (10).png", Texture.class);
        assetManager.load("miniGames/odsPlease/wrongOds/wrongOds (12).png", Texture.class);
        assetManager.load("miniGames/odsPlease/wrongOds/wrongOds (16).png", Texture.class);
        assetManager.load("miniGames/odsPlease/BoothWall.png", Texture.class);
        assetManager.load("miniGames/odsPlease/persons/persons1.png", Texture.class);
        assetManager.load("miniGames/odsPlease/persons/persons2.png", Texture.class);
        assetManager.load("miniGames/odsPlease/persons/persons3.png", Texture.class);
        assetManager.load("miniGames/odsPlease/persons/persons4.png", Texture.class);

        assetManager.load("music/meow.mp3", Music.class);
        assetManager.load("music/anomalocaris.mp3", Music.class);
        assetManager.load("music/arthropluera.mp3", Music.class);
        assetManager.load("music/caterpillar.mp3", Music.class);
        assetManager.load("music/crocodile.mp3", Music.class);
        assetManager.load("music/coffee.mp3", Music.class);
        assetManager.load("music/waiting.mp3", Music.class);

        assetManager.load("sound/kirby/kirbyAirShot.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyItem.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyAbsorb.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyDash.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyFireDamage.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyIceDamage.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyHeavyFall.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyJump.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyNormalDamage.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyPower.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyScore1.wav", Sound.class);
        assetManager.load("sound/kirby/kirbySleep.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyStar.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyRemovePower.wav", Sound.class);
        assetManager.load("sound/kirby/powers/kirbyWheelDash.wav", Sound.class);
        assetManager.load("sound/ui/click.wav", Sound.class);
        assetManager.load("sound/ui/hover.wav", Sound.class);
        assetManager.load("sound/ui/pause.wav", Sound.class);
        assetManager.load("sound/ui/pauseExit.wav", Sound.class);
        assetManager.load("sound/coin.wav", Sound.class);
        assetManager.load("sound/explosion.wav", Sound.class);
        assetManager.load("sound/introLogo.wav", Sound.class);
        assetManager.load("sound/portalChange.wav", Sound.class);
        assetManager.load("sound/enemy/enemyDamage.wav", Sound.class);
        assetManager.load("sound/enemy/enemyDead.wav", Sound.class);
        System.out.println("Loading assets...");
        assetManager.finishLoading();
        System.out.println("Assets loaded.");
    }

    private void initSounds(){
        soundManager.addSoundTrack(SoundTrackType.MENU);
        soundManager.addSoundTrack(SoundTrackType.GAME);
        soundManager.addMusicToSoundTrack(assetManager.get("music/meow.mp3"), SoundTrackType.MENU);
        soundManager.addMusicToSoundTrack(assetManager.get("music/coffee.mp3"), SoundTrackType.MENU);
        soundManager.addMusicToSoundTrack(assetManager.get("music/waiting.mp3"), SoundTrackType.MENU);
        soundManager.addMusicToSoundTrack(assetManager.get("music/anomalocaris.mp3"), SoundTrackType.GAME);
        soundManager.addMusicToSoundTrack(assetManager.get("music/arthropluera.mp3"), SoundTrackType.GAME);
        soundManager.addMusicToSoundTrack(assetManager.get("music/caterpillar.mp3"), SoundTrackType.GAME);
        soundManager.addMusicToSoundTrack(assetManager.get("music/crocodile.mp3"), SoundTrackType.GAME);
    }

    private void initScreens(){
        screensList  = new ArrayList<>();
        screensList.add(new IntroScreen(this, assetManager.get("logo.png", Texture.class), Screens.INTRO2));
        screensList.add(new IntroScreen(this, assetManager.get("ucab.png", Texture.class), Screens.MENU));
        screensList.add(new MenuScreen(this));
        screensList.add(new MultiplayerScreen(this));
        screensList.add(new JoinScreen(this));
        screensList.add(new ServerScreen(this));
        screensList.add(new OptionScreen(this));
        screensList.add(new InfoScreen(this));
        screensList.add(new LobbyScreen(this));
        screensList.add(new ConnectingScreen(this));
        screensList.add(new GameScreen(this));
        screensList.add(new EndGameScreen(this, (GameScreen) screensList.get(Screens.GAME.ordinal())));
        screensList.add(new MiniDuckScreen(this, (GameScreen) screensList.get(Screens.GAME.ordinal())));
        screensList.add(new FireFighterScreen(this, (GameScreen) screensList.get(Screens.GAME.ordinal())));
        screensList.add(new OdsPleaseScreen(this, (GameScreen) screensList.get(Screens.GAME.ordinal())));
    }

    public void setPlayerName(String name) {
        if (name.isEmpty()) this.playerName = "Sin nombre";
        else this.playerName = name;
    }
    public String getPlayerName() {
        return playerName;
    }

    public void setIp(String ip) {
        if (ip.isEmpty()) this.ip = "localhost";
        else this.ip = ip;
    }
    public String getIp() {
        return ip;
    }

    public void setIds(int ids) {
        this.ids.set(ids);
    }
    public Integer getIds() {
        return ids.incrementAndGet();
    }

    public void setPort(Integer port) {
        this.port = port;
    }
    public Integer getPort() {
        return port;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Skin getSkin() {
        return skin;
    }

    public void changeScreen(Screens screen){
        setScreen(screensList.get(screen.ordinal()));
    }

    public void startServer(String ip, int port){
        if (server != null) closeServer();
        try{
            server = new Server((GameScreen) screensList.get(Screens.GAME.ordinal()), ip, port);
            serverThread.execute(server);
        }catch (GdxRuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    public void closeServer(){
        if (server == null)  return;
        server.close();
        serverThread.shutdown();
        serverThread = Executors.newSingleThreadExecutor();
        server = null;
    }

    public Boolean isServer(){
        return server != null;
    }

    public void startClient(String ip, int port){
        if (client != null) closeClient();
        client = new Client((GameScreen) screensList.get(Screens.GAME.ordinal()), ip, port, playerName);
        clientThread.execute(client);
    }

    public Boolean isClient(){
        return client != null;
    }

    public void closeClient(){
        if (client == null)  return;
        client.close();
        clientThread.shutdown();
        clientThread = Executors.newSingleThreadExecutor();
        client = null;
    }

    @Override
    public void render() {
        try{
            super.render();
        }catch (GdxRuntimeException e){
            System.out.println(e.getMessage());
            dispose();
        }
    }

    @Override
    public void dispose() {
        closeClient();
        closeServer();
        assetManager.dispose();
        skin.dispose();
        for (Screen screen : screensList) {
            screen.dispose();
        }
        soundManager.dispose();
        fonts.dispose();
    }
}
