package src.screens.minigames.fireFighter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import src.main.Main;
import src.screens.GameScreen;
import src.screens.components.LayersManager;
import src.screens.minigames.MinigameScreen;
import src.screens.minigames.fireFighter.components.Fire;
import src.screens.minigames.fireFighter.components.Plane;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static src.utils.constants.Constants.COIN_PER_MINIGAME;

public class FireFighterScreen extends MinigameScreen
{
    private final Integer max = 4;
    private final Integer totalFires = 10;

    private final LayersManager layersManager;

    private final CopyOnWriteArrayList<Fire> fires;
    private final Random random;
    private Plane plane;
    private Integer countGood;

    private Label countLabel;

    private Sound goodSound;

    private final Image background;

    public FireFighterScreen(Main main, GameScreen game) {
        super(main, game, "Apaga Incendios!","Muevete con D y A y vuela sobre los incendios para apagarlos.");

        layersManager = new LayersManager(stageUI, 2);
        fires = new CopyOnWriteArrayList<>();
        random = new Random();
        countGood = 0;

        background = new Image(main.getAssetManager().get("miniGames/fireFighter/forest.jpg", Texture.class));
        countLabel = new Label(countGood+"/"+totalFires , new Label.LabelStyle(main.fonts.briFont, null));

        initSounds();

        layersManager.setZindex(0);
        layersManager.getLayer().top().padTop(50);
        layersManager.getLayer().add().expandX();
        layersManager.getLayer().add(timeMinigameLabel);
        layersManager.getLayer().add().expandX();
        layersManager.getLayer().add(countLabel);
        layersManager.getLayer().add().expandX();

        layersManager.setZindex(1);
        layersManager.getLayer().add(background).grow();
    }

    private void initActors(){
        plane = new Plane(main.getAssetManager(), new Rectangle(20,20,64,64));
        stageUI.addActor(plane);

        for (int i = 0; i < max; i++) {
            addFire();
        }
    }

    private void addFire(){
        float x = random.nextFloat(100, Gdx.graphics.getHeight() - 100);
        float y = random.nextFloat(100, Gdx.graphics.getHeight() - 100);

        Fire newFire = new Fire(main.getAssetManager(), new Rectangle(x,y,48,48));

        stageUI.addActor(newFire);
        fires.add(newFire);
    }

    private void initSounds(){
        goodSound = main.getAssetManager().get("sound/kirby/kirbyScore1.wav", Sound.class);
    }

    private void checkCollisions() {
        Rectangle planeBounds = plane.getShape();

        for (Fire fire : fires) {
            Rectangle fireBounds = fire.getShape();

            if (planeBounds.overlaps(fireBounds)) {
                removeFireAddPoint(fire);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        if (plane == null) return;

        float scaleX = 1280f / width;
        float scaleY = 720f / height;

        plane.setScale(scaleX, scaleY);
        for (Fire fire : fires) fire.setScale(scaleX, scaleY);
    }

    private void removeFireAddPoint(Fire fire){
        fire.remove();
        fires.remove(fire);
        addFire();

        goodSound.play();

        countGood += 1;
        countLabel.setText((countGood+"/"+totalFires));
        if (countGood >= totalFires) endMinigame();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!isGameStarted()) return;
        if (!layersManager.isVisible()) {
            layersManager.setVisible(true);
            initActors();
        }
        checkCollisions();
    }

    @Override
    public void show() {
        super.show();
        layersManager.setVisible(false);
        countGood = 0;
        countLabel.setText((countGood+"/"+totalFires));
    }

    @Override
    public void endMinigame() {
        super.endMinigame();
        int totalPoints = (countGood / totalFires) * COIN_PER_MINIGAME;
        game.setScore(game.getScore() + totalPoints);

        plane.remove();
        for (Fire fire : fires) fire.remove();
    }
}
