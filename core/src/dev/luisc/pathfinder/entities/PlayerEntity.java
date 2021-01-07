package dev.luisc.pathfinder.entities;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.levels.Level;

import java.util.ArrayList;

/**
 * Class that controls the player inside the game
 *
 * @author Luis
 * @version 10-11-2020
 */
public class PlayerEntity extends MovingEntity {

    private float rotation; //Rotation of the player (degrees)
    private float rotationSpeed; //Speed at which the player rotates (degrees/s)

    private float speedComponent; //Total speed of the player (px/s)
    private float acceleration; //Acceleration of the player (px/s^2)

    public static final float MAX_SPEED = 750.0f; //Max speed of the player (px/s)
    private int timer = 0;

    private int beaconsPlaced = 0;

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

        if(direction && rotationSpeed > -180){
            rotationSpeed -= 30;
        }else{
            if(rotationSpeed < 180)rotationSpeed += 50;
        }
    }

    /**
     * Accelerate the player up to a top speed
     * @param direction
     */
    public void accelerate(boolean direction){
        if(direction && (speedComponent < MAX_SPEED))
            acceleration += MAX_SPEED;
        else
            if(speedComponent > -MAX_SPEED/1.5 )acceleration -= MAX_SPEED/4;

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
        if(speedComponent<MAX_SPEED/8 && timer > 200 ){
            acceleration=MAX_SPEED*250;
            timer=0;
        }
    }

    /**
     * Move the player, taking into account the rotation
     */
    @Override
    public void move() {

        if(speedComponent < MAX_SPEED && speedComponent > -MAX_SPEED/3) {
            speedComponent += (acceleration * Level.TICK_TIME) - (2 * speedComponent * Level.TICK_TIME);
        }else{
            speedComponent -= 2*speedComponent* Level.TICK_TIME;
        }
        acceleration += (-3*acceleration*Level.TICK_TIME);

        getPos().x += (float) Math.cos(Math.toRadians(rotation)) * (speedComponent * Level.TICK_TIME + acceleration * Math.pow(Level.TICK_TIME, 2));
        getPos().y += (float) Math.sin(Math.toRadians(rotation)) * (speedComponent * Level.TICK_TIME + acceleration * Math.pow(Level.TICK_TIME, 2));

        rotation += rotationSpeed * Level.TICK_TIME;
        rotationSpeed += -5 * rotationSpeed * Level.TICK_TIME;

        if(Math.abs(speedComponent) < 1) speedComponent = 0.0f;

        getCollisionBox().setRotation(rotation);

        getCollisionBox().setPosition(getPos().x, getPos().y);
        timer++;

    }

    public void deSpawnProjectiles(){

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
    }

    public void shoot(){

        float direction = getRotation(); //Get the direction the player's facing
        float speed = 1500; //Set a fixed speed of 1500 px/s

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
        Level.playSchut();
    }

    public ArrayList<Entity> getProjectiles() {
        return projectiles;
    }

    public float getSpeedComponent(){
        return speedComponent;
    }

    public int getBeaconsPlaced(){
        return beaconsPlaced;
    }

    public void placeBeacon(){
        beaconsPlaced++;
    }

    public void fullStop(){
        acceleration = 0;
        speedComponent = 0;
    }
}
