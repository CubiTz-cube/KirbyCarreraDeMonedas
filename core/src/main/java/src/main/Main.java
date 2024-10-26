package src.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import src.screens.IntroScreen;
import src.screens.uiScreens.*;
import src.screens.worldScreens.*;

import java.util.ArrayList;

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
        LOBBY,
        CONNECTING,
        GAME
    }

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        assetManager = new AssetManager();
        assetManager.load("libgdx.png", Texture.class);
        assetManager.load("yoshi.jpg", Texture.class);
        assetManager.load("floor.png", Texture.class);
        System.out.println("Loading assets...");
        assetManager.finishLoading();
        System.out.println("Assets loaded.");

        screensList  = new ArrayList<>();
        screensList.add(new IntroScreen(this));
        screensList.add(new MenuScreen(this));
        screensList.add(new MultiplayerScreen(this));
        screensList.add(new OptionScreen(this));
        screensList.add(new InfoScreen(this));
        screensList.add(new LobbyScreen(this));
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
    }
}
