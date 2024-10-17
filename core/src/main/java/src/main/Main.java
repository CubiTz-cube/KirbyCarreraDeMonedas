package src.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import src.screens.*;

public class Main extends Game {

    public AssetManager assetManager;

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

        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }
}
