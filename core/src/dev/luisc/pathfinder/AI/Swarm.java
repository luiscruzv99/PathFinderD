package dev.luisc.pathfinder.AI;

import com.badlogic.gdx.math.Vector2;

public interface Swarm {

    static final float COGNITIVE_LR = 0.5f;
    static final float SOCIAL_LR = 0.5f;

    void move();

    void bestFitness();

    void setObjective(Vector2 objective);

}
