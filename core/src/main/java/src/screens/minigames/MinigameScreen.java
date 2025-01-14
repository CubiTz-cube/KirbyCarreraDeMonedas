package src.screens.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import src.main.Main;
import src.screens.GameScreen;
import src.screens.components.LayersManager;
import src.screens.uiScreens.UIScreen;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import src.utils.constants.PlayerControl;
import src.utils.managers.CameraShakeManager;

public abstract class MinigameScreen extends UIScreen {
    protected final GameScreen game;
    private final CameraShakeManager cameraShake;
    private Float initialX, initialY;

    private Float timeStart;
    private Float timeGame;
    private boolean gameStarted;
    private boolean skipStart;

    private final LayersManager layersManager;
    private final Image background;
    private final Label timeStartlabel;
    protected final Label timeMinigameLabel;

    /**
     * Clase base para crear Minijuegos, incluye la intruccion de 3 segundos.
     * @param main
     * @param game
     */
    public MinigameScreen(Main main, GameScreen game, String description) {
        super(main);
        this.game = game;
        timeStart = 0f;
        timeGame = 0f;
        gameStarted = false;
        skipStart = false;
        cameraShake = new CameraShakeManager((OrthographicCamera) stageUI.getCamera());

        layersManager = new LayersManager(stageUI, 2);

        timeStartlabel = new Label("Empieza en" + timeStart, new Label.LabelStyle(main.getBriFont(), null));
        timeStartlabel.setFontScale(1.2f);

        Label descriptionlabel = new Label("Pulsa cualquier tecla para omitir\n" + description, new Label.LabelStyle(main.getInterFont(), null));


        background = new Image(main.getAssetManager().get("background/backgroundBeach.png", Texture.class));
        background.setColor(1, 0, 1, 0.5f);

        layersManager.setZindex(0);
        layersManager.getLayer().center();
        layersManager.getLayer().add(timeStartlabel).expandX();
        layersManager.getLayer().row();
        layersManager.getLayer().add(descriptionlabel).expandX();

        layersManager.setZindex(1);
        layersManager.getLayer().add(background).expand().fill();

        timeMinigameLabel = new Label("Tiempo " + timeGame.intValue(), new Label.LabelStyle(main.getBriFont(), null));
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    @Override
    public void show() {
        super.show();
        this.initialX = stageUI.getCamera().position.x;
        this.initialY = stageUI.getCamera().position.y;
        timeStart = 10f;
        timeGame = 30f;
        gameStarted = false;
        skipStart = false;
        layersManager.setVisible(true);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        background.setSize(width, height);
        initialX = width / 2f;
        initialY = height / 2f;
    }

    public void endMinigame() {
        main.changeScreen(Main.Screens.GAME);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stageUI.act(delta);
        stageUI.draw();

        stageUI.getCamera().position.x = initialX;
        stageUI.getCamera().position.y = initialY;
        cameraShake.update(delta);
        game.actLogic(delta);

        if ((Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.isTouched()) && !skipStart) {timeStart = 0.99f; skipStart = true;}

        if (timeStart >= 1){
            timeStartlabel.setText("Empieza en " + timeStart.intValue());
            timeStart -= delta;
        }else if (timeStart > 0){
            timeStartlabel.setText("VAMOS!");
            timeStart -= delta;
        } else{
            if (!gameStarted){
                gameStarted = true;
                layersManager.setVisible(false);
            }
            timeMinigameLabel.setText("Tiempo " + timeGame.intValue());
            timeGame -= delta;
        }

        if (timeGame <= 1){
            endMinigame();
        }
    }

    public void shakeCamera(float duration, float intensity){
        cameraShake.addShake(duration,intensity);
    }
}
