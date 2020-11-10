package dev.luisc.pathfinder.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Class that controls a static entity (static obstacle in-game?)
 *
 * @author Luis
 * @version 10-11-2020
 */
public class Entity {

    Sprite sprite; //Sprite of the entity
    Polygon collisionBox; //Collision box
    Vector2 pos; //Position

    //Empty private constructor to prevent from doing super()
    private Entity(){}

    /**
     * Create the entity
     * @param sprite, its sprite
     * @param polygon, its collision box
     * @param pos, its position
     */
    public Entity(Sprite sprite, Polygon polygon, Vector2 pos){
        this.sprite=sprite;
        this.collisionBox=polygon;
        this.pos=pos;
        collisionBox.setPosition(pos.x, pos.y);
    }

    /**
     * Getters for the attributes
     */

    public Sprite getSprite() {
        return sprite;
    }

    public Polygon getCollisionBox() {
        return collisionBox;
    }

    public Vector2 getPos() {
        return pos;
    }
}
