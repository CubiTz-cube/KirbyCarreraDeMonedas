package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import src.main.Main;

public class ServerScreen extends BlueCircleScreen {
    public ServerScreen(Main main) {
        super(main, "Crear Servidor", null, Main.Screens.MULTIPLAYER);
        Skin skin = main.getSkin();

        Table table = new Table();
        table.setFillParent(true);
        stageUI.addActor(table);

        //Label labelTitle = new Label("Crear servidor", skin);

        TextField ipTextField = new TextField("localhost", skin);
        ipTextField.setAlignment(Align.center);

        TextField portTextField = new TextField("1234", skin);
        portTextField.setAlignment(Align.center);

        TextButton joinButton = new TextButton("Crear", skin);
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setPort(Integer.parseInt(portTextField.getText()));
                main.setIp(ipTextField.getText());
                main.startServer(main.getIp(), main.getPort());
                main.startClient(main.getIp(), main.getPort());
                main.changeScreen(Main.Screens.LOBBY);
            }
        });

        TextButton backButton = new TextButton("Volver", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.MULTIPLAYER);
            }
        });

        //table.add(labelTitle).width(400).height(50).pad(10);
        //table.row();
        table.add(ipTextField).width(400).height(50).pad(10);
        table.row();
        table.add(portTextField).width(400).height(50).pad(10);
        table.row();
        table.add(joinButton).width(200).height(80).pad(10);
        table.row();
        table.add(backButton).width(200).height(50).pad(10);

    }
}
