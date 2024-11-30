package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import src.main.Main;

public class JoinScreen extends UIScreen {
    public JoinScreen(Main main) {
        super(main);
        Skin skin = main.getSkin();

        Table table = new Table();
        table.setFillParent(true);
        stageUI.addActor(table);

        TextField ipTextField = new TextField("localhost", skin);
        ipTextField.setAlignment(Align.center);

        TextField portTextField = new TextField("1234", skin);
        portTextField.setAlignment(Align.center);

        TextButton joinButton = new TextButton("Unirse", skin);
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setPort(Integer.parseInt(portTextField.getText()));
                main.setIp(ipTextField.getText());
                main.startClient(main.getIp(), main.getPort());
                main.changeScreen(Main.Screens.CONNECTING);
                System.out.println("Unirse");
            }
        });

        TextButton backButton = new TextButton("Volver", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.MULTIPLAYER);
            }
        });

        table.add(ipTextField).width(400).height(50).pad(10);
        table.row();
        table.add(portTextField).width(400).height(50).pad(10);
        table.row();
        table.add(joinButton).width(200).height(80).pad(10);
        table.row();
        table.add(backButton).width(200).height(50).pad(10);

    }
}
