package dev.luisc.pathfinder.AI;

import com.badlogic.gdx.math.Vector2;

public interface Swarm {

    static final float COGNITIVE_LR = 0.1f;
    static final float SOCIAL_LR = 0.3f;

    void move();

    void bestFitness();

    void setObjective(Vector2 objective);

}
