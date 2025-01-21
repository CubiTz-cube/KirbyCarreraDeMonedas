package src.screens.uiScreens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import src.main.Main;
import src.screens.components.LayersManager;
import src.screens.components.SpriteAsActor;
import src.utils.constants.MyColors;

public class MultiplayerScreen extends BlueCircleScreen {
    private final SpriteAsActor kirbyImage;

    public MultiplayerScreen(Main main) {
        super(main, "Multijugador", null, Main.Screens.MENU);

        LayersManager layersManager = new LayersManager(stageUI, 3);

        TextField nameTextField = new TextField("Sin nombre", myTextFieldStyle);
        nameTextField.setAlignment(Align.center);

        Label nameLabel = new Label("Nombre", new Label.LabelStyle(main.fonts.briFont, MyColors.BLUE));
        nameLabel.setAlignment(Align.center);

        ImageTextButton joinButton = new ImageTextButton("Unirse", myImageTextbuttonStyle);
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setPlayerName(nameTextField.getText());
                main.changeScreen(Main.Screens.JOIN);
            }
        });
        joinButton.addListener(hoverListener);

        ImageTextButton createButton = new ImageTextButton("Crear", myImageTextbuttonStyle);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setPlayerName(nameTextField.getText());
                main.changeScreen(Main.Screens.SERVER);
            }
        });
        createButton.addListener(hoverListener);

        kirbyImage = new SpriteAsActor(main.getAssetManager().get("ui/bg/kirbyIdleBg.png", Texture.class));

        layersManager.setZindex(0);
        layersManager.getLayer().left();
        layersManager.getLayer().pad(40);
        layersManager.getLayer().padBottom(40);
        layersManager.getLayer().add(nameLabel).expand(2,0).fill();
        layersManager.getLayer().add().expand(3,0);
        layersManager.getLayer().row();
        layersManager.getLayer().add(nameTextField).expand(2,0).fill();
        layersManager.getLayer().add().expand(3,0);

        layersManager.setZindex(1);
        layersManager.getLayer().bottom();
        layersManager.getLayer().pad(20);
        layersManager.getLayer().padBottom(40);
        layersManager.getLayer().add().expand(8,0);
        layersManager.getLayer().add(joinButton).pad(10).expandX().fill();
        layersManager.getLayer().row();
        layersManager.getLayer().add().expand(8,0);
        layersManager.getLayer().add(createButton).pad(10).expandX().fill();

        layersManager.setZindex(2);
        layersManager.getLayer().bottom();
        layersManager.getLayer().add(kirbyImage).size(184, 166).expandX();
        layersManager.getLayer().add().expandX();
    }

    @Override
    public void show() {
        super.show();
        kirbyImage.setColor(main.playerColor);
    }
}
