package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
            "Al entrar en un espejo podras hacer un minijuego que te puede dar de 1 a 3 monedas" +
            ". Obten la mayor cantidad de monedas antes de que se acabe el tiempo\n",
            new Label.LabelStyle(main.fonts.interFont, MyColors.BLUE));
        infoLabel.setFontScale(0.7f);
        infoLabel.setWrap(true);

        Label infoTechLabel = new Label(
            "Librerias utilizadas: LibGDX\n" +
                "Lenguaje de programacion: Java\n" +
                "Version: v1.0.1\n" +
                "Desarrolladora: CubiTz\n" +
                "Desarrolladores: Daniel Carreño, Jose Pereira, Cesar Ostos\n",
            new Label.LabelStyle(main.fonts.interFont, MyColors.BLUE));
        infoTechLabel.setFontScale(0.7f);

        layersManager.setZindex(0);
        layersManager.getLayer().bottom();
        layersManager.getLayer().padLeft(40);
        layersManager.getLayer().add(infoLabel).expandX().fill().row();
        layersManager.getLayer().add(infoTechLabel).expandX().fill();
    }

}
