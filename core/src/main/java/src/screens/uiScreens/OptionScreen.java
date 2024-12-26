package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;
import src.utils.managers.SoundManager;

public class OptionScreen extends UIScreen {

    public OptionScreen(Main main) {
        super(main);
        Skin skin = main.getSkin();

        Table table = new Table();
        table.setFillParent(true);
        stageUI.addActor(table);

        TextButton backButton = new TextButton("Volver", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.MENU);
            }
        });

        Label volumeTitleLabel = new Label("VOLUMEN", skin);
        Label volumeLabel = new Label("General", skin);

        Slider volumeSlider = new Slider(0, 1, 0.01f, false, skin);
        volumeSlider.setValue(SoundManager.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setVolume(volumeSlider.getValue());
                System.out.println("Volume: " + SoundManager.getVolume());
            }
        });

        Label volumeMusicLabel = new Label("Musica", skin);

        Slider volumenMusicSlider = new Slider(0, 1, 0.01f, false, skin);
        volumenMusicSlider.setValue(SoundManager.getVolumeMusic());
        volumenMusicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setVolumeMusic(volumenMusicSlider.getValue());
                System.out.println("Volume: " + SoundManager.getVolumeMusic());
            }
        });

        Label volumeSoundLabel = new Label("Sonidos", skin);

        Slider volumenSoundSlider = new Slider(0, 1, 0.01f, false, skin);
        volumenSoundSlider.setValue(SoundManager.getVolumeSound());
        volumenSoundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setVolumeSound(volumenSoundSlider.getValue());
                System.out.println("Volume: " + SoundManager.getVolumeSound());
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

        table.add(backButton).width(200).height(50).pad(10);
    }
}
