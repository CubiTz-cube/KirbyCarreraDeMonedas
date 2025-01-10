package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import src.main.Main;
import src.screens.GameScreen;
import src.screens.components.LayersManager;
import src.utils.ScorePlayer;
import java.util.ArrayList;
import java.util.Collections;

public class EndGameScreen extends UIScreen{
    private final LayersManager layersManager;
    private final GameScreen game;

    private final Label.LabelStyle labelStyle;
    private final Label.LabelStyle labelTitleStyle;

    private final Label titleLabel;
    private final Image pinkLineImage;
    private final ImageTextButton backButton;

    public EndGameScreen(Main main, GameScreen game) {
        super(main);
        this.game = game;

        labelTitleStyle = new Label.LabelStyle(main.getBriBorderFont(), Color.WHITE);
        labelStyle = new Label.LabelStyle(main.getInterFont(), Color.WHITE);

        titleLabel = new Label("Partida", new Label.LabelStyle(main.getBriTitleFont(), Color.WHITE));
        titleLabel.setAlignment(Align.center);

        Texture pinkLineTexture = main.getAssetManager().get("ui/bg/pinkLineBg.png", Texture.class);
        pinkLineTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pinkLineImage = new Image(pinkLineTexture);

        backButton = new ImageTextButton("Volver", myImageTextbuttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.MENU);
            }
        });

        layersManager = new LayersManager(stageUI, 5);
    }

    @Override
    public void show() {
        super.show();
        layersManager.setZindex(0);
        layersManager.getLayer().bottom().pad(10);
        layersManager.getLayer().add().expand(5,0);
        layersManager.getLayer().add(backButton).growX();

        layersManager.setZindex(1);
        layersManager.getLayer().top().padTop(100);
        layersManager.getLayer().add(new Label("Nombre", labelTitleStyle)).expandX().center();
        layersManager.getLayer().add(new Label("Monedas", labelTitleStyle)).expandX().center();
        layersManager.getLayer().row();
        ArrayList<ScorePlayer> scores = new ArrayList<>(game.getScorePlayers().values());
        Collections.sort(scores);
        for (ScorePlayer score : scores){
            addScoreEntry(score.name, score.score);
        }

        layersManager.setZindex(3);
        layersManager.getLayer().top();
        layersManager.getLayer().add(titleLabel).padTop(15).width(398).center();
        layersManager.getLayer().add().expandX();

        layersManager.setZindex(4);
        layersManager.getLayer().top();
        layersManager.getLayer().add(pinkLineImage).padTop(50).expandX().left();
    }

    @Override
    public void hide() {
        super.hide();
        layersManager.getLayer().clear();
        game.getScorePlayers().clear();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.97f,0.55f,0.72f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stageUI.act(delta);
        stageUI.draw();
    }

    private void addScoreEntry(String name, Integer score) {
        layersManager.getLayer().add(new Label(name, labelStyle)).expandX().center();
        layersManager.getLayer().add(new Label(String.valueOf(score), labelStyle)).expandX().center();
        layersManager.getLayer().row();
    }
}
