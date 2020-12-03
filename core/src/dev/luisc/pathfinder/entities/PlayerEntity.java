package dev.luisc.pathfinder.entities;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Class that controls the player inside the game
 *
 * @author Luis
 * @version 10-11-2020
 */
public class PlayerEntity extends MovingEntity {

    private float rotation; //Rotation of the player
    private float rotationSpeed; //Speed at which the player rotates

    private float speedComponent; //Total speed of the player
    private float acceleration; //Acceleration of the player, always in the direction the polygon is pointing

    private ArrayList<Entity> projectiles;


    /**
     * Create the player entity, with its predefined hitbox (Change hitbox in the future)
     * @param pos, initial position of the player
     */
    public PlayerEntity(Vector2 pos) {

        super("playerTest.png" , new Polygon(new float[]{
                0,0,
                0,40,
                50,20}),pos, new Vector2(0,0));

        getCollisionBox().setOrigin(30,20);
        this.speedComponent = 0;
        this.acceleration = 0;
        this.rotation = 0;
        this.rotationSpeed = 0;
        projectiles = new ArrayList<>();
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
    }

    /**
     * Accelerate the player up to a top speed
     * @param direction
     */
    public void accelerate(boolean direction){
        if(direction && speedComponent<10){
            acceleration+=0.5;
        }else{
            if(speedComponent>-7)acceleration-=0.25;
        }
    }

    @Override
    public void postDeSerialize() {
        super.postDeSerialize();
        projectiles = new ArrayList<>();
    }

    @Override
    public void preSerialize() {
        super.preSerialize();
        if(!projectiles.isEmpty()){
            for(Entity e : projectiles){
                e.revive();
                e.preSerialize();
            }
            projectiles.clear();

        }
        projectiles = null;
    }

    /**
     * Boost the player's speed for a short time
     */
    public void boost(){
        if(speedComponent<5) acceleration=10;
    }

    /**
     * Move the player, taking into account the rotation
     */
    @Override
    public void move() {
        rotationSpeed += -0.1 * rotationSpeed; //Decay rotation speed

        //Rotate the player, rotate less the quicker it goes
        rotation += rotationSpeed / (Math.abs(speedComponent) * 0.1 + 1);

        if (rotation < 0) rotation += 360; //Keep rotation in bounds

        acceleration += -0.5 * acceleration; //Decay acceleration

        //Accelerate if top speed is not reached
        if (speedComponent > -7 && speedComponent < 15) speedComponent += acceleration;
        speedComponent += -0.025 * speedComponent; //Decay speed

        //Calculate the speed of each component, taking into account the rotation
        getVel().x += acceleration * Math.cos(Math.toRadians(rotation)) - getVel().x * 0.025;
        getVel().y += acceleration * Math.sin(Math.toRadians(rotation)) - getVel().y * 0.025;

        //Move the player
        getPos().x += getVel().x;
        getPos().y += getVel().y;

        //Keep the rotation in bounds TODO: Check this code, may be redundant
        if (rotation > 360) rotation = 0;
        else if (rotation < -360) rotation = 0;

        //Move the collision of the player and set its rotation
        getCollisionBox().setRotation(rotation);
        getCollisionBox().setPosition(getPos().x, getPos().y);

        if(speedComponent>9)
            System.out.println(speedComponent);

        //TODO: MOVE THIS TO EXTERNAL METHOD
        ArrayList<Entity> dead = new ArrayList<>();
        for(Entity e: projectiles)
            if(!e.alive())
                dead.add(e);

        for(Entity e: dead) {
            projectiles.remove(e);
            e.revive();
            ProjectilePool.getInstance().receiveAsteroid((MovingEntity) e);
        }
        dead.clear();
    }

    public float getRotation(){
        return rotation;
    }

    @Override
    public void levelCollision(){
        if(hitPoints>0) hitPoints--;
        speedComponent=speedComponent*(-1);
        getVel().x=-1*getVel().x;
        getVel().y=-1*getVel().y;
    }

    public void shoot(){

        float direction = getRotation(); //Get the direction the player's facing
        float speed = 30; //Set a fixed speed of 30 px/f

        Vector2 pos =  new Vector2(); //Create the position vector
        pos.x = getCollisionBox().getTransformedVertices()[4]+5*(float)Math.cos(Math.toRadians(direction));
        pos.y = getCollisionBox().getTransformedVertices()[5]+5*(float)Math.sin(Math.toRadians(direction));

        Vector2 dir = new Vector2((float)Math.cos(Math.toRadians(direction))*speed,
                (float)Math.sin(Math.toRadians(direction))*speed); //Create the velocity vector

        MovingEntity e = (MovingEntity)ProjectilePool.getInstance().lendAsteroid(); //Get a projectile from the pool

        //Set its attributes
        e.setPos(pos);
        e.setVel(dir);
        e.getCollisionBox().setRotation(getRotation()-90);
        e.setHitPoints(1);
        //Add to the shot projectiles
        projectiles.add(e);
    }

    public ArrayList<Entity> getProjectiles() {
        return projectiles;
    }

    public float getSpeedComponent(){
        return speedComponent;
    }
}
