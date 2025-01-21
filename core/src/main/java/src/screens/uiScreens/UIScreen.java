package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import src.main.Main;
import src.screens.BaseScreen;
import src.utils.constants.MyColors;
import src.utils.sound.SingleSoundManager;

public abstract class UIScreen extends BaseScreen {
    protected final Stage stageUI;

    public final ImageTextButton.ImageTextButtonStyle myImageTextbuttonStyle;
    public final TextField.TextFieldStyle myTextFieldStyle;

    private final Sound clickSound;
    //private final Sound hoverSound;
    public final InputListener hoverListener;

    public UIScreen(Main main) {
        super(main);
        Skin skin = main.getSkin();
        stageUI = new Stage(new ScreenViewport());

        TextureRegionDrawable drawableUp = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/button.png", Texture.class));
        TextureRegionDrawable drawableHover = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/buttonHover.png", Texture.class));

        myImageTextbuttonStyle = new ImageTextButton.ImageTextButtonStyle();
        myImageTextbuttonStyle.up = drawableUp;
        myImageTextbuttonStyle.font = main.fonts.briFont;
        myImageTextbuttonStyle.over = drawableHover;
        myImageTextbuttonStyle.overFontColor = MyColors.BLUE;

        Drawable drawableBg = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/input.png", Texture.class));

        myTextFieldStyle = new TextField.TextFieldStyle();
        myTextFieldStyle.font = main.fonts.interFont;
        myTextFieldStyle.fontColor = MyColors.BLUE;
        myTextFieldStyle.background = drawableBg;
        myTextFieldStyle.cursor = skin.getDrawable("textFieldCursor");
        myTextFieldStyle.selection = skin.getDrawable("selection");

        clickSound = main.getAssetManager().get("sound/ui/click.wav", Sound.class);

        hoverListener = new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer != -1) SingleSoundManager.getInstance().playSound(clickSound, 1f, 0.7f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {

            }
        };
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
    }
}
