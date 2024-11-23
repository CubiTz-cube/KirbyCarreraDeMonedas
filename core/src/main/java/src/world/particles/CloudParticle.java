package src.world.particles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import src.screens.GameScreen;
import src.utils.animation.SheetCutter;

public class CloudParticle extends Particle{

    public CloudParticle(AssetManager assetManager, Rectangle shape, GameScreen game, Float maxTime) {
        super(assetManager,shape, game, maxTime);

        Animation<TextureRegion> cloudAnimation = new Animation<>(0.08f,
            SheetCutter.cutHorizontal(assetManager.get("world/particles/cloudParticle.png"), 5));
        setAnimation(cloudAnimation);
    }
}
