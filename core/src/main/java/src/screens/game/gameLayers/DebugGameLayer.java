package src.screens.game.gameLayers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.screens.components.LayersManager;
import src.screens.components.SliderText;
import src.screens.game.GameScreen;
import src.world.entities.player.PlayerCommon;

import java.util.ArrayList;

public class DebugGameLayer extends GameLayer{
    private final ArrayList<SliderText> debugSliderList;

    public DebugGameLayer(GameLayerManager gameLayerManager, Stage stage){
        super(gameLayerManager, stage, 2);
        debugSliderList = new ArrayList<>();

        ImageTextButton backButton = new ImageTextButton(manager.game.main.isClient() ? "Desconectarse": "Volver al Menu", manager.game.myImageTextbuttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                manager.changeLayer(GameLayerManager.LayerType.MENU);
            }
        });
        backButton.addListener(manager.game.hoverListener);

        setZindex(0);
        for (SliderText slider : debugSliderList){
            getLayer().add(slider).width(400).padTop(10);
            getLayer().row();
        }
        getLayer().add(backButton).width(400).padTop(10);

        setZindex(1);
        getLayer().add(pauseBg).grow();

    }

    private void initSliders(){
        Label.LabelStyle labelStyle = new Label.LabelStyle(manager.game.main.fonts.interFont, Color.BLACK);
        SliderText newSliderText;

        debugSliderList.add(new SliderText(1, 20, 1f, false, manager.game.main.getSkin(),
            "Daño del Dash"));
        debugSliderList.get(debugSliderList.size()-1).getLabel().setStyle(labelStyle);
        debugSliderList.get(debugSliderList.size()-1).setValue((float)PlayerCommon.DEFAULT_DASH_DAMAGE);
        debugSliderList.get(debugSliderList.size()-1).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PlayerCommon.DEFAULT_DASH_DAMAGE = debugSliderList.get(debugSliderList.size()-1).getValue().intValue();
            }
        });

        debugSliderList.add(new SliderText(1, 20, 1f, false, manager.game.main.getSkin(),
            "Fuerza de Dash"));
        debugSliderList.get(debugSliderList.size()-1).getLabel().setStyle(labelStyle);
        debugSliderList.get(debugSliderList.size()-1).setValue(PlayerCommon.DASH_IMPULSE);
        debugSliderList.get(debugSliderList.size()-1).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PlayerCommon.DASH_IMPULSE = debugSliderList.get(debugSliderList.size()-1).getValue();
            }
        });

        debugSliderList.add(new SliderText(1, 20, 1f, false, manager.game.main.getSkin(),
            "Aceleracion Caminar"));
        debugSliderList.get(debugSliderList.size()-1).getLabel().setStyle(labelStyle);
        debugSliderList.get(debugSliderList.size()-1).setValue(PlayerCommon.WALK_SPEED);
        debugSliderList.get(debugSliderList.size()-1).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PlayerCommon.WALK_SPEED = debugSliderList.get(debugSliderList.size()-1).getValue();
            }
        });

        debugSliderList.add(new SliderText(1, 20, 1f, false, manager.game.main.getSkin(),
            "Velocidad Maxima Caminar"));
        debugSliderList.get(debugSliderList.size()-1).getLabel().setStyle(labelStyle);
        debugSliderList.get(debugSliderList.size()-1).setValue(PlayerCommon.WALK_MAX_SPEED);
        debugSliderList.get(debugSliderList.size()-1).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PlayerCommon.WALK_MAX_SPEED = debugSliderList.get(debugSliderList.size()-1).getValue();
            }
        });

        debugSliderList.add(new SliderText(1, 20, 1f, false, manager.game.main.getSkin(),
            "Aceleracion Correr"));
        debugSliderList.get(debugSliderList.size()-1).getLabel().setStyle(labelStyle);
        debugSliderList.get(debugSliderList.size()-1).setValue(PlayerCommon.RUN_SPEED);
        debugSliderList.get(debugSliderList.size()-1).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PlayerCommon.RUN_SPEED = debugSliderList.get(debugSliderList.size()-1).getValue();
            }
        });

        debugSliderList.add(new SliderText(1, 20, 1f, false, manager.game.main.getSkin(),
            "Velocidad Maxima Correr"));
        debugSliderList.get(debugSliderList.size()-1).getLabel().setStyle(labelStyle);
        debugSliderList.get(debugSliderList.size()-1).setValue(PlayerCommon.RUN_MAX_SPEED);
        debugSliderList.get(debugSliderList.size()-1).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PlayerCommon.RUN_MAX_SPEED = debugSliderList.get(debugSliderList.size()-1).getValue();
            }
        });

        debugSliderList.add(new SliderText(1, 20, 1f, false, manager.game.main.getSkin(),
            "Tiempo Salto Sostenido"));
        debugSliderList.get(debugSliderList.size()-1).getLabel().setStyle(labelStyle);
        debugSliderList.get(debugSliderList.size()-1).setValue(PlayerCommon.MAX_JUMP_TIME);
        debugSliderList.get(debugSliderList.size()-1).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PlayerCommon.MAX_JUMP_TIME = debugSliderList.get(debugSliderList.size()-1).getValue();
            }
        });

        debugSliderList.add(new SliderText(1, 20, 1f, false, manager.game.main.getSkin(),
            "Fuerza de Salto"));
        debugSliderList.get(debugSliderList.size()-1).getLabel().setStyle(labelStyle);
        debugSliderList.get(debugSliderList.size()-1).setValue(PlayerCommon.JUMP_IMPULSE);
        debugSliderList.get(debugSliderList.size()-1).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PlayerCommon.JUMP_IMPULSE = debugSliderList.get(debugSliderList.size()-1).getValue();
            }
        });

        debugSliderList.add(new SliderText(1, 20, 1f, false, manager.game.main.getSkin(),
            "Fuerza de Salto Sostenido"));
        debugSliderList.get(debugSliderList.size()-1).getLabel().setStyle(labelStyle);
        debugSliderList.get(debugSliderList.size()-1).setValue(PlayerCommon.JUMP_INAIR);
        debugSliderList.get(debugSliderList.size()-1).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PlayerCommon.JUMP_INAIR = debugSliderList.get(debugSliderList.size()-1).getValue();
            }
        });

        debugSliderList.add(new SliderText(1, 20, 1f, false, manager.game.main.getSkin(),
            "Fuerza de Absorcion"));
        debugSliderList.get(debugSliderList.size()-1).getLabel().setStyle(labelStyle);
        debugSliderList.get(debugSliderList.size()-1).setValue(PlayerCommon.ABSORB_FORCE);
        debugSliderList.get(debugSliderList.size()-1).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PlayerCommon.ABSORB_FORCE = debugSliderList.get(debugSliderList.size()-1).getValue();
            }
        });
    }

    @Override
    public void update() {

    }
}
