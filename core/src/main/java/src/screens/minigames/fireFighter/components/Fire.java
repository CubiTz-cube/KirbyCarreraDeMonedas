package src.screens.minigames.fireFighter.components;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import src.utils.animation.SheetCutter;

public class Fire extends Actor {
    private Float animateTime;
    private final Animation<TextureRegion> animation;

    private final Sprite sprite;
    private final Rectangle shape;

    private Float liveTime;

    public Fire(AssetManager assetManager, Rectangle shape) {
        animateTime = 0f;

        animation = new Animation<>(0.1f,SheetCutter.cutHorizontal(
            assetManager.get("minigames/FireFighter/fire.png", Texture.class), 7));
        animation.setPlayMode(Animation.PlayMode.LOOP);

        this.shape = shape;
        sprite = new Sprite(animation.getKeyFrame(0));
        liveTime = 5f;

        setBounds(shape.x, shape.y, shape.width, shape.height);
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setRegion(animation.getKeyFrame(animateTime));
        sprite.setBounds(getX(), getY(), getWidth(), getHeight());
        shape.set(getX(), getY(), getWidth(), getHeight());
        sprite.draw(batch);
    }

    @Override
    public void act(float delta) {
        animateTime += delta;

        /*if (liveTime > 0) liveTime -= delta;
        else remove();*/
    }

    public Rectangle getShape() {
        return shape;
    }
}
