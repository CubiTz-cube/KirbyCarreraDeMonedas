package src.utils.indicators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static src.utils.constants.Constants.PIXELS_IN_METER;

public class BorderIndicator extends Actor{
    private final Sprite sprite;
    private final Vector2 targetPosition;
    private final Vector2 centerPosition;

    public BorderIndicator(Texture texture, Vector2 targetPosition){
        sprite = new Sprite(texture);
        sprite.setSize(PIXELS_IN_METER, PIXELS_IN_METER);
        this.targetPosition = targetPosition.scl(PIXELS_IN_METER);
        centerPosition = new Vector2(0, 0);
    }

    /**
     * Cambia la posición del target
     * @param targetPosition vector de posición en metros
     */
    public void setTargetPosition(Vector2 targetPosition) {
        this.targetPosition.set(targetPosition);
    }

    /**
     * Cambia la posición del centro desde donde se indica la dirección
     * @param centerPosition vector de posición en metros
     */
    public void setCenterPosition(Vector2 centerPosition) {
        this.centerPosition.set(centerPosition.scl(PIXELS_IN_METER));
    }

    @Override
    public void act(float delta) {
        Vector2 direction = new Vector2(targetPosition.scl(PIXELS_IN_METER)).sub(centerPosition).nor();
        float angle = direction.angleDeg();
        setRotation(angle);

        float distance = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 2f - getWidth() / 2f;
        setPosition(centerPosition.x + direction.x * distance - getWidth() / 2f,
            centerPosition.y + direction.y * distance - getHeight() / 2f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.setOriginCenter();
        sprite.setRotation(getRotation());
        sprite.draw(batch);
    }
}


