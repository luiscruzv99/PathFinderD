package dev.luisc.pathfinder.levels;

import dev.luisc.pathfinder.entities.Entity;
import dev.luisc.pathfinder.entities.PlayerEntity;
import jdk.nashorn.api.scripting.JSObject;

import java.util.ArrayList;

/**
 * Class that contains the information about a Navigation
 * phase of the game, where the player has to place beacons
 * for a "swarm" of ships to follow it
 *
 * @author Luis
 * @version 10-11-2020
 */
public class NavLevel extends Level {

    ArrayList<Entity> obstacles; //Entities that hinder player movement
    ArrayList<Entity> allies; //Allies that follow the beacons planted by the player
    ArrayList<Entity> beacons; // Beacons placed by the player to guide the allies

    PlayerEntity player; //Player object

    public enum Phase {PLAYER, AI} //The different phases of the level (May be unnecessary??)

    /**
     * Creates the Navigation Level with its info
     * @param levelInfo the info of the level
     */
    public NavLevel(JSObject levelInfo) {
        super(levelInfo);
    }

    @Override
    public boolean render() {
        return true;
    }

    /**
     * Changes the phase of the level, from player controlled to AI controlled
     */
    private void phaseChange() {

    }

    /**
     * Checks whether the criteria for changing phase is met
     */
    private void phaseCondition() {

    }


}
