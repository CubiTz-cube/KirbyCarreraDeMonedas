package src.screens.game.gameLayers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.screens.components.LayersManager;
import src.screens.game.GameScreen;

public class DebugGameLayer extends GameLayer{
    private final Slider playerMaxSpeedSlider;

    public DebugGameLayer(GameLayerManager gameLayerManager, Stage stage){
        super(gameLayerManager, stage, 2);

        playerMaxSpeedSlider = new Slider(0, 1, 0.01f, false, manager.game.main.getSkin());
        playerMaxSpeedSlider.setValue(manager.game.getPlayer().maxSpeed);
        playerMaxSpeedSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Player max speed: " + playerMaxSpeedSlider.getValue());
            }
        });

        ImageTextButton backButton = new ImageTextButton(manager.game.main.isClient() ? "Desconectarse": "Volver al Menu", manager.game.myImageTextbuttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                manager.changeLayer(GameLayerManager.LayerType.MENU);
            }
        });
        backButton.addListener(manager.game.hoverListener);

        setZindex(0);
        getLayer().add(playerMaxSpeedSlider).width(400).padTop(10);
        getLayer().row();
        getLayer().add(backButton).width(400).padTop(10);

        setZindex(1);
        getLayer().add(pauseBg).grow();

    }

    @Override
    public void update() {

    }
}
