package src.world.entities.player.powers;

import src.world.entities.player.Player;

public class PowerSleep extends PowerUp
{

    public PowerSleep(Player player)
    {
        super(player);
    }

    @Override
    public void start()
    {
        player.stunTime = 5;
        player.setState(Player.StateType.STUN);
        player.setAnimation(Player.AnimationType.SLEEP);
    }
}
