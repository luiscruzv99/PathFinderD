package dev.luisc.pathfinder.handlers;


import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import dev.luisc.pathfinder.entities.Entity;

public class CollisionHandler {

    public static void isCollidingEntity(Entity e1, Entity e2){
        if(Intersector.overlapConvexPolygons(e1.getCollisionBox(),e2.getCollisionBox())){
            e1.collision();
            e2.collision();
        }
    }

    public static void isCollidingLevel(Entity e, Polygon p){
        if(!Intersector.overlapConvexPolygons(e.getCollisionBox(),p)){
            e.levelCollision();
        }
    }

}
