package src.screens.game.gameLayers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.screens.components.OptionTable;
import src.screens.game.GameScreen;

public class MenuGameLayer extends GameLayer {
    private final OptionTable optionTable;

    private Sound pauseSound;
    private Sound pauseExitSound;

    public MenuGameLayer(Stage stage, GameScreen game){
        super(game, stage, 2);
        initSounds();

        ImageTextButton exitButton = new ImageTextButton(game.main.isClient() ? "Desconectarse": "Volver al Menu", game.myImageTextbuttonStyle);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.endGame();
            }
        });
        exitButton.addListener(game.hoverListener);

        setZindex(0);
        optionTable = new OptionTable(game.main.getSkin(), getLayer(), game.main.fonts.briFont);
        getLayer().add(exitButton).width(400).padTop(10);

        setZindex(1);
        getLayer().add(pauseBg).grow();

        setVisible(false);
    }

    private void initSounds(){
        pauseExitSound = game.main.getAssetManager().get("sound/ui/pauseExit.wav", Sound.class);
        pauseSound = game.main.getAssetManager().get("sound/ui/pause.wav", Sound.class);
    }

    @Override
    public void setVisible(Boolean visible) {
        super.setVisible(visible);
        if (visible) optionTable.update();
    }

    public void setVisibleWithSound(boolean visible){
        setVisible(visible);
        if (visible) pauseSound.play();
        else pauseExitSound.play();
    }
}
