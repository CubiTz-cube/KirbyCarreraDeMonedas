package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import src.main.Main;

public class MultiplayerScreen extends UIScreen {

    public MultiplayerScreen(Main main) {
        super(main);
        Skin skin = main.getSkin();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextField nameTextField = new TextField("Sin nombre", skin);
        nameTextField.setAlignment(Align.center);

        TextButton joinButton = new TextButton("Conectarse a una Partida", skin);
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setName(nameTextField.getText());
                main.changeScreen(Main.Screens.JOIN);
            }
        });

        TextButton createButton = new TextButton("Crear Partida", skin);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setName(nameTextField.getText());
                main.changeScreen(Main.Screens.SERVER);
            }
        });

        TextButton backButton = new TextButton("Volver", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.MENU);
            }
        });

        table.add(nameTextField).width(400).height(50).pad(10);
        table.row();
        table.add(joinButton).width(200).height(50).pad(10);
        table.row();
        table.add(createButton).width(200).height(50).pad(10);
        table.row();
        table.add(backButton).width(200).height(50).pad(10);
    }
}
