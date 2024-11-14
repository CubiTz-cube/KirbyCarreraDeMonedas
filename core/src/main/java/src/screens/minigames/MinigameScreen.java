package src.screens.minigames;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import src.main.Main;
import src.screens.GameScreen;
import src.screens.uiScreens.UIScreen;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public abstract class MinigameScreen extends UIScreen {
    private final GameScreen game;

    private Float timeStart;
    private Float timeGame;
    private boolean gameStarted;

    private final Image background;
    private final Label timeLabel;

    public MinigameScreen(Main main, GameScreen game) {
        super(main);
        this.game = game;
        timeStart = 0f;
        timeGame = 0f;
        gameStarted = false;

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        background = new Image(main.getAssetManager().get("background/backgroundBeach.png", Texture.class));
        background.setSize(1300, 720);
        background.setColor(1, 0, 1, 0.5f);
        stage.addActor(background);

        timeLabel = new Label("Start in 3", main.getSkin());
        timeLabel.setFontScale(6);
        timeLabel.setAlignment(Align.center);
        timeLabel.setColor(0, 0, 0, 1);
        table.add(timeLabel).expand().fill();
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    @Override
    public void show() {
        super.show();
        timeStart = 3f;
        timeGame = 5f;
        gameStarted = false;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        game.actLogic(delta);

        if (timeStart >= 1){
            timeLabel.setText("Start in " + timeStart.intValue());
            timeStart -= delta;
        }else if (timeStart > 0){
            timeLabel.setText("GO!");
            timeStart -= delta;
        } else{
            if (!gameStarted){
                gameStarted = true;
                background.remove();
            }
            timeLabel.setText("Time " + timeGame.intValue());
            timeGame -= delta;
        }

        if (timeGame <= 1){
            main.changeScreen(Main.Screens.GAME);
        }
    }
}
