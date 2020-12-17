package dev.luisc.pathfinder.AI;

import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.entities.AiEntity;

import java.util.ArrayList;
import java.util.List;

//ESTO NO FUNCIONA BIEN
public class FriendlySwarm implements Swarm{

    private ArrayList<AiEntity> particles;

    private AiEntity bestParticle;

    private Vector2 objective;

    public FriendlySwarm(List<AiEntity> particles){
        this.particles = (ArrayList) particles;
        bestParticle = particles.get(0);
    }

    public void setObjective(Vector2 objective){
        this.objective = objective;
    }


    @Override
    public void move() {

        bestFitness();

        for(AiEntity e : particles){
            e.getVel().y += COGNITIVE_LR*Math.random()*(e.getBestPos().y-e.getPos().y)+SOCIAL_LR*Math.random()*(bestParticle.getBestPos().y-e.getPos().y);
            e.getVel().x += COGNITIVE_LR*Math.random()*(e.getBestPos().x-e.getPos().x)+SOCIAL_LR*Math.random()*(bestParticle.getBestPos().x-e.getPos().x);
            e.calculateFitness(objective);
            e.move();
        }

    }

    @Override
    public void bestFitness() {

        for(AiEntity e: particles){
            if(e.getBestFitness() < bestParticle.getBestFitness()){
                this.bestParticle = e;
            }
        }
    }

    public ArrayList<AiEntity> getParticles() {
        return particles;
    }
}
