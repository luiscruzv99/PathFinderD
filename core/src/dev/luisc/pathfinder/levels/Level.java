package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.collisions.CollisionHandler;
import dev.luisc.pathfinder.entities.MovingEntity;
import dev.luisc.pathfinder.entities.ProjectilePool;
import dev.luisc.pathfinder.entities.Entity;
import dev.luisc.pathfinder.entities.PlayerEntity;

import java.util.ArrayList;

/**
 * Class that contains all the information about a level,
 * including entities, bounds and completion conditions.
 *
 * @author Luis
 * @version 10-11-2020
 */
public class Level {

    private Polygon bounds; //Bounds of the level
    private Texture background; //Background image of the level
    private Vector2 startPoint; //Starting point of the player (May be unnecessary??)
    private String backgroundPath; //Path of the background of the level
    private SpriteBatch batch;

    private ShapeRenderer shapeRenderer;

    private ArrayList<Entity> entities;
    private PlayerEntity playerTest;

    private ArrayList<Vector2> entitiesPositions;

    private boolean endState; //Indicator whether the level has been completed
    private boolean failState; //Indicator whether the player has failed the level

    /**
     * Populates the level with the information extracted from the JSON file
     * TODO: add all the info of the level to make them easier to create
     */
    public Level(ArrayList<Vector2> positions, Vector2 startPoint, Polygon bounds, String bgPath) {

        this.entitiesPositions = positions;
        this.startPoint = startPoint;
        this.bounds = bounds;
        this.backgroundPath = bgPath;
        this.entities = null;

    }

    /**
     * Renders the action that happens in the level, in order to offload
     * code from the main render (Maybe not a good idea...)
     * @return whether the level has been completed, thus ending the render
     */
    public boolean render() {
        checkCollisions();
        aliveEntities();
        playerTest.move();

        batch.begin();
        batch.draw(background, 0,0);
        for(Entity entity: entities){
            batch.draw(entity.getSprite(), entity.getPos().x, entity.getPos().y,0,0,
                    entity.getSprite().getWidth(), entity.getSprite().getHeight(),1,1,
                    entity.getCollisionBox().getRotation());
            entity.move();
        }
        for(Entity entity: playerTest.getProjectiles()){
            batch.draw(entity.getSprite(), entity.getPos().x, entity.getPos().y,0,0,
                    entity.getSprite().getWidth(), entity.getSprite().getHeight(),1,1,
                    entity.getCollisionBox().getRotation());
            entity.move();
        }
        batch.draw(playerTest.getSprite(), playerTest.getPos().x, playerTest.getPos().y,
                30,20,50,40,1,1,playerTest.getRotation());
        batch.end();

        return endState || failState;
    }

    public ShapeRenderer debugRender(){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 0.0f);

        shapeRenderer.polygon(playerTest.getCollisionBox().getTransformedVertices());
        for(Entity entity: entities) shapeRenderer.polygon(entity.getCollisionBox().getTransformedVertices());
        for(Entity entity: playerTest.getProjectiles())shapeRenderer.polygon(entity.getCollisionBox().getTransformedVertices());
        shapeRenderer.polygon(bounds.getTransformedVertices());

        shapeRenderer.end();
        return shapeRenderer;
    }

    private void checkCollisions(){

        for(Entity entity: entities){
            for(Entity entity1: entities){
                if(!entity.equals(entity1)) CollisionHandler.isCollidingEntity(entity, entity1);
            }
        }

        for(Entity e: entities){
            for(Entity e1: playerTest.getProjectiles()){
                CollisionHandler.isCollidingEntity(e,e1);
            }
            CollisionHandler.isCollidingEntity(playerTest, e);
        }

        CollisionHandler.isCollidingLevel(playerTest, bounds);
    }

    private void aliveEntities(){
        ArrayList<Entity> deadEntities = new ArrayList<>();

        for(Entity entity: entities) {
            if (!entity.alive()) {
                System.out.println("Entidad: " + entities.indexOf(entity) + "murio");
                deadEntities.add(entity);
            }
        }

        for(Entity e: deadEntities){
            entities.remove(e);
            e.revive();
            e.preSerialize();
        }

        if(!playerTest.alive()) failCondition();
    }

    public void cleanUp(){batch.dispose();}


    /**
     * Checks if the criteria for failing a level has been met and
     * changes the failState variable
     */
    private void failCondition() {
        failState = true;
    }

    public void postDeSerialize(){
        background = new Texture(backgroundPath);
        entities = new ArrayList<>();
        for(int i = 0; i < entitiesPositions.size(); i++) {
            entities.add(new Entity("playerTest.png", new Polygon(new float[]{0,0,0,40,50,20}), null));
            entities.get(i).setPos(entitiesPositions.get(i));
        }
        batch = new SpriteBatch();
        playerTest = new PlayerEntity(startPoint);
        shapeRenderer = new ShapeRenderer();
    }

    public void preSerialize(){
        background.dispose();
        background = null;
        for(Entity e: entities){
            e.revive();
            e.preSerialize();
        }
        entities.clear();
        entities = null;
        batch = null;
        playerTest.preSerialize();
        playerTest = null;
        shapeRenderer = null;
    }

    public PlayerEntity getPlayerTest(){
        return playerTest;
    }

    public SpriteBatch getBatch(){
        return batch;
    }

    public ShapeRenderer getDebugRenderer(){
        return shapeRenderer;
    }
}
