package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;
import src.screens.GameScreen;
import src.screens.components.LayersManager;
import src.utils.ScorePlayer;

import java.util.ArrayList;
import java.util.Collections;

public class EndGameScreen extends UIScreen{
    private final Table table;
    private final GameScreen game;

    private final TextButton backButton;

    public EndGameScreen(Main main, GameScreen game) {
        super(main);
        this.game = game;

        table = new Table();
        table.setFillParent(true);
        stageUI.addActor(table);

        backButton = new TextButton("Volver", main.getSkin());
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.MENU);
            }
        });
    }

    @Override
    public void show() {
        super.show();
        table.top();
        table.add().expandX();
        table.add(backButton).expandX().fillX().prefHeight(50);
        table.add().expandX();
        table.row();
        table.add().expandX();
        table.add(new Label("Name", main.getSkin())).pad(10);
        table.add(new Label("Score", main.getSkin())).pad(10);
        table.add().expandX();
        table.row();
        ArrayList<ScorePlayer> scores = new ArrayList<>(game.getScorePlayers().values());
        Collections.sort(scores);
        for (ScorePlayer score : scores){
            addScoreEntry(score.name, score.score);
        }
    }

    @Override
    public void hide() {
        super.hide();
        table.clear();
        game.getScorePlayers().clear();
    }

    private void addScoreEntry(String name, Integer score) {
        table.add().expandX();
        table.add(new Label(name, main.getSkin())).pad(10);
        table.add(new Label(String.valueOf(score), main.getSkin())).pad(10);
        table.add().expandX();
        table.row();
    }
}
