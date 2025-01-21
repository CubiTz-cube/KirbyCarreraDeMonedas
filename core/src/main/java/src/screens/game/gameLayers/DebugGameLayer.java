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

    public DebugGameLayer(Stage stage, GameScreen game){
        super(game, stage, 2);

        playerMaxSpeedSlider = new Slider(0, 1, 0.01f, false, game.main.getSkin());
        playerMaxSpeedSlider.setValue(game.getPlayer().maxSpeed);
        playerMaxSpeedSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Player max speed: " + playerMaxSpeedSlider.getValue());
            }
        });

        setZindex(0);
        getLayer().add(playerMaxSpeedSlider).width(400).padTop(10);

        setZindex(1);
        getLayer().add(pauseBg).grow();

    }

    public void updateValues(){

    }
}
