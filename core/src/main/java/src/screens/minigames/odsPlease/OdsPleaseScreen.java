package src.screens.minigames.odsPlease;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import src.main.Main;
import src.screens.GameScreen;
import src.screens.minigames.MinigameScreen;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class OdsPleaseScreen extends MinigameScreen {
    private final Table backTable;
    private final Table frontTable;

    public OdsPleaseScreen(Main main, GameScreen game) {
        super(main, game);

        backTable = new Table();
        frontTable = new Table();
        backTable.setFillParent(true);
        frontTable.setFillParent(true);
        stageUI.addActor(backTable);
        stageUI.addActor(frontTable);

        timeMinigameLabel.setFontScale(2);
        timeMinigameLabel.setColor(Color.BLACK);

        Image odsImage = new Image(main.getAssetManager().get("odsPng/ods (10).png", Texture.class));
        Image backgroundImage = new Image(main.getAssetManager().get("miniGames/odsPlease/Desk.png", Texture.class));
        Image background2Image = new Image(main.getAssetManager().get("miniGames/odsPlease/CheckpointBack.png", Texture.class));

        TextButton passButton = new TextButton("Pass", main.getSkin());
        TextButton denyButton = new TextButton("Deny", main.getSkin());

        backTable.add(background2Image).expand(1,1).fill(1,1);
        backTable.row();
        backTable.add(backgroundImage).expand(1,2).fill(1,1);
        background2Image.toBack();

        frontTable.debugAll();
        frontTable.top().add(timeMinigameLabel).colspan(2);
        frontTable.row();
        frontTable.add(odsImage).width(512).height(310).colspan(2).padTop(250).padLeft(200);
        frontTable.row();
        frontTable.add(passButton).width(256).height(128).colspan(1).padLeft(200);
        frontTable.add(denyButton).width(256).height(128).colspan(1);
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
    public void resize(int width, int height) {

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!isGameStarted()) return;
        if (!frontTable.isVisible()) setVisibleAll(true);
    }
}
