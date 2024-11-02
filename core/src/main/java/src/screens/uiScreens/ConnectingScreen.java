package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import src.main.Main;

public class ConnectingScreen extends UIScreen {
    public ConnectingScreen(Main main) {
        super(main);
        Skin skin = main.getSkin();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label connectLabel = new Label("Conectando...", skin);

        table.add(connectLabel).width(200).height(50).center();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (main.client != null && main.client.isRunning()) {
            main.changeScreen(Main.Screens.LOBBYCLIENT);
        }
    }
}
