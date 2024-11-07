package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import src.main.Main;

public class ConnectingScreen extends UIScreen {
    private Float timeOut = 0f;
    private final Label connectLabel;

    public ConnectingScreen(Main main) {
        super(main);
        Skin skin = main.getSkin();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        connectLabel = new Label("Conectando...", skin);
        connectLabel.setFontScale(3f);

        table.add(connectLabel).width(200).height(50).center();
    }

    @Override
    public void show() {
        super.show();
        timeOut = 0f;
        connectLabel.setText("Conectando...");
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        timeOut += delta;
        if (timeOut > 5f) connectLabel.setText("Conexión fallida");
        if (timeOut > 7f) main.changeScreen(Main.Screens.MENU);
        if (main.client != null && main.client.isRunning()) {
            main.changeScreen(Main.Screens.LOBBY);
        }
    }
}
