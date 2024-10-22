package src.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import src.screens.*;
import src.screens.worldScreens.GameScreen;

import java.util.ArrayList;

public class Main extends Game {
    private AssetManager assetManager;
    private ArrayList<Screen> screensList;
    public enum Screens {
        MENU,
        GAME
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public void create() {
        assetManager = new AssetManager();
        assetManager.load("libgdx.png", Texture.class);
        assetManager.load("yoshi.jpg", Texture.class);
        assetManager.load("floor.png", Texture.class);
        System.out.println("Loading assets...");
        assetManager.finishLoading();
        System.out.println("Assets loaded.");

        screensList  = new ArrayList<>();
        screensList.add(new MenuScreen(this));
        screensList.add(new GameScreen(this));

        setScreen(screensList.get(Screens.GAME.ordinal()));
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
        for (Screen screen : screensList) {
            screen.dispose();
        }
    }
}
