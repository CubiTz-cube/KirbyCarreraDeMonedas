package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

        Slider volumeSlider = new Slider(0, 1, 0.01f, false, skin);
        volumeSlider.setValue(SoundManager.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setVolume(volumeSlider.getValue());
                System.out.println("Volume: " + SoundManager.getVolume());
            }
        });

        table.add(volumeSlider).width(200).height(50).pad(10);
        table.row();
        table.add(backButton).width(200).height(50).pad(10);
    }
}
