package src.screens.minigames.fireFighter.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import src.utils.constants.PlayerControl;

public class Plane extends Actor {
    private final float speed = 12f;
    private final float rotationSpeed = 90f;
    private final Texture texture;
    private final Vector2 position;
    private float rotationDegrees;

    public Plane(AssetManager assetManager, Vector2 initialPosition) {
        this.texture = assetManager.get("minigames/FireFighter/plane.png", Texture.class);
        this.position = initialPosition;
        this.rotationDegrees = 0f;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getRotationDegrees() {
        return rotationDegrees;
    }

    public Rectangle getBody() {
        return new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isKeyPressed(PlayerControl.LEFT)) {
            rotationDegrees += rotationSpeed * delta;
        }
        if (Gdx.input.isKeyPressed(PlayerControl.RIGHT)) {
            rotationDegrees -= rotationSpeed * delta;
        }

        float radians = (float) Math.toRadians(rotationDegrees);
        position.x += (float) (speed * delta * Math.cos(radians));
        position.y += (float) (speed * delta * Math.sin(radians));
    }
}
