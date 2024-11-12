package src.world.entities.otherPlayer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.utils.CollisionFilters;
import src.utils.animation.SheetCutter;
import src.world.entities.Entity;
import src.world.player.Player;

import static src.utils.Constants.PIXELS_IN_METER;

public class OtherPlayer extends Entity {
    private final String name;
    private final BitmapFont font;
    private final GlyphLayout layout;

    private final Animation<TextureRegion> walkAnimation;
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> jumpAnimation;
    private final Animation<TextureRegion> fallAnimation;
    private final Animation<TextureRegion> downAnimation;
    private final Animation<TextureRegion> runAnimation;
    private final Animation<TextureRegion> dashAnimation;
    private final Animation<TextureRegion> flyAnimation;
    private final Animation<TextureRegion> inFlyAnimation;
    private final Animation<TextureRegion> upFlyAnimation;
    private final Animation<TextureRegion> absorbAnimation;
    private final Animation<TextureRegion> damageAnimation;

    public OtherPlayer(World world, AssetManager assetManager, Rectangle shape, Integer id, String name){
        super(world, id);
        this.name = name;
        sprite = new Sprite();
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);
        this.font = assetManager.get("ui/default.fnt", BitmapFont.class); // Obtén la fuente del AssetManager
        this.layout = new GlyphLayout();

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/4, shape.height/4);
        fixture = body.createFixture(box, 1.5f);
        fixture.setUserData("otherPlayer");
        box.dispose();

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.CATEGORY_OTHERPLAYER;
        filter.maskBits = CollisionFilters.MASK_PLAYER;
        fixture.setFilterData(filter);

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);
        setSpritePosModification(0f, getHeight()/4);

        walkAnimation = new Animation<>(0.12f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyWalk.png"), 10));
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        idleAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyIdle.png"), 31));
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);

        downAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyDown.png"), 31));
        downAnimation.setPlayMode(Animation.PlayMode.LOOP);

        jumpAnimation = new Animation<>(1,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyJump.png"), 1));

        fallAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyFall.png"), 22));

        runAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyRun.png"), 8));
        runAnimation.setPlayMode(Animation.PlayMode.LOOP);

        dashAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyDash.png"), 2));

        flyAnimation = new Animation<>(0.04f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyFly.png"), 5));

        inFlyAnimation = new Animation<>(0.1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyInFly.png"), 2));
        inFlyAnimation.setPlayMode(Animation.PlayMode.LOOP);

        upFlyAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyUpFly.png"), 6));

        absorbAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyAbsorb.png"), 16));

        damageAnimation = new Animation<>(0.06f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/kirby/kirbyDamage.png"), 9));

        setAnimation(Player.AnimationType.IDLE);
    }

    public void setAnimation(Player.AnimationType animationType){
        switch (animationType){
            case IDLE -> setCurrentAnimation(idleAnimation);
            case WALK -> setCurrentAnimation(walkAnimation);
            case JUMP -> setCurrentAnimation(jumpAnimation);
            case FALL -> setCurrentAnimation(fallAnimation);
            case DOWN -> setCurrentAnimation(downAnimation);
            case RUN -> setCurrentAnimation(runAnimation);
            case DASH -> setCurrentAnimation(dashAnimation);
            case FLY -> setCurrentAnimation(flyAnimation);
            case INFLY -> setCurrentAnimation(inFlyAnimation);
            case UPFLY -> setCurrentAnimation(upFlyAnimation);
            case ABSORB -> setCurrentAnimation(absorbAnimation);
            case DAMAGE -> setCurrentAnimation(damageAnimation);
        }
    }

    @Override
    public void act(float delta) {
        //stateMachine.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        layout.setText(font, name + " " + getId());
        font.draw(batch, layout, getX() - layout.width / 4, getY() + sprite.getHeight() + layout.height/4);
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
