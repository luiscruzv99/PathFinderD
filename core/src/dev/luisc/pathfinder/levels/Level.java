package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.entities.Entity;
import dev.luisc.pathfinder.entities.PlayerEntity;
import jdk.nashorn.api.scripting.JSObject;

import java.util.ArrayList;
import java.util.List;

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
    public SpriteBatch batch;

    public ShapeRenderer shapeRenderer;
    ArrayList<Entity> entities;
    public PlayerEntity playerTest;


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

        batch.begin();
        batch.draw(background, 0,0);
        for(Entity entity: entities)batch.draw(entity.getSprite(), entity.getPos().x, entity.getPos().y);
        batch.draw(playerTest.getSprite(), playerTest.getPos().x, playerTest.getPos().y,
                30,20,50,40,1,1,playerTest.getRotation());
        batch.end();
        return endState;
    }

    public ShapeRenderer debugRender(){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 0.0f);

        shapeRenderer.polygon(playerTest.getCollisionBox().getTransformedVertices());
        for(Entity entity: entities) shapeRenderer.polygon(entity.getCollisionBox().getTransformedVertices());
        shapeRenderer.polygon(bounds.getTransformedVertices());
        shapeRenderer.end();
        return shapeRenderer;
    }

    public void cleanUp(){batch.dispose();}

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
        for(Entity entity: entities)entity.postDeSerialize();
        batch = new SpriteBatch();
        playerTest = new PlayerEntity(new Vector2(100,100));
        shapeRenderer = new ShapeRenderer();
    }

    public void preSerialize(){
        background = null;
        for(Entity entity: entities)entity.preSerialize();
        batch = null;
        playerTest = null;
        shapeRenderer = null;
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

    public void setEntities(List<Entity> entities) {
        this.entities = (ArrayList<Entity>) entities;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Polygon getBounds() {
        return bounds;
    }

    public Texture getBackground(){
        return background;
    }
}
