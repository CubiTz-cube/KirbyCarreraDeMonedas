package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import src.main.Main;
import src.screens.components.LayersManager;
import src.utils.constants.MyColors;

public class JoinScreen extends BlueCircleScreen {
    public JoinScreen(Main main) {
        super(main, "Unirse Servidor", null, Main.Screens.MULTIPLAYER);

        LayersManager layersManager = new LayersManager(stageUI, 3);

        TextField ipTextField = new TextField("localhost", myTextFieldStyle);
        ipTextField.setAlignment(Align.center);

        TextField portTextField = new TextField("1234", myTextFieldStyle);
        portTextField.setAlignment(Align.center);

        Label ipLabel = new Label("Ip", new Label.LabelStyle(main.fonts.briFont, MyColors.BLUE));
        ipLabel.setAlignment(Align.center);

        Label portLabel = new Label("Puerto", new Label.LabelStyle(main.fonts.briFont, MyColors.BLUE));
        portLabel.setAlignment(Align.center);

        ImageTextButton joinButton = new ImageTextButton("Unirse", myImageTextbuttonStyle);
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setPort(Integer.parseInt(portTextField.getText()));
                main.setIp(ipTextField.getText());
                main.startClient(main.getIp(), main.getPort());
                main.changeScreen(Main.Screens.CONNECTING);
            }
        });
        joinButton.addListener(hoverListener);

        layersManager.setZindex(0);
        layersManager.getLayer().center();
        layersManager.getLayer().pad(40);
        layersManager.getLayer().add(ipLabel).expandX().fill().padRight(10);
        layersManager.getLayer().add(portLabel).expandX().fill().padLeft(10);
        layersManager.getLayer().row();
        layersManager.getLayer().add(ipTextField).expandX().fill().padRight(10);
        layersManager.getLayer().add(portTextField).expandX().fill().padLeft(10);

        layersManager.setZindex(1);
        layersManager.getLayer().bottom();
        layersManager.getLayer().padBottom(80);
        layersManager.getLayer().add(joinButton).expandX().width(600);

    }
}
