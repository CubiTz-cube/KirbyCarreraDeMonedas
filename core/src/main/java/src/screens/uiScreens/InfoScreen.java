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
            "Controles:\n" +
            "W/A/S/D Moverse\n" +
            "P Ejecutar accion\n" +
            "Shift Correr\n" +
            "Control-Izquierdo Soltar poder\n\n" +
            "Objetivo:\n" +
            "Al entrar en un espejo podras hacer \nun minijuego que te puede dar de 1 a 3 monedas\n" +
            "Obten la mayor cantidad de monedas \nantes de que se acabe el tiempo\n",
            new Label.LabelStyle(main.getInterFont(), MyColors.BLUE));

        layersManager.setZindex(0);
        layersManager.getLayer().bottom();
        layersManager.getLayer().padLeft(40);
        layersManager.getLayer().add(infoLabel).expandX().fill();
    }

}
