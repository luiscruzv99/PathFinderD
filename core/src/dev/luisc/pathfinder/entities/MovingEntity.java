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

    private Vector2 vel; //Speed of the entity

    /**
     * Create the moving entity
     * @param spritePath, its sprite
     * @param polygon, its collision box
     * @param pos, its initial position
     * @param vel, its speed
     */
    public MovingEntity(String spritePath, Polygon polygon, Vector2 pos, Vector2 vel){
        super(spritePath, polygon, pos);
        this.vel = new Vector2();

    }

    /**
     * Move the entity and its collision
     */
    @Override
    public void move(){
        getPos().x += vel.x;
        getPos().y += vel.y;

        getCollisionBox().setPosition(getPos().x, getPos().y);
    }

    public void setVel(Vector2 vel){
        this.vel.x = vel.x;
        this.vel.y = vel.y;

        //getCollisionBox().rotate(vel.angle()-90);
    }

    public Vector2 getVel(){
        return vel;
    }

}
