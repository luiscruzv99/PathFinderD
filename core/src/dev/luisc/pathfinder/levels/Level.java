package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.entities.Entity;
import jdk.nashorn.api.scripting.JSObject;

/**
 * Class that contains all the information about a level,
 * including entities, bounds and completion conditions.
 *
 * @author Luis
 * @version 10-11-2020
 */
public class Level {

    Polygon bounds; //Bounds of the level
    Texture background; //Background image of the level
    Vector2 startPoint; //Starting point of the player (May be unnecessary??)
    String backgroundPath;

    Entity entity;

    boolean endState; //Indicator whether the level has been completed
    boolean failState; //Indicator whether the player has failed the level

    /**
     * Populates the level with the information extracted from the JSON file
     *
     * @param levelInfo, the information of the level
     */
    public Level(JSObject levelInfo) {
        //TODO: Load the level data from a JSON file
    }

    /**
     * Renders the action that happens in the level, in order to offload
     * code from the main render (Maybe not a good idea...)
     * @return whether the level has been completed, thus ending the render
     */
    public boolean render() {

        return true;
    }

    /**
     * Checks if the criteria for completing the level has been met
     * and changes the endState variable
     */
    private void endCondition() {

    }

    /**
     * Checks if the criteria for failing a level has been met and
     * changes the failState variable
     */
    private void failCondition() {

    }

    public void postDeSerialize(){
        background = new Texture(backgroundPath);
        entity.postDeSerialize();
    }

    public void preSerialize(){
        background = null;
        entity.preSerialize();
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public void setBounds(Polygon bounds) {
        this.bounds = bounds;
    }

    public void setStartPoint(Vector2 startPoint) {
        this.startPoint = startPoint;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public Polygon getBounds() {
        return bounds;
    }

    public Texture getBackground(){
        return background;
    }
}
