package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import src.main.Main;
import src.screens.components.LayersManager;
import src.utils.FontCreator;
import src.utils.constants.MyColors;

public class MultiplayerScreen extends BlueCircleScreen {
    private final BitmapFont fontBri;

    public MultiplayerScreen(Main main) {
        super(main, "Multijugador", null, Main.Screens.MENU);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Bricolage_Grotesque/BricolageGrotesque_48pt-Regular.ttf"));
        fontBri = FontCreator.createFont(48, Color.WHITE, generator, new FreeTypeFontGenerator.FreeTypeFontParameter());
        Skin skin = main.getSkin();

        LayersManager layersManager = new LayersManager(stageUI, 7);

        TextureRegionDrawable drawableUp = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/button.png", Texture.class));
        TextureRegionDrawable drawableHover = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/buttonHover.png", Texture.class));
        ImageTextButton.ImageTextButtonStyle imageTextButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        imageTextButtonStyle.up = drawableUp;
        imageTextButtonStyle.font = fontBri;
        imageTextButtonStyle.over = drawableHover;
        imageTextButtonStyle.overFontColor = MyColors.BLUE;

        TextField nameTextField = new TextField("Sin nombre", skin);
        nameTextField.setAlignment(Align.center);

        ImageTextButton joinButton = new ImageTextButton("Unirse", imageTextButtonStyle);
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setName(nameTextField.getText());
                main.changeScreen(Main.Screens.JOIN);
            }
        });

        ImageTextButton createButton = new ImageTextButton("Crear", imageTextButtonStyle);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setName(nameTextField.getText());
                main.changeScreen(Main.Screens.SERVER);
            }
        });

        layersManager.setZindex(0);
        layersManager.getLayer().left();
        layersManager.getLayer().add(nameTextField).width(400).height(50).expandY();

        layersManager.setZindex(1);
        layersManager.getLayer().bottom();
        layersManager.getLayer().pad(20);
        layersManager.getLayer().add(joinButton).width(500).pad(10).expandX().right();
        layersManager.getLayer().row();
        layersManager.getLayer().add(createButton).width(500).pad(10).expandX().right();
    }

    @Override
    public void dispose() {
        super.dispose();
        fontBri.dispose();
    }
}
