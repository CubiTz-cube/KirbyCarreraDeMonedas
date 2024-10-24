package src.world.entities;

import src.world.ActorBox2d;

public abstract class Entity extends ActorBox2d {
    private Integer id;

    public Integer getId() {
        return id;
    }
}
