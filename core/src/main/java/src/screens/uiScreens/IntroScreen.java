package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import src.main.Main;
import src.utils.sound.SingleSoundManager;
import src.utils.sound.SoundManager;

public class IntroScreen extends UIScreen {
    private Float time, alpha;
    private final Image logo;
    private Boolean soundPlayed;
    private final Main.Screens nextScreen;

    public IntroScreen(Main main, Texture logoTexture, Main.Screens nextScreen) {
        super(main);
        this.nextScreen = nextScreen;
        time = 0f;
        alpha = -0.1f;
        soundPlayed = false;

        Table table = new Table();
        table.setFillParent(true);
        stageUI.addActor(table);

        logo = new Image(logoTexture);
        table.add(logo).width(Gdx.graphics.getHeight()/1.5f).height(Gdx.graphics.getHeight()/1.5f);
        logo.setColor(1f, 1f, 1f, alpha);
    }

    @Override
    public void show() {
        super.show();
        time = 0f;
        alpha = -0.1f;
        soundPlayed = false;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        //SoundManager soundManager = SingleSoundManager.getInstance();
        time += delta;
        alpha += delta / 5;

        if (alpha > 1f) alpha = 1f;

        if (alpha >= 1f && !soundPlayed){
            //soundManager.playSound(main.getAssetManager().get("sound/introLogo.wav"), 1f);
            soundPlayed = true;
        }

        logo.setColor(1f, 1f, 1f, alpha);

        if (time > 6 || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) {
            main.changeScreen(nextScreen);
            //if (!soundPlayed) soundManager.playSound(main.getAssetManager().get("sound/introLogo.wav"), 1f);
        }
    }
}
