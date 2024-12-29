package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;
import src.utils.sound.SingleSoundManager;
import src.utils.sound.SoundManager;

public class OptionScreen extends BlueCircleScreen {

    public OptionScreen(Main main) {
        super(main, "Ajustes", null, Main.Screens.MENU);
        Skin skin = main.getSkin();

        SoundManager soundManager = SingleSoundManager.getInstance();

        Table table = new Table();
        table.setFillParent(true);
        stageUI.addActor(table);

        Label volumeTitleLabel = new Label("VOLUMEN", skin);
        Label volumeLabel = new Label("General", skin);

        Slider volumeSlider = new Slider(0, 1, 0.01f, false, skin);
        volumeSlider.setValue(soundManager.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundManager.setVolume(volumeSlider.getValue());
            }
        });

        Label volumeMusicLabel = new Label("Musica", skin);

        Slider volumenMusicSlider = new Slider(0, 1, 0.01f, false, skin);
        volumenMusicSlider.setValue(soundManager.getVolumeMusic());
        volumenMusicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundManager.setVolumeMusic(volumenMusicSlider.getValue());
            }
        });

        Label volumeSoundLabel = new Label("Sonidos", skin);

        Slider volumenSoundSlider = new Slider(0, 1, 0.01f, false, skin);
        volumenSoundSlider.setValue(soundManager.getVolumeSound());
        volumenSoundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundManager.setVolumeSound(volumenSoundSlider.getValue());
            }
        });

        volumeTitleLabel.setFontScale(2);
        table.add(volumeTitleLabel).width(200).height(50).pad(10);
        table.row();

        table.add(volumeLabel).width(200).height(50).pad(10);
        table.row();
        table.add(volumeSlider).width(200).height(50).pad(10);
        table.row();

        table.add(volumeMusicLabel).width(200).height(50).pad(10);
        table.row();
        table.add(volumenMusicSlider).width(200).height(50).pad(10);
        table.row();

        table.add(volumeSoundLabel).width(200).height(50).pad(10);
        table.row();
        table.add(volumenSoundSlider).width(200).height(50).pad(10);
        table.row();
    }
}
