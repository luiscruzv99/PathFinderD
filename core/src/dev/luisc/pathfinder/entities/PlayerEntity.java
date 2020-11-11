package dev.luisc.pathfinder.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Class that controls the player inside the game
 *
 * @author Luis
 * @version 10-11-2020
 */
public class PlayerEntity extends MovingEntity {

    float rotation; //Rotation of the player
    float rotationSpeed; //Speed at which the player rotates

    float speedComponent; //Total speed of the player
    float acceleration; //Acceleration of the player, always in the direction the polygon is pointing

    /**
     * Create the player entity, with its predefined hitbox (Change hitbox in the future)
     * @param pos, initial position of the player
     */
    public PlayerEntity(Vector2 pos) {

        super(null , new Polygon(new float[]{
                0,0,
                0,40,
                50,20}),pos, new Vector2(0,0));

        collisionBox.setOrigin(30,20);

        this.speedComponent = 0;
        this.acceleration = 0;
        this.rotation = 0;
        this.rotationSpeed = 0;
    }

    /**
     * Prepare to rotate the player
     * @param direction in which to rotate
     */
    public void rotate(boolean direction){

        if(direction && rotationSpeed>-7){
            rotationSpeed-=2;
        }else{
            if(rotationSpeed<7)rotationSpeed+=2;
        }
        acceleration+=0.1;
    }

    /**
     * Accelerate the player up to a top speed
     * @param direction
     */
    public void accelerate(boolean direction){
        if(direction && speedComponent<25){
            acceleration+=1;
        }else{
            if(speedComponent>-10)acceleration-=0.25;
        }
    }

    /**
     * Boost the player's speed for a short time
     */
    public void boost(){
        if(speedComponent<15) acceleration=50;
    }

    /**
     * Move the player, taking into account the rotation
     */
    @Override
    public void move() {
        rotationSpeed += -0.08 * rotationSpeed; //Decay rotation speed

        //Rotate the player, rotate less the quicker it goes
        rotation += rotationSpeed / (Math.abs(acceleration + speedComponent / 4) * 0.05 + 1);

        if (rotation < 0) rotation += 360; //Keep rotation in bounds

        acceleration += -0.5 * acceleration; //Decay acceleration

        //Accelerate if top speed is not reached
        if (speedComponent > -35 && speedComponent < 35) speedComponent += acceleration;
        speedComponent += -0.025 * speedComponent; //Decay speed

        //Calculate the speed of each component, taking into account the rotation
        vel.x += acceleration * Math.cos(Math.toRadians(rotation)) - vel.x * 0.025;
        vel.y += acceleration * Math.sin(Math.toRadians(rotation)) - vel.y * 0.025;

        //Move the player
        pos.x += vel.x;
        pos.y += vel.y;

        //Keep the rotation in bounds TODO: Check this code, may be redundant
        if (rotation > 360) rotation = 0;
        else if (rotation < -360) rotation = 0;

        //Move the collision of the player and set its rotation
        collisionBox.setRotation(rotation);
        collisionBox.setPosition(pos.x, pos.y);
    }
}