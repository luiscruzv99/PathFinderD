package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dev.luisc.pathfinder.entities.Entity;
import jdk.nashorn.api.scripting.JSObject;

import java.util.ArrayList;

/**
 * Class that contains the information about a chase phase
 * of the game, where the player is chased by enemies and has
 * to reach a destination
 *
 * @author Luis
 * @version 10-11-2020
 */
public class ChaseLevel extends Level {

    ArrayList<Entity> obstacles; //Obstacles in the level
    ArrayList<Entity> enemies; //Enemies chasing the player

    /**
     * Creates the chase level, with all the info
     * @param levelInfo, the info of the level
     */
    public ChaseLevel(JSObject levelInfo) {
        super(levelInfo);
    }

    @Override
    public boolean render() {
        return endState;
    }
}
