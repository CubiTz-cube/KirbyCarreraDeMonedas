package src.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import static src.utils.variables.Constants.PIXELS_IN_METER;

public abstract class SpriteActorBox2d extends ActorBox2d{
    protected Sprite sprite;
    private Float animateTime;
    private Animation<TextureRegion> currentAnimation;
    private Boolean flipX;
    private final Vector2 spritePosModification;

    public SpriteActorBox2d(World world, Rectangle shape, AssetManager assetManager) {
        super(world, shape);
        animateTime = 0f;
        flipX = false;
        spritePosModification = new Vector2(0, 0);
        sprite = new Sprite(assetManager.get("logo.png", Texture.class));
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);
    }

    protected void setCurrentAnimation(Animation<TextureRegion> currentAnimation) {
        this.currentAnimation = currentAnimation;
        animateTime = 0f;
    }

    public Float getAnimateTime() {
        return animateTime;
    }

    public void setFlipX(Boolean flipX) {
        sprite.setFlip(flipX, false);
        this.flipX = flipX;
    }

    public Boolean isAnimationFinish() {
        return currentAnimation.isAnimationFinished(animateTime);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSpritePosModification(Float x, Float y) {
        spritePosModification.set(x,y);
    }

    public Boolean isFlipX() {
        return flipX;
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
