package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import src.main.Main;
import src.screens.components.LayersManager;
import src.utils.FontCreator;
import src.utils.constants.MyColors;

public class MultiplayerScreen extends BlueCircleScreen {
    private final BitmapFont fontBri;
    private final BitmapFont fontInter;

    public MultiplayerScreen(Main main) {
        super(main, "Multijugador", null, Main.Screens.MENU);
        Skin skin = main.getSkin();

        FreeTypeFontGenerator generatorBri = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Bricolage_Grotesque/BricolageGrotesque_48pt-Regular.ttf"));
        fontBri = FontCreator.createFont(48, Color.WHITE, generatorBri, new FreeTypeFontGenerator.FreeTypeFontParameter());

        FreeTypeFontGenerator generatorInter = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Inter/Inter_28pt-Regular.ttf"));
        fontInter = FontCreator.createFont(40, Color.WHITE, generatorInter, new FreeTypeFontGenerator.FreeTypeFontParameter());

        LayersManager layersManager = new LayersManager(stageUI, 7);

        Drawable drawableBg = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/input.png", Texture.class));

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = fontInter;
        textFieldStyle.fontColor = MyColors.BLUE;
        textFieldStyle.background = drawableBg;
        textFieldStyle.cursor = skin.getDrawable("textFieldCursor");
        textFieldStyle.selection = skin.getDrawable("selection");

        TextField nameTextField = new TextField("Sin nombre", textFieldStyle);
        nameTextField.setAlignment(Align.center);

        TextureRegionDrawable drawableUp = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/button.png", Texture.class));
        TextureRegionDrawable drawableHover = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/buttonHover.png", Texture.class));

        ImageTextButton.ImageTextButtonStyle imageTextButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        imageTextButtonStyle.up = drawableUp;
        imageTextButtonStyle.font = fontBri;
        imageTextButtonStyle.over = drawableHover;
        imageTextButtonStyle.overFontColor = MyColors.BLUE;

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
        layersManager.getLayer().add(nameTextField).width(500).expandY().padLeft(40);

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
        fontInter.dispose();
    }
}
