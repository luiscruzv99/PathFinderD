package dev.luisc.pathfinder.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Class that controls an entity capable of moving with a constant speed
 *
 * @author Luis
 * @version 10-11-2020
 */
public class MovingEntity extends Entity {

    Vector2 vel; //Speed of the entity

    /**
     * Create the moving entity
     * @param sprite, its sprite
     * @param polygon, its collision box
     * @param pos, its initial position
     * @param vel, its speed
     */
    public MovingEntity(Sprite sprite, Polygon polygon, Vector2 pos, Vector2 vel){
        super(sprite, polygon, pos);
        this.vel = vel;
    }

    /**
     * Move the entity and its collision
     */
    public void move(){
        pos.x += vel.x;
        pos.y += vel.y;

        collisionBox.setPosition(pos.x, pos.y);
    }

}
