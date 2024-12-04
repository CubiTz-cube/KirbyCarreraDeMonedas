package src.screens.minigames;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import src.main.Main;
import src.screens.GameScreen;
import src.screens.uiScreens.UIScreen;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public abstract class MinigameScreen extends UIScreen {
    private final GameScreen game;

    private Float timeStart;
    private Float timeGame;
    private boolean gameStarted;

    private final Table backTable;
    private final Table frontTable;
    private final Image background;
    private final Label timeStartlabel;
    protected final Label timeMinigameLabel;

    /**
     * Clase base para crear Minijuegos, incluye la intruccion de 3 segundos.
     * @param main
     * @param game
     */
    public MinigameScreen(Main main, GameScreen game) {
        super(main);
        this.game = game;
        timeStart = 0f;
        timeGame = 0f;
        gameStarted = false;

        backTable = new Table();
        backTable.setFillParent(true);
        frontTable = new Table();
        frontTable.setFillParent(true);
        stageUI.addActor(backTable);
        stageUI.addActor(frontTable);

        background = new Image(main.getAssetManager().get("background/backgroundBeach.png", Texture.class));
        background.setColor(1, 0, 1, 0.5f);
        backTable.add(background).expand().fill();

        timeStartlabel = new Label("Empieza en" + timeStart, main.getSkin());
        timeStartlabel.setFontScale(3);

        timeMinigameLabel = new Label("Tiempo " + timeGame.intValue(), main.getSkin());

        frontTable.center().add(timeStartlabel);
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    @Override
    public void show() {
        super.show();
        timeStart = 0f;
        timeGame = 15f;
        gameStarted = false;
        frontTable.setVisible(true);
        backTable.setVisible(true);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        background.setSize(width, height);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        game.actLogic(delta);

        if (timeStart >= 1){
            timeStartlabel.setText("Empieza en " + timeStart.intValue());
            timeStart -= delta;
        }else if (timeStart > 0){
            timeStartlabel.setText("VAMOS!");
            timeStart -= delta;
        } else{
            if (!gameStarted){
                gameStarted = true;
                frontTable.setVisible(false);
                backTable.setVisible(false);
            }
            timeMinigameLabel.setText("Tiempo " + timeGame.intValue());
            timeGame -= delta;
        }

        if (timeGame <= 1){
            main.changeScreen(Main.Screens.GAME);
        }
    }
}
