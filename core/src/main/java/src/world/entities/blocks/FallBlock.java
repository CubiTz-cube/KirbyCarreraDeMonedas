package src.world.entities.blocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.game.GameScreen;
import src.utils.animation.SheetCutter;
import src.world.ActorBox2d;
import src.world.entities.player.Player;

public class FallBlock extends Block{
    private Float timeBreak;
    private Boolean toBreak;
    private final Animation<TextureRegion> breakIdleAnimation;
    private final Animation<TextureRegion> breakAnimation;

    public FallBlock(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game) {
        super(world, shape, assetManager, id, Type.FALLBLOCK, game);
        sprite.setTexture((assetManager.get("world/entities/blocks/fallBlock.png")));
        timeBreak = 0.2f;
        toBreak = false;

        breakIdleAnimation = new Animation<>(1f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/blocks/fallBlock.png"), 1));

        breakAnimation = new Animation<>(0.07f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/blocks/fallBlockBreak.png"), 3));
    }

    @Override
    public void setColision(Boolean colision) {
        super.setColision(colision);
        if (!colision){
            timeBreak = 0.2f;
            toBreak = false;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!toBreak) return;
        if (timeBreak > 0) timeBreak -= delta;
        else {
            setState(StateType.BREAK);
            setCurrentAnimation(breakIdleAnimation);
        }
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof Player){
            toBreak = true;
            setCurrentAnimation(breakAnimation);
        }
    }
}
