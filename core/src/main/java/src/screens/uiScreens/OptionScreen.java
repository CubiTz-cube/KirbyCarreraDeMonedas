package src.screens.uiScreens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;
import src.screens.components.LayersManager;
import src.utils.constants.MyColors;
import src.utils.sound.SingleSoundManager;
import src.utils.sound.SoundManager;

public class OptionScreen extends BlueCircleScreen {

    public OptionScreen(Main main) {
        super(main, "Ajustes", null, Main.Screens.MENU);
        Skin skin = main.getSkin();

        LayersManager layersManager = new LayersManager(stageUI, 3);

        SoundManager soundManager = SingleSoundManager.getInstance();

        Label volumeTitleLabel = new Label("VOLUMEN",  new Label.LabelStyle(fontBri, MyColors.BLUE));
        Label volumeLabel = new Label("General", new Label.LabelStyle(fontBri, MyColors.BLUE));

        Slider volumeSlider = new Slider(0, 1, 0.01f, false, skin);
        volumeSlider.setValue(soundManager.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundManager.setVolume(volumeSlider.getValue());
            }
        });

        Label volumeMusicLabel = new Label("Musica", new Label.LabelStyle(fontBri, MyColors.BLUE));

        Slider volumenMusicSlider = new Slider(0, 1, 0.01f, false, skin);
        volumenMusicSlider.setValue(soundManager.getVolumeMusic());
        volumenMusicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundManager.setVolumeMusic(volumenMusicSlider.getValue());
            }
        });

        Label volumeSoundLabel = new Label("Sonidos", new Label.LabelStyle(fontBri, MyColors.BLUE));

        Slider volumenSoundSlider = new Slider(0, 1, 0.01f, false, skin);
        volumenSoundSlider.setValue(soundManager.getVolumeSound());
        volumenSoundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundManager.setVolumeSound(volumenSoundSlider.getValue());
            }
        });

        layersManager.setZindex(0);
        layersManager.getLayer().setDebug(true);
        volumeTitleLabel.setFontScale(1.2f);
        layersManager.getLayer().add(volumeTitleLabel).expandX().pad(10);
        layersManager.getLayer().row();

        layersManager.getLayer().add(volumeLabel).expandX().pad(10);
        layersManager.getLayer().row();
        layersManager.getLayer().add(volumeSlider).expandX().pad(10);
        layersManager.getLayer().row();

        layersManager.getLayer().add(volumeMusicLabel).expandX().pad(10);
        layersManager.getLayer().row();
        layersManager.getLayer().add(volumenMusicSlider).expandX().pad(10);
        layersManager.getLayer().row();

        layersManager.getLayer().add(volumeSoundLabel).expandX().pad(10);
        layersManager.getLayer().row();
        layersManager.getLayer().add(volumenSoundSlider).expandX().pad(10);
        layersManager.getLayer().row();
    }
}
