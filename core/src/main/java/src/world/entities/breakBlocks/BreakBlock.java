package src.world.entities.breakBlocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.utils.animation.SheetCutter;
import src.utils.stateMachine.StateMachine;
import src.world.entities.Entity;
import src.world.entities.breakBlocks.states.*;
import src.world.player.Player;

import static src.utils.variables.Constants.PIXELS_IN_METER;

public class BreakBlock extends Entity {
    public enum StateType {LIVE,BREAK}
    private StateType currentStateType;
    private final StateMachine stateMachine;
    private final LiveState liveState;
    private final BreakState breakState;

    public enum AnimationType{LIVE,BREAK}
    private AnimationType currentAnimationType;
    private Boolean changeAnimation;

    public BreakBlock(World world, Rectangle shape, AssetManager assetManager, Integer id) {
        super(world, shape, assetManager,id);
        type = Type.BREAKBLOCK;
        sprite.setTexture((assetManager.get("world/entities/breakBlock.png")));
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/2, shape.height/2);
        fixture = body.createFixture(box, 1.5f);
        fixture.setUserData("breakBlock");
        box.dispose();

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);

        stateMachine = new StateMachine();
        liveState = new LiveState(this);
        breakState = new BreakState(this);
        stateMachine.setState(liveState);
    }
    public void setState(StateType stateType){
        currentStateType = stateType;
        switch (stateType){
            case LIVE -> stateMachine.setState(liveState);
            case BREAK -> stateMachine.setState(breakState);
        }
    }

    public StateType getCurrentStateType() {
        return currentStateType;
    }

    public void setAnimation(AnimationType animationType){
        currentAnimationType = animationType;
        changeAnimation = true;
        switch (animationType){
            case LIVE -> sprite.setAlpha(1);
            case BREAK -> sprite.setAlpha(0.5f);
        }
    }

    public AnimationType getCurrentAnimationType() {
        return currentAnimationType;
    }

    public Boolean checkChangeAnimation() {
        if (changeAnimation) {
            changeAnimation = false;
            return true;
        }
        return false;
    }

    public void setColision(Boolean colision){
        fixture.setSensor(!colision);
    }

    @Override
    public void act(float delta) {
        stateMachine.update(delta);
    }

    @Override
    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
