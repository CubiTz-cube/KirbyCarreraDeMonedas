package src.world.player.powers;

import src.world.player.Player;

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
    }
}
