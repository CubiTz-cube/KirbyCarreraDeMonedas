package src.world.player.powers;

import src.world.player.Player;

public abstract class PowerUp
{
    public enum Type{ NULL, SLEEP, SWORD, BOMB}
    protected Player player;

    public PowerUp(Player player)
    {
        this.player = player;
    }

    public abstract void start();

}
