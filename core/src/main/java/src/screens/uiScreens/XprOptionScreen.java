package src.screens.uiScreens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import src.main.Main;
import src.net.packets.Packet;
import src.screens.components.ColorPickerImage;
import src.screens.components.LayersManager;
import src.screens.components.OptionTable;
import src.utils.constants.MyColors;

public class XprOptionScreen extends BlueCircleScreen {
    private final Label mainKirbyName;
    private final Image mainKirbyImage;
    private final ColorPickerImage colorWheel;

    public XprOptionScreen(Main main) {
        super(main, "Modificaciones", null, Main.Screens.MENU);

        Label colorLabel = new Label("Color", new Label.LabelStyle(main.fonts.briFont, Color.BLACK));

        mainKirbyName = new Label("Sin nombre", new Label.LabelStyle(main.fonts.interNameFont, MyColors.PINK));
        mainKirbyName.setAlignment(Align.center);
        mainKirbyImage = new Image(main.getAssetManager().get("ui/bg/kirbyIdleBg.png", Texture.class));
        mainKirbyImage.setScaling(Scaling.fit);
        mainKirbyImage.setAlign(Align.bottomLeft);

        colorWheel = new ColorPickerImage(main.getAssetManager().get("ui/colorWheel.png", Texture.class));
        colorWheel.setScaling(Scaling.fit);
        colorWheel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (main.playerColor.equals(colorWheel.getSelectColor())) return;
                mainKirbyImage.setColor(colorWheel.getSelectColor());
                main.playerColor.set(colorWheel.getSelectColor());
            }
        });
        colorWheel.addListener(hoverListener);

        LayersManager layersManager = new LayersManager(stageUI, 1);

        layersManager.setZindex(0);
        layersManager.getLayer().setDebug(true);
        layersManager.getLayer().padTop(50);
        layersManager.getLayer().add(mainKirbyName).expandX().pad(10);
        layersManager.getLayer().row();
        layersManager.getLayer().add(mainKirbyImage).expandX().pad(30).padBottom(20);
        layersManager.getLayer().row();
        layersManager.getLayer().add(colorLabel).expandX().pad(10);
        layersManager.getLayer().row();
        layersManager.getLayer().add(colorWheel).expand().pad(30).padBottom(20);
        layersManager.getLayer().row();

    }

    @Override
    public void show() {
        super.show();

    }
}
