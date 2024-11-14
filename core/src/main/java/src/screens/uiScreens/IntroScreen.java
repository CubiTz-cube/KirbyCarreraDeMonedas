package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import src.main.Main;

public class IntroScreen extends UIScreen {
    private Float time, alpha;
    private final Image logo;

    public IntroScreen(Main main) {
        super(main);
        time = 0f;
        alpha = -0.1f;

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        logo = new Image(main.getAssetManager().get("logo.png", Texture.class));
        table.add(logo).width(logo.getWidth()/2).height(logo.getHeight()/2);
        logo.setColor(1f, 1f, 1f, alpha);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        time += delta;
        alpha += delta / 6;
        if (alpha > 1f) alpha = 1f;

        logo.setColor(1f, 1f, 1f, alpha);

        if (time > 6 || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) main.changeScreen(Main.Screens.MENU);
    }
}
