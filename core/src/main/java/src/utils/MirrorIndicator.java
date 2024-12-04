package src.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MirrorIndicator extends Actor{
    private final Sprite sprite;
    private final Vector2 mirrorPosition;
    private final Vector2 screenCenter;

    public MirrorIndicator(Texture texture, Rectangle shape) {
        this.sprite = new Sprite(texture);
        this.mirrorPosition = new Vector2(shape.x, shape.y);
        this.screenCenter = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);;
        setSize(shape.width, shape.height);
    }

    public void resize(){
        screenCenter.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Vector2 playerPosition = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        Vector2 direction = new Vector2(mirrorPosition).sub(playerPosition).nor();
        float angle = direction.angleDeg();
        setRotation(angle);

        float distance = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 2f - getWidth() / 2f;
        setPosition(screenCenter.x + direction.x * distance - getWidth() / 2f,
            screenCenter.y + direction.y * distance - getHeight() / 2f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setBounds(getX(), getY(), getWidth(), getHeight());
        sprite.setRotation(getRotation());
        sprite.draw(batch);
    }
}


