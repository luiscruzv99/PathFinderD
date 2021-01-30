package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import dev.luisc.pathfinder.entities.MovingEntity;
import dev.luisc.pathfinder.entities.ProjectilePool;
import dev.luisc.pathfinder.handlers.CollisionHandler;
import dev.luisc.pathfinder.entities.Entity;
import dev.luisc.pathfinder.entities.PlayerEntity;
import dev.luisc.pathfinder.handlers.InputHandler;

import java.util.ArrayList;

/**
 * Class that contains all the information about a level,
 * including entities, bounds and completion conditions.
 *
 * @author Luis
 * @version 10-11-2020
 */
public class Level implements RenderClass{

    private Polygon bounds; //Bounds of the level
    Texture background; //Background image of the level
    private Vector2 startPoint; //Starting point of the player (May be unnecessary??)
    private String backgroundPath; //Path of the background of the level
    private SpriteBatch batch; //Image renderer
    private static Sound schut;
    private static Sound explosion;

    ShapeRenderer renderer; //CollisionBox renderer(for debug)

    protected ArrayList<Entity> dumbEntities;
    protected ArrayList<MovingEntity> projectiles;
    protected ArrayList<Entity> deadEntities;
    protected PlayerEntity playerTest;

    private ArrayList<Vector2> dumbEntitiesPositions;

    boolean endState; //Indicator whether the level has been completed

    public static final float TICK_TIME = 0.01f; // Interval between ticks (Seconds)
    private Timer.Task t; //Tick system
    BitmapFont font; //UI of the ship (speed and position for now (debug))

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

    public void reset(){


        for(int i = 0; i < deadEntities.size(); i++){
            dumbEntities.add(deadEntities.get(i));
        }
        deadEntities.clear();

        for(int i = 0; i < dumbEntitiesPositions.size(); i++){
            dumbEntities.get(i).setPos(dumbEntitiesPositions.get(i));
            dumbEntities.get(i).revive();
        }

        playerTest.setPos(startPoint.cpy());
        playerTest.reset();
    }

    /**
     * Renders the action that happens in the level, in order to offload
     * code from the main render (Maybe not a good idea...)
     * @return whether the level has been completed, thus ending the render
     */
    @Override
    public RenderClass render(OrthographicCamera c) {

        moveCamera(c);

        c.update();
        batch.setProjectionMatrix(c.combined);
        renderer.setProjectionMatrix(c.combined);

        batch.begin();
        batch.draw(background, 0,0);
        for(Entity entity: dumbEntities){
            batch.draw(entity.getSprite(), entity.getPos().x, entity.getPos().y,0,0,
                    entity.getCollisionBox().getBoundingRectangle().getWidth(),entity.getCollisionBox().getBoundingRectangle().getHeight(),1,1,
                    entity.getCollisionBox().getRotation());
        }

        for(Entity entity: playerTest.getProjectiles()){
            batch.draw(entity.getSprite(), entity.getPos().x, entity.getPos().y,0,0,
                    entity.getSprite().getWidth(), entity.getSprite().getHeight(),1,1,
                    entity.getCollisionBox().getRotation());
        }

        batch.draw(playerTest.getSprite(), playerTest.getPos().x, playerTest.getPos().y,
                30,20,50,40,1,1,playerTest.getRotation());

        font.draw(batch, Integer.toString(Math.round(playerTest.getSpeedComponent())),playerTest.getPos().x+75, playerTest.getPos().y+75);
        batch.end();

        debugRender();

        return this;
    }

    public void moveCamera(OrthographicCamera c){
        c.position.set(playerTest.getPos(), 0);

        if (Math.abs(playerTest.getSpeedComponent()) > playerTest.MAX_SPEED / 4 && c.zoom < 1.2) {
            c.zoom += 1.33 * Gdx.graphics.getDeltaTime();
        } else if (Math.abs(playerTest.getSpeedComponent()) < playerTest.MAX_SPEED / 6 && c.zoom > 0.8) {
            c.zoom -= 0.33 * Gdx.graphics.getDeltaTime();
        }

    }

    public ShapeRenderer debugRender(){
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(0, 0, 0, 0.0f);

        renderer.polygon(playerTest.getCollisionBox().getTransformedVertices());
        for(Entity entity: dumbEntities) renderer.polygon(entity.getCollisionBox().getTransformedVertices());
        for(Entity entity: playerTest.getProjectiles()) renderer.polygon(entity.getCollisionBox().getTransformedVertices());
        renderer.polygon(bounds.getTransformedVertices());

        renderer.end();
        return renderer;
    }

    protected void checkCollisions(){
        

        for(Entity e: dumbEntities){
            for(Entity p: playerTest.getProjectiles()){
                CollisionHandler.isCollidingEntity(e,p);
                CollisionHandler.isCollidingLevel(p, bounds);
            }
            CollisionHandler.isCollidingEntity(playerTest, e);
        }

        CollisionHandler.isCollidingLevel(playerTest, bounds);

    }

    protected void aliveEntities(){


        for(Entity entity: dumbEntities) 
            if (!entity.alive()) {
                explosion.play();
                deadEntities.add(entity);
            }

        for(Entity e: deadEntities)
            dumbEntities.remove(e);


        if(!playerTest.alive()) failCondition();
    }

    public void cleanUp(){batch.dispose();}

    /**
     * Checks if the criteria for failing a level has been met and
     * changes the failState variable
     */
    private void failCondition() {
        reset();
    }

    public void postDeSerialize(){

        dumbEntities = new ArrayList<>();
        for(int i = 0; i < dumbEntitiesPositions.size(); i++) {
            int v = (int) (Math.random()*2)+2;
            dumbEntities.add(new Entity("Asteroid_"+v+".png", new Polygon(new float[]{0,20,0,130,20,150,120,150,140,130,140,20,120,0,20,0}), null));
            dumbEntities.get(i).setPos(dumbEntitiesPositions.get(i));
        }
        background = new Texture(backgroundPath);

        batch = new SpriteBatch();
        playerTest = new PlayerEntity(startPoint.cpy());
        playerTest.revive();
        playerTest.postDeSerialize();
        renderer = new ShapeRenderer();

        this.schut = Gdx.audio.newSound(Gdx.files.internal("sounds/schut.mp3"));
        this.explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.mp3"));

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

        deadEntities = new ArrayList<>();
        projectiles = new ArrayList<>();

        InputHandler.getInstance().setLevel(this);
        InputHandler.getInstance().setPlayer(playerTest);
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
        renderer = null;
        t.cancel();
        t = null;
        this.schut.dispose();
        this.explosion.dispose();

    }

    public PlayerEntity getPlayerTest(){
        return playerTest;
    }

    public SpriteBatch getBatch(){
        return batch;
    }

    public ShapeRenderer getDebugRenderer(){
        return renderer;
    }

    protected void moveAndCollide(){

        this.listenInputs();

        if(playerTest != null) {
            for (Entity entity : dumbEntities) {
                entity.move();
            }
            playerTest.move();

            for (Entity entity : playerTest.getProjectiles()) {
                entity.move();
            }
            checkCollisions();

            playerTest.deSpawnProjectiles();
            aliveEntities();
        }
    }

    protected void listenInputs(){
        InputHandler.listenInputs();
    }
    public static void playSchut(){
        schut.play();
    }

    public Polygon getBounds() {
        return bounds;
    }
}
