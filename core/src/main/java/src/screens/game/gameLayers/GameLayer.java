package src.screens.game.gameLayers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import src.screens.components.LayersManager;

public abstract class GameLayer extends LayersManager {
    protected final GameLayerManager manager;

    protected Image pauseBg;

    public GameLayer(GameLayerManager manager, Stage stage, Integer numLayers) {
        super(stage, numLayers);
        this.manager = manager;

        pauseBg = new Image(manager.game.main.getAssetManager().get("ui/bg/whiteBg.png", Texture.class));
        setVisible(false);
    }

    public abstract void update();

}
