package dev.luisc.pathfinder.entities;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class AiEntity extends MovingEntity{

    private Vector2 bestPos; //The best position found by the particle

    private float fitness; //The fitness of the current position

    private float bestFitness; //The best fitness found by the particle

    /**
     * Create the moving entity
     *
     * @param spritePath
     * @param polygon
     * @param pos
     * @param vel
     */
    public AiEntity(String spritePath, Polygon polygon, Vector2 pos, Vector2 vel) {
        super(spritePath, polygon, pos, vel);
        bestPos = pos;
        fitness = Float.MAX_VALUE;
        bestFitness = fitness;
    }


    public Vector2 getBestPos() {
        return bestPos;
    }

    public float getBestFitness(){
        return bestFitness;
    }

    public void calculateFitness(Vector2 objective){
        fitness = getPos().dst(objective);
        if(fitness < bestFitness){
            bestFitness = fitness;
            bestPos = getPos().cpy();
        }
    }

    @Override
    public void move(){
        if(getVel().x >= 450) getVel().x = 400;
        if(getVel().y >= 450) getVel().y = 400;
        getCollisionBox().setRotation(getVel().angle());
        super.move();
    }


    @Override
    public void preSerialize(){
        super.preSerialize();
        bestFitness = Float.NaN;
        bestPos = null;
        fitness = Float.NaN;
    }

    @Override
    public void postDeSerialize(){
        super.postDeSerialize();
        bestFitness = Float.MAX_VALUE;
        fitness = bestFitness;
        bestPos = new Vector2(getPos());
    }
}
