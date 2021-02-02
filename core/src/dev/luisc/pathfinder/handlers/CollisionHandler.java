package dev.luisc.pathfinder.handlers;


import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import dev.luisc.pathfinder.entities.Entity;

/**
 * Checks collisions between entities and with the level
 */
public class CollisionHandler {

    /**
     * Checks for collisions between entities
     * @param e1
     * @param e2
     */
    public static void isCollidingEntity(Entity e1, Entity e2){
        if(Intersector.overlapConvexPolygons(e1.getCollisionBox(),e2.getCollisionBox())){
            e1.collision();
            e2.collision();
        }
    }

    /**
     * Checks for collisions between an entity and the level bounds
     * @param e
     * @param p
     */
    public static void isCollidingLevel(Entity e, Polygon p){
        if(!Intersector.overlapConvexPolygons(e.getCollisionBox(),p)){
            e.levelCollision();
        }
    }

}
