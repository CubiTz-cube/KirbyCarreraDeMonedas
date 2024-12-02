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
    private final Table table;

    public OdsPleaseScreen(Main main, GameScreen game) {
        super(main, game);

        table = new Table();
        table.setFillParent(true);
        stageUI.addActor(table);

        timeMinigameLabel.setFontScale(2);
        timeMinigameLabel.setColor(Color.BLACK);

        Image odsImage = new Image(main.getAssetManager().get("odsPng/ods (10).png", Texture.class));

        TextButton passButton = new TextButton("Pass", main.getSkin());
        TextButton denyButton = new TextButton("Deny", main.getSkin());

        table.left().add(timeMinigameLabel);
        table.add(odsImage).width(512).height(310);
        table.row();
        table.add(passButton).width(256).height(128);
        table.add(denyButton).width(256).height(128);
    }

    @Override
    public void show() {
        super.show();
        table.setVisible(false);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!isGameStarted()) return;
        if (!table.isVisible()) table.setVisible(true);
    }
}
