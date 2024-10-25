package src.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import src.main.Main;

public class IntroScreen extends BaseScreen {
    private final SpriteBatch batch;
    private Float time;

    public IntroScreen(Main main) {
        super(main);
        batch = new SpriteBatch();
        time = 0f;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        Texture image = main.getAssetManager().get("yoshi.jpg");
        batch.draw(image, (Gdx.graphics.getWidth() - image.getWidth())/2f,(Gdx.graphics.getHeight() - image.getHeight())/2f);
        batch.end();

        time += delta;
        if (time > 3) main.changeScreen(Main.Screens.MENU);
    }
}
