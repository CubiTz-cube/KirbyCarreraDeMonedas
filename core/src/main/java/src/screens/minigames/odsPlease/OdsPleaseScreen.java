package src.screens.minigames.odsPlease;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;
import src.screens.GameScreen;
import src.screens.minigames.MinigameScreen;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import src.screens.components.SpriteAsActor;

import java.util.ArrayList;
import java.util.Random;

public class OdsPleaseScreen extends MinigameScreen {
    private final Integer max = 9;

    private final Table layer1Table;
    private final Table layer2Table;
    private final Table layer3Table;

    private final Random random;

    private final ArrayList<Texture> odsTextures;
    private final ArrayList<Texture> wrongOdsTextures;

    private SpriteAsActor odsImage;
    private Label countLabel;

    private Boolean odsReal;
    private Integer countGood;
    private Integer countBad;

    private Image backgroundImage;
    private Image deskImage;
    private Image deskBackImage;

    private TextButton passButton;
    private TextButton denyButton;

    public OdsPleaseScreen(Main main, GameScreen game) {
        super(main, game);
        layer1Table = new Table();
        layer2Table = new Table();
        layer3Table = new Table();
        random = new Random();
        odsTextures = new ArrayList<>();
        wrongOdsTextures = new ArrayList<>();
        countGood = 0;
        countBad = 0;

        initOds();

        initUIComponents();

        stageUI.addActor(layer3Table);
        stageUI.addActor(layer2Table);
        stageUI.addActor(layer1Table);

        layer2Table.add(backgroundImage).expand(1,1).fill(1,1);
        layer2Table.row();
        layer2Table.add(deskImage).expand(1,2).fill(1,1);
        backgroundImage.toBack();

        layer3Table.add().expand(1,1).fill(1,1);
        layer3Table.row();
        layer3Table.add(deskBackImage).grow();
        layer3Table.add().expand(3,1).fill(3,1);
        layer3Table.row();
        layer3Table.add().expand(1,1).fill(1,1);

        layer1Table.top().add(timeMinigameLabel).colspan(2);
        layer1Table.add(countLabel).colspan(2);
        layer1Table.row();
        layer1Table.add(odsImage).width(512).height(310).colspan(2).padTop(250).padLeft(200);
        layer1Table.row();
        layer1Table.add(denyButton).width(256).height(128).colspan(1).padLeft(200);
        layer1Table.add(passButton).width(256).height(128).colspan(1);

        layer2Table.debugAll();
    }

    private void initOds(){
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

        wrongOdsTextures.add(main.getAssetManager().get("poshi.jpg", Texture.class));
    }

    private void initUIComponents(){
        layer1Table.setFillParent(true);
        layer2Table.setFillParent(true);
        layer3Table.setFillParent(true);

        timeMinigameLabel.setFontScale(2);
        timeMinigameLabel.setColor(Color.BLACK);

        odsImage = new SpriteAsActor(main.getAssetManager().get("poshi.jpg", Texture.class));

        deskImage = new Image(main.getAssetManager().get("miniGames/odsPlease/Desk.png", Texture.class));
        backgroundImage = new Image(main.getAssetManager().get("miniGames/odsPlease/CheckpointBack.png", Texture.class));
        deskBackImage = new Image(main.getAssetManager().get("miniGames/odsPlease/BoothWall.png", Texture.class));

        passButton = new TextButton("Pass", main.getSkin());
        passButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                passButton();
            }
        });

        denyButton = new TextButton("Deny", main.getSkin());
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
        if (!odsReal) countGood++;
        else countBad++;
        changeOdsImage();
    }

    private void denyButton() {
        if (odsReal) countGood++;
        else countBad++;
        changeOdsImage();
    }

    private void changeOdsImage() {
        countLabel.setText((countBad+countGood)+"/"+max+" Amonestaciones: " +countBad);

        if (countGood + countBad == max) {
            game.setScore(game.getScore() + countGood/(max/3));
            endMinigame();
            return;
        }

        boolean select = random.nextBoolean();
        if (select) odsImage.setTexture(wrongOdsTextures.get(random.nextInt(wrongOdsTextures.size())));
        else odsImage.setTexture(odsTextures.get(random.nextInt(odsTextures.size())));

        odsReal = select;
    }

    @Override
    public void show() {
        super.show();
        setVisibleAll(false);
        countGood = 0;
        countBad = 0;
        changeOdsImage();
    }

    private void setVisibleAll(boolean visible) {
        layer2Table.setVisible(visible);
        layer1Table.setVisible(visible);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!isGameStarted()) return;
        if (!layer1Table.isVisible()) setVisibleAll(true);
    }
}
