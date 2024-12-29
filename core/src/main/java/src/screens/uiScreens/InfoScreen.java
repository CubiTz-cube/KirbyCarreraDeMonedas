package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import src.main.Main;
import src.screens.components.LayersManager;
import src.utils.constants.MyColors;

public class InfoScreen extends BlueCircleScreen {

    public InfoScreen(Main main) {
        super(main, "Acerca de", null, Main.Screens.MENU);

        LayersManager layersManager = new LayersManager(stageUI, 3);

        Label infoLabel = new Label(
            "Controles\n" +
            "W/A/S/D Moverse\n" +
            "Espacio Ejecutar accion\n" +
            "Shift Correr\n",
            new Label.LabelStyle(fontBri, MyColors.BLUE));

        layersManager.setZindex(0);
        layersManager.getLayer().center();
        layersManager.getLayer().pad(40);
        layersManager.getLayer().add(infoLabel).expandX().fill().padRight(10);

    }

}
