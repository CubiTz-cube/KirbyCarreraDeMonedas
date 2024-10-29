package src.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import static src.utils.Constants.PIXELS_IN_METER;

public abstract class SpriteActorBox2d extends ActorBox2d{
    protected Sprite sprite;
    protected Float animateTime;
    protected Animation<TextureRegion> currentAnimation;
    private Boolean loopingAnimation;

    public SpriteActorBox2d(World world) {
        super(world);
        loopingAnimation = true;
        animateTime = 0f;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setLoopingAnimation(Boolean loopingAnimation) {
        this.loopingAnimation = loopingAnimation;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (body == null) return;
        setPosition(
            body.getPosition().x * PIXELS_IN_METER - (getHeight()-PIXELS_IN_METER) / 2,
            body.getPosition().y * PIXELS_IN_METER - (getWidth()-PIXELS_IN_METER) / 2
        );
        if (currentAnimation != null) sprite.setRegion(currentAnimation.getKeyFrame(animateTime, loopingAnimation));
        sprite.setFlip(body.getLinearVelocity().x < 0, false);
        sprite.setPosition(getX(), getY());
        sprite.setOriginCenter();
        sprite.draw(batch);

        animateTime += Gdx.graphics.getDeltaTime();
    }
}
