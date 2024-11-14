package src.screens.minigames;

import com.badlogic.gdx.utils.Align;
import src.main.Main;
import src.screens.GameScreen;
import src.screens.uiScreens.UIScreen;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public abstract class MinigameScreen extends UIScreen {
    private final GameScreen game;
    private Float time;
    private final Label timeLabel;

    public MinigameScreen(Main main, GameScreen game) {
        super(main);
        this.game = game;
        time = 0f;
        timeLabel = new Label("Time: 0", main.getSkin());
        timeLabel.setAlignment(Align.topLeft);
        stage.addActor(timeLabel);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        game.actLogic(delta);
        timeLabel.setText("Time: " + time);
        System.out.println(time);
        time += delta;
        if (time > 15) {
            main.changeScreen(Main.Screens.GAME);
        }
    }
}
