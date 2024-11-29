package src.world.entities.blocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import src.net.packets.Packet;
import src.screens.GameScreen;
import src.world.ActorBox2d;
import src.world.entities.player.Player;

public class FallBlock extends Block{
    private Float timeBreak;
    private Boolean toBreak;

    public FallBlock(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game) {
        super(world, shape, assetManager, id, Type.FALLBLOCK, game);
        sprite.setTexture((assetManager.get("world/entities/breakBlock.png")));
        timeBreak = 0.2f;
        toBreak = false;
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
            game.sendPacket(Packet.actBlock(getId(), BreakBlock.StateType.BREAK));
        }
    }

    @Override
    public void beginContactWith(ActorBox2d actor, GameScreen game) {
        if (actor instanceof Player){
            toBreak = true;
        }
    }
}
