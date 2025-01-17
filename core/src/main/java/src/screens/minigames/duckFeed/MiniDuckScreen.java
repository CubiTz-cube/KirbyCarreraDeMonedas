package src.screens.minigames.duckFeed;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;
import src.screens.GameScreen;
import src.screens.minigames.MinigameScreen;

public class MiniDuckScreen extends MinigameScreen {
    private TextButton duck1;
    private Image background;
    private float x = 0;

    public MiniDuckScreen(Main main, GameScreen game) {
        super(main, game, "Patos!", "En contruccion");

        Skin skin = main.getSkin();

        /*Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);*/

        background = new Image(main.getAssetManager().get("background/backgroundBeach.png", Texture.class));
        background.setSize(1300, 720);
        stageUI.addActor(background);
        background.toBack();

        duck1 = new TextButton("Duck1", skin);
        duck1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScore(game.getScore() + 1);
            }
        });

        stageUI.addActor(duck1);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!isGameStarted()) return;

        x += 100 * delta;
        duck1.setPosition(x, 600);
        x = x % 1280;
    }
}
