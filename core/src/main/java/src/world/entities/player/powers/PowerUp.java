package src.world.entities.player.powers;

import src.world.entities.player.Player;

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
