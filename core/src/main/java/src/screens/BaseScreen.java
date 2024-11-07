package src.screens;

import com.badlogic.gdx.Screen;
import src.main.Main;

public abstract class BaseScreen implements Screen {
    public Main main;

    public BaseScreen(Main main) {
        this.main = main;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
