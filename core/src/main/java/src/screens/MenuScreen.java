package src.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import src.main.Main;

public class MenuScreen extends BaseScreen {
    public MenuScreen(Main main) {
        super(main);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.justTouched()) {
            main.changeScreen(Main.Screens.GAME);
        }
    }
}
