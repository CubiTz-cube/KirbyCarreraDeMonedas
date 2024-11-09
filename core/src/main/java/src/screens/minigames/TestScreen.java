package src.screens.minigames;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;
import src.screens.uiScreens.UIScreen;
import src.screens.GameScreen;

public class TestScreen extends UIScreen {
    GameScreen game;

    public TestScreen(Main main, GameScreen game) {
        super(main);
        this.game = game;

        Skin skin = main.getSkin();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton backButton = new TextButton("Volver", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.GAME);
                game.addScore(10);
            }
        });

        table.add(backButton).width(200).height(50).pad(10);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        game.actLogic(delta);
    }
}
