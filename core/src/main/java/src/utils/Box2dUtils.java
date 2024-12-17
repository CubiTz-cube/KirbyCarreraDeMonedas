package src.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Box2dUtils {
    public static void knockbackBody(Body receiver, Body attacker, float knockback) {
        Vector2 pushDirection = attacker.getPosition().cpy().sub(receiver.getPosition()).nor();

        receiver.setLinearVelocity(0,0);
        receiver.applyLinearImpulse(pushDirection.scl(-knockback), receiver.getWorldCenter(), true);
        receiver.applyLinearImpulse(0,knockback, receiver.getWorldCenter().x, receiver.getWorldCenter().y, true);
    }
}
