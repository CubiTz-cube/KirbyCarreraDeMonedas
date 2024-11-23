package src.world.particles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import src.screens.GameScreen;
import src.world.ActorBox2d;
import src.world.statics.Floor;
import src.world.statics.StaticFactory;

public class ParticleFactory {
    public enum Type{
        CLOUD
    }

    public Actor create(Type type, Vector2 position, GameScreen game){
        return switch (type) {
            case CLOUD -> new CloudParticle(game.main.getAssetManager(),new Rectangle(position.x,position.y, 1,1), game, 0.5f);
            default -> null;
        };
    }
}
