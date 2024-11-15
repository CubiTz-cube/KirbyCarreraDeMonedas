package src.world.entities.enemies.basic;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.utils.animation.SheetCutter;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.basic.states.*;

import static src.utils.variables.Constants.PIXELS_IN_METER;

public class BasicEnemy extends Enemy {
    private final BitmapFont font;
    private final GlyphLayout layout;

    public enum AnimationType {
        IDLE,
        WALK
    }
    private AnimationType currentAnimationType;
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> walkAnimation;

    public BasicEnemy(World world, AssetManager assetManager, Rectangle shape, Integer id) {
        super(world, assetManager, shape,id);
        type = Type.BASIC;
        sprite = new Sprite();
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);
        this.font = assetManager.get("ui/default.fnt", BitmapFont.class);
        this.layout = new GlyphLayout();

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/4, shape.height/4);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("enemy");
        box.dispose();
        body.setFixedRotation(true);

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);
        setSpritePosModification(0f, getHeight()/4);

        idleState = new IdleStateBasic(this);
        walkState = new WalkStateBasic(this);
        setState(StateType.IDLE);

        idleAnimation = new Animation<>(0.12f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/basic/basicIdle.png"), 1));

        walkAnimation = new Animation<>(0.12f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/basic/basicWalk.png"), 8));
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
        setCurrentAnimation(idleAnimation);
    }

    public void setAnimation(AnimationType type){
        currentAnimationType = type;
        switch (type){
            case IDLE:
                setCurrentAnimation(idleAnimation);
                break;
            case WALK:
                setCurrentAnimation(walkAnimation);
                break;
        }
    }

    public AnimationType getCurrentAnimationType() {
        return currentAnimationType;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        layout.setText(font, "ID " + getId() + " TIME " + getActCrono());
        font.draw(batch, layout, getX() + layout.width / 2, getY() + sprite.getHeight() + layout.height);
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
