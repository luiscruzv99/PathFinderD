package dev.luisc.pathfinder.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Class that controls a static entity
 *
 * @author Luis
 * @version 10-11-2020
 */
public class Entity {

    private Sprite sprite; //Sprite of the entity
    private Polygon collisionBox; //Collision box
    private Vector2 pos; //Position
    private String spritePath; //Path to the texture
    private int FULL_HEALTH; //Full health of the entity
    int hitPoints=2;

    //Empty private constructor to prevent from doing super()
    private Entity(){}

    /**
     * Create the entity
     * @param spritePath, its sprite
     * @param polygon, its collision box
     * @param pos, its position
     */
    public Entity(String spritePath, Polygon polygon, Vector2 pos){
        this.spritePath = spritePath;
        this.collisionBox=polygon;
        this.pos=pos;
        hitPoints=2;
        FULL_HEALTH = hitPoints;
        if(spritePath != null)
            postDeSerialize();
    }

    /**
     * Getters for the attributes
     */

    public void postDeSerialize(){
        sprite = new Sprite(new Texture(spritePath));
        sprite.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

    }

    public void preSerialize(){
        if(sprite != null) sprite.getTexture().dispose();
        sprite = null;
        collisionBox = null;
        pos = null;
        hitPoints=FULL_HEALTH;
    }

    public void revive(){
        hitPoints=FULL_HEALTH;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Polygon getCollisionBox() {
        return collisionBox;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void collision() {
        if(hitPoints>0) hitPoints--;
    }

    public boolean alive(){
        if(hitPoints==0)return false;
        return true;
    }

    public void levelCollision(){
        hitPoints = 0;
    }

    public void move(){}

    public void setPos(Vector2 pos){
        this.pos = pos;
        collisionBox.setPosition(pos.x, pos.y);
    }

    public void setHitPoints(int hp){
        this.hitPoints = hp;
    }
}
