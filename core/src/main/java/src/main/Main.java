package src.main;

import com.badlogic.gdx.Game;
import src.pages.*;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
    }

    @Override
    public void dispose() {
    }
}
