package src.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import src.net.Client;
import src.net.Server;
import src.screens.IntroScreen;
import src.screens.uiScreens.*;
import src.screens.worldScreens.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Game {
    private AssetManager assetManager;
    private ArrayList<Screen> screensList;
    private Skin skin;
    public enum Screens {
        INTRO,
        MENU,
        MULTIPLAYER,
        OPTION,
        INFO,
        LOBBYSERVER,
        LOBBYCLIENT,
        CONNECTING,
        GAME
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
        assetManager.load("floor.png", Texture.class);
        assetManager.load("world/entities/kirby/kirbyWalk.png", Texture.class);
        System.out.println("Loading assets...");
        assetManager.finishLoading();
        System.out.println("Assets loaded.");

        screensList  = new ArrayList<>();
        screensList.add(new IntroScreen(this));
        screensList.add(new MenuScreen(this));
        screensList.add(new MultiplayerScreen(this));
        screensList.add(new OptionScreen(this));
        screensList.add(new InfoScreen(this));
        screensList.add(new LobbyServerScreen(this));
        screensList.add(new LobbyClientScreen(this));
        screensList.add(new ConnectingScreen(this));
        screensList.add(new GameScreen(this));

        changeScreen(Screens.MENU);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Skin getSkin() {
        return skin;
    }

    public void changeScreen(Screens screen){
        setScreen(screensList.get(screen.ordinal()));
        //System.out.println("Screen changed to: " + screen);
    }

    public void startServer(){
        if (server != null) closeServer();
        server = new Server((GameScreen) screensList.get(Screens.GAME.ordinal()), "localhost", 38746);
        serverThread.execute(server);
    }

    public void closeServer(){
        if (server == null)  return;
        server.close();
        serverThread.shutdown();
        serverThread = Executors.newSingleThreadExecutor();
        server = null;
    }

    public void startClient(String name){
        if (client != null) closeClient();
        client = new Client((GameScreen) screensList.get(Screens.GAME.ordinal()), "bin-hansen.gl.at.ply.gg", 38746, name);
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
