package dev.luisc.pathfinder.collisions;

import dev.luisc.pathfinder.entities.Entity;

public interface CollisionEvent {

    void onCollision(Entity e);
}
