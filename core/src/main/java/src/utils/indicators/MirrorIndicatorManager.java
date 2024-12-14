package src.utils.indicators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;

public class MirrorIndicatorManager extends Actor {
    private final Texture texture;
    private final HashMap<Integer, BorderIndicator> borderIndicators;

    public MirrorIndicatorManager(Texture texture) {
        this.texture = texture;
        borderIndicators = new HashMap<>();
    }

    public void setCenterPositions(Vector2 centerPosition) {
        for (BorderIndicator borderIndicator : borderIndicators.values()) {
            borderIndicator.setCenterPosition(centerPosition);
        }
    }

    public void add(Integer id,Vector2 targetPosition) {
        BorderIndicator borderIndicator = new BorderIndicator(texture, targetPosition);
        borderIndicators.put(id,borderIndicator);
    }

    public void changeTargetPosition(Integer id,Vector2 targetPosition) {
        if (!borderIndicators.containsKey(id)) {
            Gdx.app.log("MirrorIndicatorManager", "id " + id + " no encontrada para cambiar su target position");
            return;
        }
        borderIndicators.get(id).setTargetPosition(targetPosition);
    }

    public void remove(Integer id) {
        borderIndicators.remove(id);
    }

    @Override
    public void act(float delta) {
        for (BorderIndicator borderIndicator : borderIndicators.values()) {
            borderIndicator.act(delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (BorderIndicator borderIndicator : borderIndicators.values()) {
            borderIndicator.draw(batch, parentAlpha);
        }
    }
}
