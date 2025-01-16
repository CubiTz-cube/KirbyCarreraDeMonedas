package src.screens.minigames.fireFighter.components;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Fire extends Actor
{
    private final Texture texture;
    private final Vector2 position;

    public Fire(AssetManager assetManager, Vector2 position) {
        this.texture = assetManager.get("minigames/FireFighter/fire.png", Texture.class);
        this.position = position;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBody() {
        return new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
