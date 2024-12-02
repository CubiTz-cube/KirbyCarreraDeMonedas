package src.screens.minigames.odsPlease;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import src.main.Main;
import src.screens.GameScreen;
import src.screens.minigames.MinigameScreen;

public class OdsPleaseScreen extends MinigameScreen {
    private ShapeRenderer shapeRenderer;

    public OdsPleaseScreen(Main main, GameScreen game) {
        super(main, game);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(stageUI.getCamera().combined);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(50, 50, 200, 100);
        shapeRenderer.end();
    }
}
