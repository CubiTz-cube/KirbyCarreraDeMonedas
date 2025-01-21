package src.screens.game.gameLayers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import src.screens.components.LayersManager;
import src.screens.game.GameScreen;

public class GameLayer extends LayersManager {
    protected final GameScreen game;

    protected Image pauseBg;

    public GameLayer(GameScreen game, Stage stage, Integer numLayers) {
        super(stage, numLayers);
        this.game = game;

        pauseBg = new Image(game.main.getAssetManager().get("ui/bg/whiteBg.png", Texture.class));
    }

}
