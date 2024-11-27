package src.world.entities.staticEntity.breakBlocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.utils.stateMachine.StateMachine;
import src.world.entities.staticEntity.StaticEntity;
import src.world.entities.staticEntity.breakBlocks.states.BreakState;
import src.world.entities.staticEntity.breakBlocks.states.LiveState;

public class BreakBlock extends StaticEntity {
    public enum StateType {LIVE,BREAK}
    private final StateMachine stateMachine;
    private final LiveState liveState;
    private final BreakState breakState;

    public enum AnimationType{LIVE,BREAK}

    public BreakBlock(World world, Rectangle shape, AssetManager assetManager, Integer id) {
        super(world, shape, assetManager,id, Type.BREAKBLOCK);
        sprite.setTexture((assetManager.get("world/entities/breakBlock.png")));

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/2, shape.height/2);
        fixture = body.createFixture(box, 1.5f);
        fixture.setUserData(this);
        box.dispose();

        stateMachine = new StateMachine();
        liveState = new LiveState(this);
        breakState = new BreakState(this);
        stateMachine.setState(liveState);
    }
    public void setState(StateType stateType){
        switch (stateType){
            case LIVE -> stateMachine.setState(liveState);
            case BREAK -> stateMachine.setState(breakState);
        }
    }

    public void setAnimation(AnimationType animationType){
        switch (animationType){
            case LIVE -> sprite.setAlpha(1);
            case BREAK -> sprite.setAlpha(0.5f);
        }
    }

    public void setColision(Boolean colision){
        fixture.setSensor(!colision);
    }

    @Override
    public void act(float delta) {
        stateMachine.update(delta);
    }
}
