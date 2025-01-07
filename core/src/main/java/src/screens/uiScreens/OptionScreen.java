package src.screens.uiScreens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;
import src.screens.components.LayersManager;
import src.screens.components.OptionTable;
import src.utils.constants.MyColors;
import src.utils.sound.SingleSoundManager;
import src.utils.sound.SoundManager;

public class OptionScreen extends BlueCircleScreen {
    private final OptionTable optionTable;;

    public OptionScreen(Main main) {
        super(main, "Ajustes", null, Main.Screens.MENU);
        Skin skin = main.getSkin();

        LayersManager layersManager = new LayersManager(stageUI, 1);

        layersManager.setZindex(0);
        optionTable = new OptionTable(skin, layersManager.getLayer(), main.getBriFont());
    }

    @Override
    public void show() {
        super.show();
        optionTable.update();
    }
}
