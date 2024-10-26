package src.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import src.main.Main;

public class IntroScreen extends BaseScreen {
    private final SpriteBatch batch;
    private Float time, alpha;

    public IntroScreen(Main main) {
        super(main);
        batch = new SpriteBatch();
        time = 0f;
        alpha = 0f;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        time += delta;
        alpha += delta / 6;
        if (alpha > 1f) alpha = 1f;

        batch.begin();
        Texture image = main.getAssetManager().get("yoshi.jpg");
        batch.setColor(1f, 1f, 1f, alpha);
        batch.draw(image, (Gdx.graphics.getWidth() - image.getWidth())/2f,(Gdx.graphics.getHeight() - image.getHeight())/2f);
        batch.end();

        if (time > 6) main.changeScreen(Main.Screens.MENU);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
