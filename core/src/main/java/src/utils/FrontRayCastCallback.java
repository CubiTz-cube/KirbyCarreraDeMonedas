package src.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

public class FrontRayCastCallback implements RayCastCallback {
    private Fixture hitFixture;

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        hitFixture = fixture;
        return fraction;
    }

    public Fixture getHitFixture() {
        return hitFixture;
    }
}
