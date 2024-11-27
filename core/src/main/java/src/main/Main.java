package src.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import src.net.Client;
import src.net.Server;
import src.screens.GameScreen;
import src.screens.uiScreens.IntroScreen;
import src.screens.minigames.*;
import src.screens.minigames.duckFeed.MiniDuckScreen;
import src.screens.uiScreens.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Game {
    private AssetManager assetManager;
    private ArrayList<Screen> screensList;
    private Skin skin;
    private int ids = 0;
    private String name;
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
        MINIDUCK,
    }

    public Server server;
    public Client client;
    private ExecutorService serverThread = Executors.newSingleThreadExecutor();
    private ExecutorService clientThread = Executors.newSingleThreadExecutor();

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        assetManager = new AssetManager();
        assetManager.load("libgdx.png", Texture.class);
        assetManager.load("yoshi.jpg", Texture.class);
        assetManager.load("perro.jpg", Texture.class);
        assetManager.load("poshi.jpg", Texture.class);
        assetManager.load("yozhi.jpg", Texture.class);
        assetManager.load("yoshiSword.png", Texture.class);
        assetManager.load("logo.png", Texture.class);
        assetManager.load("ui/default.fnt", BitmapFont.class);
        assetManager.load("world/entities/breakBlock.png", Texture.class);
        assetManager.load("background/backgroundBeach.png", Texture.class);
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
        assetManager.load("world/entities/kirby/absorb/kirbyAbsorbIdle.png", Texture.class);
        assetManager.load("world/entities/kirby/absorb/kirbyAbsorbWalk.png", Texture.class);
        assetManager.load("world/entities/kirby/absorb/kirbyAbsorbFall.png", Texture.class);
        assetManager.load("world/entities/kirby/absorb/kirbyAbsorbJump.png", Texture.class);
        assetManager.load("world/entities/mirror/mirrorLoop.png", Texture.class);
        assetManager.load("world/entities/basic/basicIdle.png", Texture.class);
        assetManager.load("world/entities/basic/basicWalk.png", Texture.class);
        assetManager.load("world/particles/cloudParticle.png", Texture.class);
        System.out.println("Loading assets...");
        assetManager.finishLoading();
        System.out.println("Assets loaded.");

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
        screensList.add(new MiniDuckScreen(this, (GameScreen) screensList.get(Screens.GAME.ordinal())));

        changeScreen(Screens.GAME);
    }

    public void setName(String name) {
        if (name.isEmpty()) this.name = "Sin nombre";
        else this.name = name;
    }

    public Integer getIds() {
        return ++ids;
    }

    public void setIds(int ids) {
        this.ids = ids;
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
        server = new Server((GameScreen) screensList.get(Screens.GAME.ordinal()), ip, port);
        serverThread.execute(server);
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
        assetManager.dispose();
        skin.dispose();
        for (Screen screen : screensList) {
            screen.dispose();
        }
        closeServer();
        closeClient();
    }
}
