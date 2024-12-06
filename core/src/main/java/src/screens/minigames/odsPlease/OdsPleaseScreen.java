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
import src.utils.SpriteActor;

import java.util.ArrayList;
import java.util.Random;

public class OdsPleaseScreen extends MinigameScreen {
    private final Integer max = 9;

    private final Table backTable;
    private final Table frontTable;

    private final Random random;

    private final ArrayList<Texture> odsTextures;
    private final ArrayList<Texture> wrongOdsTextures;

    private final SpriteActor odsImage;
    private final Label countLabel;

    private Boolean odsReal;
    private Integer countGood;
    private Integer countBad;

    public OdsPleaseScreen(Main main, GameScreen game) {
        super(main, game);
        backTable = new Table();
        frontTable = new Table();
        random = new Random();
        odsTextures = new ArrayList<>();
        wrongOdsTextures = new ArrayList<>();
        countGood = 0;
        countBad = 0;

        odsTextures.add(main.getAssetManager().get("odsPng/ods (1).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (2).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (3).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (4).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (5).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (6).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (7).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (8).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (9).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (10).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (11).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (12).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (13).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (14).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (15).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (16).png", Texture.class));
        odsTextures.add(main.getAssetManager().get("odsPng/ods (17).png", Texture.class));

        wrongOdsTextures.add(main.getAssetManager().get("poshi.jpg", Texture.class));

        backTable.setFillParent(true);
        frontTable.setFillParent(true);

        timeMinigameLabel.setFontScale(2);
        timeMinigameLabel.setColor(Color.BLACK);

        odsImage = new SpriteActor(main.getAssetManager().get("poshi.jpg", Texture.class));
        Image backgroundImage = new Image(main.getAssetManager().get("miniGames/odsPlease/Desk.png", Texture.class));
        Image background2Image = new Image(main.getAssetManager().get("miniGames/odsPlease/CheckpointBack.png", Texture.class));

        TextButton passButton = new TextButton("Pass", main.getSkin());
        passButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                passButton();
            }
        });

        TextButton denyButton = new TextButton("Deny", main.getSkin());
        denyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                denyButton();
            }
        });

        countLabel = new Label((countBad+countGood)+"/"+max+" Amonestaciones: " +countBad , main.getSkin());
        countLabel.setFontScale(2);

        stageUI.addActor(backTable);
        stageUI.addActor(frontTable);

        backTable.add(background2Image).expand(1,1).fill(1,1);
        backTable.row();
        backTable.add(backgroundImage).expand(1,2).fill(1,1);
        background2Image.toBack();

        frontTable.debugAll();
        frontTable.top().add(timeMinigameLabel).colspan(2);
        frontTable.add(countLabel).colspan(2);
        frontTable.row();
        frontTable.add(odsImage).width(512).height(310).colspan(2).padTop(250).padLeft(200);
        frontTable.row();
        frontTable.add(denyButton).width(256).height(128).colspan(1).padLeft(200);
        frontTable.add(passButton).width(256).height(128).colspan(1);

        changeOdsImage();
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
    }

    private void setVisibleAll(boolean visible) {
        backTable.setVisible(visible);
        frontTable.setVisible(visible);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!isGameStarted()) return;
        if (!frontTable.isVisible()) setVisibleAll(true);
    }
}
