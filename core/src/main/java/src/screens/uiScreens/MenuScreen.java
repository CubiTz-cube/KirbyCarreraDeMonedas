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
import src.main.Main;
import src.screens.components.LayersManager;
import src.utils.FontCreator;
import src.utils.constants.MyColors;
import src.utils.sound.SingleSoundManager;

public class MenuScreen extends UIScreen {
    private final BitmapFont fontBri;

    public MenuScreen(Main main) {
        super(main);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Bricolage_Grotesque/BricolageGrotesque_48pt-Regular.ttf"));
        fontBri = FontCreator.createFont(48, Color.WHITE, generator, new FreeTypeFontGenerator.FreeTypeFontParameter());

        Image bgImage = new Image(main.getAssetManager().get("ui/bg/menuBg.png", Texture.class));

        Texture kirbyTexture = main.getAssetManager().get("ui/bg/kirbyBg.png", Texture.class);
        kirbyTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image kirbyImage = new Image(kirbyTexture);

        Texture lineTexture = main.getAssetManager().get("ui/bg/lineBg.png", Texture.class);
        lineTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image lineImage = new Image(lineTexture);

        Texture aroTexture = main.getAssetManager().get("ui/bg/aroBg.png", Texture.class);
        aroTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image aroImage = new Image(aroTexture);

        Texture logoTexture = main.getAssetManager().get("logo.png", Texture.class);
        logoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image logoImage = new Image(logoTexture);

        TextureRegionDrawable drawableUp = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/info.png", Texture.class));
        TextureRegionDrawable drawableHover = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/infoHover.png", Texture.class));
        drawableHover.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        drawableUp.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = drawableUp;
        style.imageOver = drawableHover;

        ImageButton infoButton = new ImageButton(style);
        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.INFO);
            }
        });

        drawableUp = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/exit.png", Texture.class));
        drawableHover = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/exitHover.png", Texture.class));
        drawableHover.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        drawableUp.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        style = new ImageButton.ImageButtonStyle();
        style.imageUp = drawableUp;
        style.imageOver = drawableHover;

        ImageButton exitButton = new ImageButton(style);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        drawableUp = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/button.png", Texture.class));
        drawableHover = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/buttonHover.png", Texture.class));
        ImageTextButton.ImageTextButtonStyle imageTextButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        imageTextButtonStyle.up = drawableUp;
        imageTextButtonStyle.font = fontBri;
        imageTextButtonStyle.over = drawableHover;
        imageTextButtonStyle.overFontColor = MyColors.BLUE;

        ImageTextButton playButton = new ImageTextButton("Jugar", imageTextButtonStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.GAME);
            }
        });

        ImageTextButton multiplayerButton = new ImageTextButton("Multijugador", imageTextButtonStyle);
        multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.MULTIPLAYER);
            }
        });

        ImageTextButton optionButton = new ImageTextButton("Ajustes", imageTextButtonStyle);
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.OPTION);
            }
        });

        LayersManager layersManager = new LayersManager(stageUI, 7);

        layersManager.setZindex(0);
        layersManager.getLayer().top();
        layersManager.getLayer().add().expandX();
        layersManager.getLayer().add(infoButton).size(64);
        layersManager.getLayer().add(exitButton).size(64).pad(25);

        layersManager.setZindex(1);
        layersManager.getLayer().top();
        layersManager.getLayer().add(logoImage).size(128).pad(20);
        layersManager.getLayer().add().expandX();

        layersManager.setZindex(2);
        layersManager.getLayer().top();
        layersManager.getLayer().add(aroImage);
        layersManager.getLayer().add().expandX();

        layersManager.setZindex(3);
        layersManager.getLayer().top();
        layersManager.getLayer().add(lineImage).expandX().fillX().padTop(65);

        layersManager.setZindex(4);
        layersManager.getLayer().left();
        layersManager.getLayer().setDebug(true);
        layersManager.getLayer().padLeft(250);
        layersManager.getLayer().padTop(130);
        layersManager.getLayer().add(playButton).expand(1,0).fill().left().pad(10);
        layersManager.getLayer().add().width(460);
        layersManager.getLayer().row();
        layersManager.getLayer().add(multiplayerButton).expand(1,0).fill().left().pad(10);
        layersManager.getLayer().row();
        layersManager.getLayer().add(optionButton).expandX().fillX().left().pad(10);

        layersManager.setZindex(5);
        layersManager.getLayer().right();
        layersManager.getLayer().add().expand(0,6);
        layersManager.getLayer().row();
        layersManager.getLayer().add(kirbyImage).expandY().fill(1,1).bottom().pad(25);

        layersManager.setZindex(6);
        layersManager.getLayer().add(bgImage).grow();
    }

    @Override
    public void dispose() {
        super.dispose();
        fontBri.dispose();
    }
}
