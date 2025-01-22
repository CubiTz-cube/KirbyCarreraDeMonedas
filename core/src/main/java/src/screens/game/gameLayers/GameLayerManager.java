package src.screens.game.gameLayers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import src.screens.game.GameScreen;

public class GameLayerManager {
    private Sound pauseSound;
    private Sound pauseExitSound;

    private final Stage stage;
    public final GameScreen game;

    public enum LayerType {
        MENU
    }
    private Boolean visible;
    private GameLayer currentLayer;
    private MenuGameLayer menuGameLayer;

    public GameLayerManager(GameScreen game, Stage stage){
        this.stage = stage;
        this.game = game;
        visible = false;
        initSounds();
        initLayers();

        changeLayer(LayerType.MENU);
        setVisible(false);
    }

    private void initSounds(){
        pauseExitSound = game.main.getAssetManager().get("sound/ui/pauseExit.wav", Sound.class);
        pauseSound = game.main.getAssetManager().get("sound/ui/pause.wav", Sound.class);
    }

    private void initLayers(){
        menuGameLayer = new MenuGameLayer(this, stage);
    }

    public void changeLayer(LayerType type){
        if (currentLayer != null) currentLayer.setVisible(false);
        currentLayer = switch (type){
            case MENU -> menuGameLayer;
        };
        currentLayer.setVisible(visible);
        currentLayer.update();
    }

    public void setVisible(Boolean visible){
        this.visible = visible;
        currentLayer.setVisible(visible);
        if (visible) currentLayer.update();
    }
    public Boolean isVisible(){
        return visible;
    }

    public void setVisibleWithSound(Boolean visible){
        setVisible(visible);
        if (visible) pauseSound.play();
        else pauseExitSound.play();
    }

    public void setCenterPosition(Float x, Float y){
        currentLayer.setCenterPosition(x, y);
    }
}
