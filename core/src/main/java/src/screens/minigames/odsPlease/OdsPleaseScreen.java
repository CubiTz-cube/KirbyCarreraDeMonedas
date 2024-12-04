package src.screens.minigames.odsPlease;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
    private final Table backTable;
    private final Table frontTable;

    private final ArrayList<Texture> odsTextures = new ArrayList<>();
    private final Random random;

    private final SpriteActor odsImage;

    public OdsPleaseScreen(Main main, GameScreen game) {
        super(main, game);
        random = new Random();

        backTable = new Table();
        frontTable = new Table();
        backTable.setFillParent(true);
        frontTable.setFillParent(true);
        stageUI.addActor(backTable);
        stageUI.addActor(frontTable);

        timeMinigameLabel.setFontScale(2);
        timeMinigameLabel.setColor(Color.BLACK);

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

        odsImage = new SpriteActor(odsTextures.get(random.nextInt(odsTextures.size())));
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

        backTable.add(background2Image).expand(1,1).fill(1,1);
        backTable.row();
        backTable.add(backgroundImage).expand(1,2).fill(1,1);
        background2Image.toBack();

        frontTable.debugAll();
        frontTable.top().add(timeMinigameLabel).colspan(2);
        frontTable.row();
        frontTable.add(odsImage).width(512).height(310).colspan(2).padTop(250).padLeft(200);
        frontTable.row();
        frontTable.add(denyButton).width(256).height(128).colspan(1).padLeft(200);
        frontTable.add(passButton).width(256).height(128).colspan(1);
    }

    private void passButton() {
        changeOdsImage();
    }

    private void denyButton() {
        changeOdsImage();
    }

    private void changeOdsImage() {
        odsImage.setTexture(odsTextures.get(random.nextInt(odsTextures.size())));
    }

    @Override
    public void show() {
        super.show();
        setVisibleAll(false);
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
