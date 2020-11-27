package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dev.luisc.pathfinder.entities.Entity;
import jdk.nashorn.api.scripting.JSObject;

import java.util.ArrayList;

/**
 * Class that contains the information about an attack phase
 * of the game, where the player is supposed to find a path
 * for the allies to attack an enemy target
 *
 * @author Luis
 * @version 10-11-2020
 */
public class AttackLevel extends NavLevel {

    ArrayList<Entity> enemies; //Enemies present in the level, with little AI

    /**
     * Creates the attack level, with all the info
     * @param levelInfo, the info of the level
     */
    public AttackLevel() {
        super();
    }

    @Override
    public boolean render() {
        return endState;
    }
}
