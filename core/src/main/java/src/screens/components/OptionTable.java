package src.screens.components;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import src.utils.constants.MyColors;
import src.utils.sound.SingleSoundManager;
import src.utils.sound.SoundManager;

public class OptionTable {
    private final Slider volumeSlider;
    private final Slider volumenMusicSlider;
    private final Slider volumenSoundSlider;

    public OptionTable(Skin skin, Table table, BitmapFont font) {
        SoundManager soundManager = SingleSoundManager.getInstance();

        Label volumeTitleLabel = new Label("VOLUMEN",  new Label.LabelStyle(font, MyColors.BLUE));
        Label volumeLabel = new Label("General", new Label.LabelStyle(font, MyColors.BLUE));

        volumeSlider = new Slider(0, 1, 0.01f, false, skin);
        volumeSlider.setValue(soundManager.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundManager.setVolume(volumeSlider.getValue());
            }
        });

        Label volumeMusicLabel = new Label("Musica", new Label.LabelStyle(font, MyColors.BLUE));

        volumenMusicSlider = new Slider(0, 1, 0.01f, false, skin);
        volumenMusicSlider.setValue(soundManager.getVolumeMusic());
        volumenMusicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundManager.setVolumeMusic(volumenMusicSlider.getValue());
            }
        });

        Label volumeSoundLabel = new Label("Sonidos", new Label.LabelStyle(font, MyColors.BLUE));

        volumenSoundSlider = new Slider(0, 1, 0.01f, false, skin);
        volumenSoundSlider.setValue(soundManager.getVolumeSound());
        volumenSoundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundManager.setVolumeSound(volumenSoundSlider.getValue());
            }
        });

        volumeTitleLabel.setFontScale(1.2f);
        table.add(volumeTitleLabel).expandX().pad(10);
        table.row();

        table.add(volumeLabel).expandX().pad(10);
        table.row();
        table.add(volumeSlider).expandX().pad(10);
        table.row();

        table.add(volumeMusicLabel).expandX().pad(10);
        table.row();
        table.add(volumenMusicSlider).expandX().pad(10);
        table.row();

        table.add(volumeSoundLabel).expandX().pad(10);
        table.row();
        table.add(volumenSoundSlider).expandX().pad(10);
        table.row();
    }

    public void update(){
        SoundManager soundManager = SingleSoundManager.getInstance();
        volumeSlider.setValue(soundManager.getVolume());
        volumenMusicSlider.setValue(soundManager.getVolumeMusic());
        volumenSoundSlider.setValue(soundManager.getVolumeSound());
    }
}
