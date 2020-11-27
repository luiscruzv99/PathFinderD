package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.entities.Entity;
import jdk.nashorn.api.scripting.JSObject;

import java.util.ArrayList;


/**
 * Class that contains all the information of a
 * cinematic of the game, in which predetermined entities
 * follow predetermined paths in order to tell part of the
 * argument
 *
 * @author Luis
 * @version 10-11-2020
 */
public class CinematicLevel extends Level {

    ArrayList<Texture> scenes; //Scenes of the cinematic
    ArrayList<Entity> entities; // Entities of the cinematic

    //Don't like this one...
    ArrayList<ArrayList<Vector2>> movements; //Movements of the entities within the cinematic

    public CinematicLevel() {
        super();
    }

    @Override
    public boolean render() {
        return endState;
    }
}
