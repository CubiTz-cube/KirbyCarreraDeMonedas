package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import src.main.Main;
import src.screens.components.LayersManager;
import src.utils.FontCreator;
import src.utils.constants.MyColors;

public class BlueCircleScreen extends UIScreen{
    private Main.Screens backPage;

    public BlueCircleScreen(Main main, String title, Image bgImage, Main.Screens backPage) {
        super(main);
        this.backPage = backPage;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Bricolage_Grotesque/BricolageGrotesque_48pt-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderWidth = 4;
        parameter.borderColor = MyColors.BLUE;
        parameter.shadowColor = MyColors.BLUE;
        parameter.shadowOffsetX = -2;
        parameter.shadowOffsetY = 2;
        BitmapFont fontBri = FontCreator.createFont(48, MyColors.YELLOW, generator, parameter);

        LayersManager layersManager = new LayersManager(stageUI, 5);

        Label titleLabel = new Label(title, new Label.LabelStyle(fontBri, Color.WHITE));
        titleLabel.setAlignment(Align.center);

        Texture lineTexture = main.getAssetManager().get("ui/bg/lineBg.png", Texture.class);
        lineTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image lineImage = new Image(lineTexture);

        Texture pinkLineTexture = main.getAssetManager().get("ui/bg/pinkLineBg.png", Texture.class);
        pinkLineTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image pinkLineImage = new Image(pinkLineTexture);

        Texture aroTexture = main.getAssetManager().get("ui/bg/aroColorBg.png", Texture.class);
        aroTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image aroImage = new Image(aroTexture);

        TextureRegionDrawable drawableUp = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/exit.png", Texture.class));
        TextureRegionDrawable drawableHover = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/exitHover.png", Texture.class));
        drawableHover.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        drawableUp.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = drawableUp;
        style.imageOver = drawableHover;

        ImageButton exitButton = new ImageButton(style);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(backPage);
            }
        });

        layersManager.setZindex(0);
        layersManager.getLayer().top();
        layersManager.getLayer().add().expandX();
        layersManager.getLayer().add(exitButton).size(64).pad(25);

        layersManager.setZindex(1);
        layersManager.getLayer().top();
        layersManager.getLayer().add(titleLabel).padTop(65).width(398).center();
        layersManager.getLayer().add().expandX();

        layersManager.setZindex(2);
        layersManager.getLayer().top();
        layersManager.getLayer().add(pinkLineImage).padTop(100).expandX().left();

        layersManager.setZindex(3);
        layersManager.getLayer().top();
        layersManager.getLayer().add(aroImage);
        layersManager.getLayer().add().expandX();

        layersManager.setZindex(4);
        layersManager.getLayer().top();
        layersManager.getLayer().add(lineImage).expandX().fillX().padTop(65);

        if (bgImage == null) return;
        layersManager.setZindex(5);
        layersManager.getLayer().top();
        layersManager.getLayer().add(bgImage).grow();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            main.changeScreen(backPage);
        }
    }
}
