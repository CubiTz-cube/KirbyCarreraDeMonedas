package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import src.main.Main;
import src.screens.components.LayersManager;
import src.screens.components.OptionTable;

public class XprOptionScreen extends BlueCircleScreen {
    private final OptionTable optionTable;;

    public XprOptionScreen(Main main) {
        super(main, "Modificaciones", null, Main.Screens.MENU);
        Skin skin = main.getSkin();

        LayersManager layersManager = new LayersManager(stageUI, 1);

        layersManager.setZindex(0);
        optionTable = new OptionTable(skin, layersManager.getLayer(), main.fonts.briFont);
    }

    @Override
    public void show() {
        super.show();
        optionTable.update();
    }
}
