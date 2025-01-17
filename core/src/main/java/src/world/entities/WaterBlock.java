package src.world.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.utils.animation.SheetCutter;
import src.world.ActorBox2d;
import src.world.entities.player.Player;


public class WaterBlock extends Entity implements NoAutoPacketEntity {

    private Vector2 waterDirection = null;

    public WaterBlock(World world, Rectangle shape, AssetManager assetManager, Integer id, Type type) {
        super(world, shape, assetManager, id, type);

        BodyDef def = new BodyDef();
        def.position.set(shape.x + shape.width / 2, shape.y + shape.height/ 2);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 2, shape.height / 2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData(this);
        box.dispose();

        Animation<TextureRegion> loopAnimation = null;
        switch (type){
            case WATERBLOCKD -> {
                loopAnimation = new Animation<>(0.1f,
                    SheetCutter.cutHorizontal(assetManager.get("world/entities/waterBlockY.png"), 8));
                waterDirection = new Vector2(0,-1);
            }
            case WATERBLOCKR -> {
                loopAnimation = new Animation<>(0.1f,
                    SheetCutter.cutHorizontal(assetManager.get("world/entities/water/waterBlockX.png"), 8));
                waterDirection = new Vector2(1,0);
            }

            case WATERBLOCKL -> {
                loopAnimation = new Animation<>(0.1f,
                        SheetCutter.cutHorizontal(assetManager.get("world/entities/water/waterBlockXL.png"), 8));
                for (TextureRegion textureRegion : loopAnimation.getKeyFrames()) {
                    textureRegion.flip(true, false);
                }
                waterDirection = new Vector2(-1,0);
            }

            default -> System.out.println("Error: WaterBlock type not found");
        }
        if (loopAnimation != null) loopAnimation.setPlayMode(Animation.PlayMode.LOOP);

        setCurrentAnimation(loopAnimation);
        fixture.setSensor(true);
    }


    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof Player player) {
            player.se(true,this);
        }
    }

    @Override
    public void endContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof Player player) {
            player.setInWater(false,this);
        }
    }
}
