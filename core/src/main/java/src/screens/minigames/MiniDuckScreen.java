package src.screens.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;
import src.screens.GameScreen;

public class MiniDuckScreen extends  MinigameScreen{
    private TextButton duck1;
    private Image background;
    private float x = 0;

    public MiniDuckScreen(Main main, GameScreen game) {
        super(main, game);

        Skin skin = main.getSkin();

        /*Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);*/

        background = new Image(main.getAssetManager().get("background/backgroundBeach.png", Texture.class));
        background.setSize(1300, 720);
        background.setZIndex(0);
        //stage.addActor(background);

        duck1 = new TextButton("Duck1", skin);
        duck1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.addScore(10);
            }
        });

        stage.addActor(duck1);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        x += 100 * delta;
        duck1.setPosition(x, 600);
        x = x % 1280;
    }
}
