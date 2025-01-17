package src.screens.minigames.fireFighter.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import src.utils.constants.PlayerControl;

public class Plane extends Actor {
    private final float speed = 230f;
    private final float rotationSpeed = 120f;

    private final Sprite sprite;
    private final Rectangle shape;

    public Plane(AssetManager assetManager, Rectangle shape) {
        this.sprite = new Sprite(assetManager.get("miniGames/fireFighter/plane.png", Texture.class));
        this.shape = shape;
        setBounds(shape.x, shape.y, shape.width, shape.height);
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    public Rectangle getShape() {
        return shape;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setOriginCenter();
        sprite.setRotation(getRotation());
        sprite.setBounds(getX(), getY(), getWidth(), getHeight());
        shape.set(getX(), getY(), getWidth(), getHeight());
        sprite.draw(batch);
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isKeyPressed(PlayerControl.LEFT)) {
            setRotation(getRotation() + rotationSpeed * delta);
        }
        if (Gdx.input.isKeyPressed(PlayerControl.RIGHT)) {
            setRotation(getRotation() - rotationSpeed * delta);
        }

        float radians = (float) Math.toRadians(getRotation());
        float x = (float) (speed * delta * Math.cos(radians));
        float y = (float) (speed * delta * Math.sin(radians));

        float newPosCenterX = getX() + x + getWidth()/2;
        float newPosCenterY = getY() + y + getHeight()/2;

        if (newPosCenterX < 0 || newPosCenterX > 1280
            || newPosCenterY < 0 || newPosCenterY > 720) {
            return;
        }

        moveBy(x,y);
    }
}
