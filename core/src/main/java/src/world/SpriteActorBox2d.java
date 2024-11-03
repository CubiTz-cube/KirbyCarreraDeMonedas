package src.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import static src.utils.Constants.PIXELS_IN_METER;

public abstract class SpriteActorBox2d extends ActorBox2d{
    protected Sprite sprite;
    protected Float animateTime;
    protected Animation<TextureRegion> currentAnimation;
    private Boolean flipX;
    private Vector2 spritePosModification;

    public SpriteActorBox2d(World world) {
        super(world);
        animateTime = 0f;
        flipX = false;
        spritePosModification = new Vector2(0, 0);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setCurrentAnimation(Animation<TextureRegion> currentAnimation) {
        this.currentAnimation = currentAnimation;
        animateTime = 0f;
    }

    public void setFlipX(Boolean flipX) {
        sprite.setFlip(flipX, false);
        this.flipX = flipX;
    }

    public void setSpritePosModification(Float x, Float y) {
        spritePosModification.set(x,y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (body == null) return;
        setPosition(
            body.getPosition().x * PIXELS_IN_METER - (getHeight()-PIXELS_IN_METER) / 2 + spritePosModification.x,
            body.getPosition().y * PIXELS_IN_METER - (getWidth()-PIXELS_IN_METER) / 2 + spritePosModification.y
        );
        if (currentAnimation != null) sprite.setRegion(currentAnimation.getKeyFrame(animateTime, false));
        sprite.setFlip(flipX, false);
        sprite.setPosition(getX(), getY());
        sprite.setOriginCenter();
        sprite.draw(batch);

        animateTime += Gdx.graphics.getDeltaTime();
    }
}
