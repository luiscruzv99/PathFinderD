package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import dev.luisc.pathfinder.collisions.CollisionHandler;
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
    private SpriteBatch batch; //Image renderer

    private ShapeRenderer shapeRenderer; //CollisionBox renderer(for debug)

    private ArrayList<Entity> dumbEntities;
    private PlayerEntity playerTest;

    private ArrayList<Vector2> dumbEntitiesPositions;

    private boolean endState; //Indicator whether the level has been completed
    private boolean failState; //Indicator whether the player has failed the level

    public static final float TICK_TIME = 0.005f; // Interval between ticks (Seconds)
    private Timer.Task t; //Tick system
    private BitmapFont font; //UI of the ship (speed and position for now (debug))

    /**
     * Populates the level with the information
     */
    public Level(ArrayList<Vector2> positions, Vector2 startPoint, Polygon bounds, String bgPath) {

        this.dumbEntitiesPositions = positions;
        this.startPoint = startPoint;
        this.bounds = bounds;
        this.backgroundPath = bgPath;
        this.dumbEntities = null;

    }

    /**
     * Renders the action that happens in the level, in order to offload
     * code from the main render (Maybe not a good idea...)
     * @return whether the level has been completed, thus ending the render
     */
    public boolean render() {

        batch.begin();
        batch.draw(background, 0,0);
        for(Entity entity: dumbEntities){
            batch.draw(entity.getSprite(), entity.getPos().x, entity.getPos().y,0,0,
                    entity.getSprite().getWidth(), entity.getSprite().getHeight(),1,1,
                    entity.getCollisionBox().getRotation());
        }

        for(Entity entity: playerTest.getProjectiles()){
            batch.draw(entity.getSprite(), entity.getPos().x, entity.getPos().y,0,0,
                    entity.getSprite().getWidth(), entity.getSprite().getHeight(),1,1,
                    entity.getCollisionBox().getRotation());
        }

        batch.draw(playerTest.getSprite(), playerTest.getPos().x, playerTest.getPos().y,
                30,20,50,40,1,1,playerTest.getRotation());

        font.draw(batch, Float.toString(playerTest.getSpeedComponent()),playerTest.getPos().x+50, playerTest.getPos().y+50);
        font.draw(batch, Float.toString(playerTest.getPos().x)+", "+Float.toString(playerTest.getPos().y), playerTest.getPos().x-100, playerTest.getPos().y+50);
        batch.end();

        return endState || failState;
    }

    public ShapeRenderer debugRender(){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 0.0f);

        shapeRenderer.polygon(playerTest.getCollisionBox().getTransformedVertices());
        for(Entity entity: dumbEntities) shapeRenderer.polygon(entity.getCollisionBox().getTransformedVertices());
        for(Entity entity: playerTest.getProjectiles())shapeRenderer.polygon(entity.getCollisionBox().getTransformedVertices());
        shapeRenderer.polygon(bounds.getTransformedVertices());

        shapeRenderer.end();
        return shapeRenderer;
    }

    private void checkCollisions(){

        for(Entity entity: dumbEntities){
            for(Entity entity1: dumbEntities){
                if(!entity.equals(entity1)) CollisionHandler.isCollidingEntity(entity, entity1);
            }
        }

        for(Entity e: dumbEntities){
            for(Entity e1: playerTest.getProjectiles()){
                CollisionHandler.isCollidingEntity(e,e1);
                CollisionHandler.isCollidingLevel(e1, bounds);
            }
            CollisionHandler.isCollidingEntity(playerTest, e);
        }

        CollisionHandler.isCollidingLevel(playerTest, bounds);

    }

    private void aliveEntities(){
        ArrayList<Entity> deadEntities = new ArrayList<>();

        for(Entity entity: dumbEntities) {
            if (!entity.alive()) {
                System.out.println("Entidad: " + dumbEntities.indexOf(entity) + "murio");
                deadEntities.add(entity);
            }
        }

        for(Entity e: deadEntities){
            dumbEntities.remove(e);
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
        dumbEntities = new ArrayList<>();
        for(int i = 0; i < dumbEntitiesPositions.size(); i++) {
            dumbEntities.add(new Entity("playerTest.png", new Polygon(new float[]{0,0,0,40,50,20}), null));
            dumbEntities.get(i).setPos(dumbEntitiesPositions.get(i));
        }
        batch = new SpriteBatch();
        playerTest = new PlayerEntity(startPoint);
        shapeRenderer = new ShapeRenderer();

        font = new BitmapFont();
        font.setColor(0,0,0,255);
        /*
        ------TICK SYSTEM------
        ------100 ticks/s------
         */
        t = new Timer.Task() {
            @Override
            public void run() {
                moveAndCollide();
            }
        };
        Timer.schedule(t,TICK_TIME,TICK_TIME);
    }

    public void preSerialize(){
        background.dispose();
        background = null;
        for(Entity e: dumbEntities){
            e.revive();
            e.preSerialize();
        }
        dumbEntities.clear();
        dumbEntities = null;
        batch = null;
        playerTest.preSerialize();
        playerTest = null;
        shapeRenderer = null;
        t.cancel();

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

    private void moveAndCollide(){

        for(Entity entity: dumbEntities){
            entity.move();
        }
        playerTest.move();

        for(Entity entity: playerTest.getProjectiles()){
            entity.move();
        }
        checkCollisions();

        playerTest.deSpawnProjectiles();
        aliveEntities();

    }
}
