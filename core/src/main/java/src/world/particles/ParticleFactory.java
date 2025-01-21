package src.world.particles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import src.screens.game.GameScreen;

public class ParticleFactory {
    public enum Type{
        CLOUD,
    }

    public Actor create(Type type, Vector2 position, GameScreen game){
        return switch (type) {
            case CLOUD -> new DustParticle(game.main.getAssetManager(),new Rectangle(position.x,position.y, 1,1), game, 0.5f);
            default -> null;
        };
    }
}
