package dev.luisc.pathfinder.levels;

import dev.luisc.pathfinder.entities.Entity;

import java.util.ArrayList;

public class ChaseLevel extends Level {

    ArrayList<Entity> obstacles;
    ArrayList<Entity> enemies;

    public ChaseLevel() {
        super();
    }

    @Override
    public boolean render() {
        return true;
    }
}
