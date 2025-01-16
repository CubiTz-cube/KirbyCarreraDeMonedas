package src.screens.minigames.fireFighter;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import src.main.Main;
import src.screens.GameScreen;
import src.screens.components.LayersManager;
import src.screens.minigames.MinigameScreen;
import src.screens.minigames.fireFighter.components.Fire;
import src.screens.minigames.fireFighter.components.Plane;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class FireFighterScreen extends MinigameScreen
{
    private final Integer max = 9;

    private final LayersManager layersManager;

    private final ArrayList<Fire> fires;
    private Plane plane;

    private Sound goodSound;

    private final Image background;

    public FireFighterScreen(Main main, GameScreen game) {
        super(main, game, "Muevete con D y A y vuela sobre los incendios para apagarlos.");

        layersManager = new LayersManager(stageUI, 4);
        fires = new ArrayList<>();

        background = new Image(main.getAssetManager().get("minigames/FireFighter/forest.png", Texture.class));

        layersManager.setZindex(3);
        layersManager.getLayer().add(background).grow();
        
    }

    private void initSprites() {
        main.getAssetManager().get("minigames/FireFighter/fire.png", Texture.class);

        plane = new Plane(main.getAssetManager(), new Vector2(0, 0));
        for (int i = 0; i < max; i++) {
            fires.add(new Fire(main.getAssetManager(), new Vector2((float) Math.random() * 10, (float) Math.random() * 10)));
        }
    }

    private void initSounds(){
        goodSound = main.getAssetManager().get("sound/kirby/kirbyScore1.wav", Sound.class);
    }

    private void checkCollisions() {
        Rectangle planeBounds = plane.getBody();

        for (Fire fire : fires) {
            Rectangle fireBounds = fire.getBody();

            if (planeBounds.overlaps(fireBounds)) {
                fires.remove(fire);
                goodSound.play();
                break;
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!isGameStarted()) return;
        if (!layersManager.isVisible()) layersManager.setVisible(true);

        checkCollisions();
    }

    @Override
    public void show() {
        super.show();
        layersManager.setVisible(false);
        initSprites();
        initSounds();
    }
}
