package src.world.particles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import src.screens.game.GameScreen;
import src.utils.animation.SheetCutter;

public class DustParticle extends Particle{

    public DustParticle(AssetManager assetManager, Rectangle shape, GameScreen game, Float maxTime) {
        super(assetManager,shape, game, maxTime);

        Animation<TextureRegion> cloudAnimation = new Animation<>(0.08f,
            SheetCutter.cutHorizontal(assetManager.get("world/particles/dustParticle.png"), 5));
        setAnimation(cloudAnimation);
    }
}
