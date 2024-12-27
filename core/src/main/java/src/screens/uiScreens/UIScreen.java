package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import src.main.Main;
import src.screens.BaseScreen;

public abstract class UIScreen extends BaseScreen {
    protected final Stage stageUI;

    public UIScreen(Main main) {
        super(main);
        stageUI = new Stage(new ScreenViewport());
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(stageUI);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stageUI.act(delta);
        stageUI.draw();
    }

    @Override
    public void resize(int width, int height) {
        stageUI.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stageUI.dispose();
    }
}
