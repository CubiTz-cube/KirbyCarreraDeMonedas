package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import src.main.Main;
import src.screens.BaseScreen;
import src.utils.FontCreator;
import src.utils.constants.MyColors;

public abstract class UIScreen extends BaseScreen {
    protected final Stage stageUI;

    protected final BitmapFont fontBri;
    protected final BitmapFont fontInter;

    ImageTextButton.ImageTextButtonStyle myImageTextbuttonStyle;
    TextField.TextFieldStyle myTextFieldStyle;

    public UIScreen(Main main) {
        super(main);
        Skin skin = main.getSkin();
        stageUI = new Stage(new ScreenViewport());

        FreeTypeFontGenerator generatorBri = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Bricolage_Grotesque/BricolageGrotesque_48pt-Regular.ttf"));
        fontBri = FontCreator.createFont(48, Color.WHITE, generatorBri, new FreeTypeFontGenerator.FreeTypeFontParameter());

        FreeTypeFontGenerator generatorInter = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Inter/Inter_28pt-Regular.ttf"));
        fontInter = FontCreator.createFont(40, Color.WHITE, generatorInter, new FreeTypeFontGenerator.FreeTypeFontParameter());

        TextureRegionDrawable drawableUp = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/button.png", Texture.class));
        TextureRegionDrawable drawableHover = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/buttonHover.png", Texture.class));

        myImageTextbuttonStyle = new ImageTextButton.ImageTextButtonStyle();
        myImageTextbuttonStyle.up = drawableUp;
        myImageTextbuttonStyle.font = fontBri;
        myImageTextbuttonStyle.over = drawableHover;
        myImageTextbuttonStyle.overFontColor = MyColors.BLUE;

        Drawable drawableBg = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/input.png", Texture.class));

        myTextFieldStyle = new TextField.TextFieldStyle();
        myTextFieldStyle.font = fontInter;
        myTextFieldStyle.fontColor = MyColors.BLUE;
        myTextFieldStyle.background = drawableBg;
        myTextFieldStyle.cursor = skin.getDrawable("textFieldCursor");
        myTextFieldStyle.selection = skin.getDrawable("selection");
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(stageUI);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stageUI.act(delta);
        stageUI.draw();
    }

    @Override
    public void resize(int width, int height) {
        stageUI.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stageUI.dispose();
        fontBri.dispose();
        fontInter.dispose();
    }
}
