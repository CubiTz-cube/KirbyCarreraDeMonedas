package src.screens.minigames.odsPlease;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;
import src.screens.GameScreen;
import src.screens.components.LayersManager;
import src.screens.minigames.MinigameScreen;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import src.screens.components.SpriteAsActor;
import src.utils.animation.SheetCutter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class OdsPleaseScreen extends MinigameScreen {
    private final Integer max = 9;

    private final LayersManager layersManager;

    private final Random random;

    private final ArrayList<Texture> odsTextures;
    private final ArrayList<Texture> wrongOdsTextures;
    private final ArrayList<TextureRegion> personsTextures;

    private Label countLabel;

    private Boolean odsReal;
    private Integer countGood;
    private Integer countBad;

    private Image backgroundImage;
    private Image deskImage;
    private Image deskBackImage;

    private TextButton passButton;
    private TextButton denyButton;

    private SpriteAsActor odsImage;
    private SpriteAsActor personImage;

    public OdsPleaseScreen(Main main, GameScreen game) {
        super(main, game);
        layersManager = new LayersManager(stageUI, 5);
        random = new Random();
        odsTextures = new ArrayList<>();
        wrongOdsTextures = new ArrayList<>();
        personsTextures = new ArrayList<>();
        countGood = 0;
        countBad = 0;

        initSprites();
        initUIComponents();

        layersManager.setZindex(4);
        layersManager.getLayer().add().expand(1,1).fill(1,1);
        layersManager.getLayer().row();
        layersManager.getLayer().add(deskBackImage).grow();
        layersManager.getLayer().add().expand(3,1).fill(3,1);
        layersManager.getLayer().row();
        layersManager.getLayer().add().expand(1,1).fill(1,1);

        layersManager.setZindex(3);
        layersManager.getLayer().add().expand(1,3).fill(1,3);
        layersManager.getLayer().row();
        layersManager.getLayer().add(personImage).grow();
        layersManager.getLayer().add().expand(6,1).fill(6,1);
        layersManager.getLayer().row();
        layersManager.getLayer().add().expand(1,3).fill(1,3);

        layersManager.setZindex(2);
        layersManager.getLayer().add(backgroundImage).expand(1,1).fill(1,1);
        layersManager.getLayer().row();
        layersManager.getLayer().add(deskImage).expand(1,2).fill(1,1);
        backgroundImage.toBack();

        layersManager.setZindex(1);
        layersManager.getLayer().top().add(timeMinigameLabel).colspan(2);
        layersManager.getLayer().add(countLabel).colspan(2);

        layersManager.setZindex(0);
        layersManager.getLayer().add(odsImage).width(512).height(310).colspan(2).padTop(250).padLeft(200);
        layersManager.getLayer().row();
        layersManager.getLayer().add(denyButton).width(256).height(128).colspan(1).padLeft(200);
        layersManager.getLayer().add(passButton).width(256).height(128).colspan(1);
    }

    private void initSprites(){
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (1).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (2).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (3).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (4).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (5).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (6).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (7).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (8).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (9).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (10).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (11).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (12).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (13).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (14).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (15).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (16).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("miniGames/odsPlease/odsPng/ods (17).png", Texture.class));

        wrongOdsTextures.add(main.getAssetManager().get("miniGames/odsPlease/wrongOds/wrongOds (1).png", Texture.class));
        wrongOdsTextures.add(main.getAssetManager().get("miniGames/odsPlease/wrongOds/wrongOds (2).png", Texture.class));
        wrongOdsTextures.add(main.getAssetManager().get("miniGames/odsPlease/wrongOds/wrongOds (3).png", Texture.class));
        wrongOdsTextures.add(main.getAssetManager().get("miniGames/odsPlease/wrongOds/wrongOds (3B).png", Texture.class));
        wrongOdsTextures.add(main.getAssetManager().get("miniGames/odsPlease/wrongOds/wrongOds (4).png", Texture.class));
        //wrongOdsTextures.add(main.getAssetManager().get("miniGames/odsPlease/wrongOds/wrongOds (5).png", Texture.class));
        wrongOdsTextures.add(main.getAssetManager().get("miniGames/odsPlease/wrongOds/wrongOds (16).png", Texture.class));

        personsTextures.addAll(Arrays.asList(SheetCutter.cutSheet(main.getAssetManager().get("miniGames/odsPlease/persons/persons1.png", Texture.class), 2, 2)));
        personsTextures.addAll(Arrays.asList(SheetCutter.cutSheet(main.getAssetManager().get("miniGames/odsPlease/persons/persons2.png", Texture.class), 2, 2)));
        personsTextures.addAll(Arrays.asList(SheetCutter.cutSheet(main.getAssetManager().get("miniGames/odsPlease/persons/persons3.png", Texture.class), 2, 2)));
        personsTextures.addAll(Arrays.asList(SheetCutter.cutSheet(main.getAssetManager().get("miniGames/odsPlease/persons/persons4.png", Texture.class), 2, 2)));
    }

    private void initUIComponents(){

        timeMinigameLabel.setFontScale(2);
        timeMinigameLabel.setColor(Color.BLACK);

        odsImage = new SpriteAsActor(main.getAssetManager().get("poshi.jpg", Texture.class));
        personImage = new SpriteAsActor(main.getAssetManager().get("poshi.jpg", Texture.class));
        personImage.setSize(256,256);

        deskImage = new Image(main.getAssetManager().get("miniGames/odsPlease/Desk.png", Texture.class));
        backgroundImage = new Image(main.getAssetManager().get("miniGames/odsPlease/CheckpointBack.png", Texture.class));
        deskBackImage = new Image(main.getAssetManager().get("miniGames/odsPlease/BoothWall.png", Texture.class));

        passButton = new TextButton("Pass (D)", main.getSkin());
        passButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                passButton();
            }
        });

        denyButton = new TextButton("Deny (A)", main.getSkin());
        denyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                denyButton();
            }
        });

        countLabel = new Label((countBad+countGood)+"/"+max+" Amonestaciones: " +countBad , main.getSkin());
        countLabel.setFontScale(2);
    }

    private void passButton() {
        if (odsReal) countGood++;
        else {countBad++; shakeCamera(0.3f,4);}
        changeOdsImage();
        changePersonImage();
    }

    private void denyButton() {
        if (!odsReal) countGood++;
        else {countBad++; shakeCamera(0.3f,4);}
        changeOdsImage();
        changePersonImage();
    }

    private void changeOdsImage() {
        countLabel.setText((countBad+countGood)+"/"+max+" Amonestaciones: " +countBad);

        if (countGood + countBad == max) {
            game.setScore(game.getScore() + countGood/(max/3));
            endMinigame();
            return;
        }

        int select = random.nextInt(3);
        if (select == 2) odsImage.setTexture(wrongOdsTextures.get(random.nextInt(wrongOdsTextures.size())));
        else odsImage.setTexture(odsTextures.get(random.nextInt(odsTextures.size())));

        odsReal = select != 2;
        System.out.println("odsReal: " + odsReal);
    }

    private void changePersonImage() {
        personImage.setTextureRegion(personsTextures.get(random.nextInt(personsTextures.size())));
    }

    @Override
    public void show() {
        super.show();
        layersManager.setVisible(false);
        countGood = 0;
        countBad = 0;
        changeOdsImage();
        changePersonImage();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!isGameStarted()) return;
        if (!layersManager.isVisible()) layersManager.setVisible(true);

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            passButton();
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            denyButton();
        }
    }
}
