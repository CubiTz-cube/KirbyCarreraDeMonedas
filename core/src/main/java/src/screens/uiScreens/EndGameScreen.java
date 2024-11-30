package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;

public class EndGameScreen extends UIScreen{
    Table table;

    public EndGameScreen(Main main) {
        super(main);
        Skin skin = main.getSkin();

        table = new Table();
        table.setFillParent(true);
        stageUI.addActor(table);

        TextButton backButton = new TextButton("Volver", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.MENU);
            }
        });


        table.top();
        table.add(backButton).expandX().row();
        table.add(new Label("Name", skin)).pad(10);
        table.add(new Label("Score", skin)).pad(10);
        table.row();
    }

    private void addScoreEntry(String name, Integer score) {
        table.add(new Label(name, main.getSkin())).pad(10);
        table.add(new Label(String.valueOf(score), main.getSkin())).pad(10);
        table.row();
    }
}
