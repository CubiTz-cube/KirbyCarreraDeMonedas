package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import src.main.Main;
import src.net.PacketListener;
import src.net.packets.Packet;
import src.screens.components.LayersManager;
import src.utils.constants.MyColors;

public class ConnectingScreen extends UIScreen  implements PacketListener {
    private Float timeLabel;
    private Integer pointLabel;
    private final Label connectLabel;

    public ConnectingScreen(Main main) {
        super(main);
        timeLabel = 0f;
        pointLabel = 0;
        LayersManager layersManager = new LayersManager(stageUI, 3);

        Image whiteContainerImage = new Image(main.getAssetManager().get("ui/bg/whiteContainerBg.png", Texture.class));

        connectLabel = new Label("Conectando", new Label.LabelStyle(main.getInterFont(), MyColors.BLUE));
        connectLabel.setAlignment(Align.left);
        connectLabel.setFontScale(2.5f);

        layersManager.setZindex(0);
        layersManager.getLayer().setDebug(true);
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
        timeLabel = 0f;
        main.client.addListener(this);
        connectLabel.setText("Conectando");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(MyColors.BLUE.r, MyColors.BLUE.g, MyColors.BLUE.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        timeLabel += delta;
        if (timeLabel >= 0.5f ) {
            if (pointLabel < 3){
                connectLabel.setText(connectLabel.getText() + ".");
                pointLabel++;
            }else{
                connectLabel.setText("Conectando");
                pointLabel = 0;
            }
            timeLabel = 0f;
        }

        stageUI.act(delta);
        stageUI.draw();
    }

    @Override
    public void receivedPacket(Packet.Types type) {
        if (main.client.gameStart) return;
        if (type.equals(Packet.Types.ACTENTITYCOLOR)) {
            Gdx.app.postRunnable(() -> main.changeScreen(Main.Screens.LOBBY));
        }
    }

    @Override
    public void closeClient() {
        if (main.client.gameStart) return;
        main.closeClient();
        Gdx.app.postRunnable(() -> main.changeScreen(Main.Screens.MULTIPLAYER));
    }
}
