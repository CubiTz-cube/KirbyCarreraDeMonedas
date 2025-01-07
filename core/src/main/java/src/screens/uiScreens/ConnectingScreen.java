package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import src.main.Main;
import src.screens.components.LayersManager;
import src.utils.constants.MyColors;

public class ConnectingScreen extends UIScreen {
    private Float timeOut = 0f;
    private final Label connectLabel;

    public ConnectingScreen(Main main) {
        super(main);
        LayersManager layersManager = new LayersManager(stageUI, 3);

        Image whiteContainerImage = new Image(main.getAssetManager().get("ui/bg/whiteContainerBg.png", Texture.class));

        connectLabel = new Label("Conectando...", new Label.LabelStyle(main.getInterFont(), MyColors.BLUE));
        connectLabel.setAlignment(Align.right);
        connectLabel.setFontScale(2.5f);

        layersManager.setZindex(0);
        layersManager.getLayer().bottom();
        layersManager.getLayer().padBottom(70);
        layersManager.getLayer().padRight(60);
        layersManager.getLayer().add(connectLabel).expandX().right();

        layersManager.setZindex(1);
        layersManager.getLayer().bottom();
        layersManager.getLayer().padBottom(60);
        layersManager.getLayer().add(whiteContainerImage).expandX().right();
    }

    @Override
    public void show() {
        super.show();
        timeOut = 0f;
        connectLabel.setText("Conectando...");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(MyColors.BLUE.r, MyColors.BLUE.g, MyColors.BLUE.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stageUI.act(delta);
        stageUI.draw();

        timeOut += delta;
        if (timeOut > 5f) connectLabel.setText("Conexión fallida");
        if (timeOut > 7f) main.changeScreen(Main.Screens.MENU);
        if (main.client != null && main.client.isRunning()) {
            main.changeScreen(Main.Screens.LOBBY);
        }
    }
}
