package src.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import src.net.Client;
import src.net.Server;
import src.screens.GameScreen;
import src.screens.minigames.odsPlease.OdsPleaseScreen;
import src.screens.uiScreens.IntroScreen;
import src.screens.minigames.duckFeed.MiniDuckScreen;
import src.screens.uiScreens.*;
import src.utils.FontCreator;
import src.utils.constants.MyColors;
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
    private AtomicInteger ids;
    public Color playerColor;
    public enum Screens {
        INTRO,
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
        MINIODSPLEASE,
    }

    public Server server;
    public Client client;
    private ExecutorService serverThread = Executors.newSingleThreadExecutor();
    private ExecutorService clientThread = Executors.newSingleThreadExecutor();

    private String name;
    private String ip;
    private Integer port;

    private BitmapFont interFont;
    private BitmapFont interNameFont;
    private BitmapFont interNameFontSmall;
    private BitmapFont briFont;
    private BitmapFont briTitleFont;
    private BitmapFont briBorderFont;

    public SoundManager soundManager;
    public enum SoundTrackType {
        MENU,
        GAME,
    }

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        ids = new AtomicInteger(0);
        playerColor = new Color(Color.WHITE);

        initAssets();

        soundManager = SingleSoundManager.getInstance();
        soundManager.setVolumeMusic(0.0f);
        initSounds();
        initFonts();
        initScreens();

        changeScreen(Screens.INTRO);
        soundManager.setSoundTracks(SoundTrackType.MENU);
    }

    private void initAssets(){
        assetManager = new AssetManager();
        assetManager.load("yoshi.jpg", Texture.class);
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
        assetManager.load("logo.png", Texture.class);
        assetManager.load("ui/default.fnt", BitmapFont.class);
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
        assetManager.load("world/entities/coin.png", Texture.class);
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
        assetManager.load("world/entities/kirby/wheel/kirbyDashWheel.png", Texture.class);
        assetManager.load("world/entities/kirby/wheel/kirbyRunWheel.png", Texture.class);
        assetManager.load("world/entities/kirby/wheel/kirbyIdleWheel.png", Texture.class);
        assetManager.load("world/entities/mirror/mirrorLoop.png", Texture.class);
        assetManager.load("world/entities/basic/basicIdle.png", Texture.class);
        assetManager.load("world/entities/basic/basicWalk.png", Texture.class);
        assetManager.load("world/entities/basic/basicDamage.png", Texture.class);
        assetManager.load("world/entities/blocks/fallBlock.png", Texture.class);
        assetManager.load("world/entities/blocks/fallBlockBreak.png", Texture.class);
        assetManager.load("world/particles/cloudParticle.png", Texture.class);
        assetManager.load("world/particles/dustParticle.png", Texture.class);
        assetManager.load("world/particles/starParticle.png", Texture.class);
        assetManager.load("world/particles/swordParticle.png", Texture.class);
        assetManager.load("world/particles/iceParticle.png", Texture.class);
        assetManager.load("world/particles/bombParticle.png", Texture.class);
        assetManager.load("world/particles/kirbySwordParticle.png", Texture.class);
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
        assetManager.load("sound/kirby/kirbyAbsorb1.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyAbsorb2.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyDash.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyFireDamage.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyHeavyFall.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyJump.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyNormalDamage.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyPower.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyScore1.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyScore2.wav", Sound.class);
        assetManager.load("sound/kirby/kirbySleep.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyStar.wav", Sound.class);
        assetManager.load("sound/kirby/kirbyRemovePower.wav", Sound.class);
        assetManager.load("sound/bomb.wav", Sound.class);
        assetManager.load("sound/introLogo.wav", Sound.class);
        assetManager.load("sound/portalChange.wav", Sound.class);
        assetManager.load("sound/enemy/enemyDamage.wav", Sound.class);
        assetManager.load("sound/enemy/enemyDead.wav", Sound.class);
        System.out.println("Loading assets...");
        assetManager.finishLoading();
        System.out.println("Assets loaded.");
    }

    private void initFonts(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Bricolage_Grotesque/BricolageGrotesque_48pt-Regular.ttf"));
        briFont = FontCreator.createFont(48, Color.WHITE, generator, new FreeTypeFontGenerator.FreeTypeFontParameter());

        generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Inter/Inter_28pt-Regular.ttf"));
        interFont = FontCreator.createFont(40, Color.WHITE, generator, new FreeTypeFontGenerator.FreeTypeFontParameter());

        generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Bricolage_Grotesque/BricolageGrotesque_48pt-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderWidth = 4;
        parameter.borderColor = MyColors.BLUE;
        parameter.shadowColor = MyColors.BLUE;
        parameter.shadowOffsetX = -2;
        parameter.shadowOffsetY = 2;
        briTitleFont = FontCreator.createFont(48, MyColors.YELLOW, generator, parameter);
        parameter.borderColor = Color.BLACK;
        parameter.shadowColor = null;
        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;
        briBorderFont = FontCreator.createFont(48, Color.WHITE, generator, parameter);

        generator= new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Inter/Inter_28pt-Regular.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderWidth = 3;
        parameter.borderColor = Color.BLACK;
        interNameFont = FontCreator.createFont(32, MyColors.PINK, generator, parameter);
        parameter.borderWidth = 2;
        interNameFontSmall = FontCreator.createFont(14, MyColors.PINK, generator, parameter);
    }

    private void initSounds(){
        soundManager.addSoundTrack(SoundTrackType.MENU);
        soundManager.addMusicToSoundTrack(assetManager.get("music/meow.mp3"), SoundTrackType.MENU);
        soundManager.addMusicToSoundTrack(assetManager.get("music/anomalocaris.mp3"), SoundTrackType.MENU);
        soundManager.addMusicToSoundTrack(assetManager.get("music/arthropluera.mp3"), SoundTrackType.MENU);
        soundManager.addMusicToSoundTrack(assetManager.get("music/caterpillar.mp3"), SoundTrackType.MENU);
        soundManager.addMusicToSoundTrack(assetManager.get("music/crocodile.mp3"), SoundTrackType.MENU);
        soundManager.addMusicToSoundTrack(assetManager.get("music/coffee.mp3"), SoundTrackType.MENU);
        soundManager.addMusicToSoundTrack(assetManager.get("music/waiting.mp3"), SoundTrackType.MENU);
    }

    private void initScreens(){
        screensList  = new ArrayList<>();
        screensList.add(new IntroScreen(this));
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
        screensList.add(new OdsPleaseScreen(this, (GameScreen) screensList.get(Screens.GAME.ordinal())));
    }

    public BitmapFont getBriFont() {
        return briFont;
    }

    public BitmapFont getInterFont() {
        return interFont;
    }

    public BitmapFont getBriTitleFont() {
        return briTitleFont;
    }

    public BitmapFont getInterNameFont() {
        return interNameFont;
    }

    public BitmapFont getInterNameFontSmall() {
        return interNameFontSmall;
    }

    public BitmapFont getBriBorderFont() {
        return briBorderFont;
    }

    public void setName(String name) {
        if (name.isEmpty()) this.name = "Sin nombre";
        else this.name = name;
    }

    public String getName() {
        return name;
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

    public void startClient(String ip, int port){
        if (client != null) closeClient();
        client = new Client((GameScreen) screensList.get(Screens.GAME.ordinal()), ip, port, name);
        clientThread.execute(client);
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
        super.render();
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
        interFont.dispose();
        interNameFont.dispose();
        interNameFontSmall.dispose();
        briFont.dispose();
        briTitleFont.dispose();
        briBorderFont.dispose();
    }
}
