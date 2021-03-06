package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import dev.luisc.pathfinder.entities.MovingEntity;
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
    private static Sound schut; //Sfx for the projectile
    private static Sound explosion; //Sfx for the death of an Entity

    ShapeRenderer renderer; //CollisionBox renderer(for debug)

    protected ArrayList<Entity> dumbEntities; //Static entities of the level
    protected ArrayList<MovingEntity> projectiles; //Projectiles launched by the player
    protected ArrayList<Entity> deadEntities; //Dead static entities
    protected PlayerEntity player;

    private ArrayList<Vector2> dumbEntitiesPositions; //Positions of the static Entities

    boolean endState; //Indicator whether the level has been completed
    boolean failed = false; //Indicator whether the player has failed

    public static final float TICK_TIME = 0.01f; // Interval between ticks (Seconds)
    private Timer.Task t; //Tick system
    BitmapFont font; //UI of the ship (speed and beacons for now (debug))
    RenderClass[] currentRender;

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
     * Resets the positions of the components of the level
     */
    public void reset(){

        failed = false;
        endState = false;
        for(int i = 0; i < deadEntities.size(); i++){
            dumbEntities.add(deadEntities.get(i));
        }
        deadEntities.clear();

        for(int i = 0; i < dumbEntitiesPositions.size(); i++){
            dumbEntities.get(i).setPos(dumbEntitiesPositions.get(i));
            dumbEntities.get(i).revive();
        }

        player.setPos(startPoint.cpy());
        player.reset();
    }

    /**
     * Renders the action that happens in the level
     * @return the next object to render
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

        for(Entity entity: player.getProjectiles()){
            batch.draw(entity.getSprite(), entity.getPos().x, entity.getPos().y,0,0,
                    entity.getSprite().getWidth(), entity.getSprite().getHeight(),1,1,
                    entity.getCollisionBox().getRotation());
        }

        batch.draw(player.getSprite(), player.getPos().x, player.getPos().y,
                30,20,50,40,1,1, player.getRotation());

        font.draw(batch, Integer.toString(Math.round(player.getSpeedComponent())), player.getPos().x+75, player.getPos().y+75);
        batch.end();

        //debugRender();
        if(failed) return new FailMenu(this);
        return currentRender[endState? 1:0];
    }

    /**
     * Moves the camera in the level, to follow the Ai allies when necessary
     * @param c
     */
    public void moveCamera(OrthographicCamera c){
        c.position.set(player.getPos(), 0);

        if (Math.abs(player.getSpeedComponent()) > player.MAX_SPEED / 4 && c.zoom < 1.2) {
            c.zoom += 1.33 * Gdx.graphics.getDeltaTime();
        } else if (Math.abs(player.getSpeedComponent()) < player.MAX_SPEED / 6 && c.zoom > 0.8) {
            c.zoom -= 0.33 * Gdx.graphics.getDeltaTime();
        }

    }

    /**
     * Debug Renderer that shows the collisions for the level components
     * @return
     */
    public ShapeRenderer debugRender(){
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(0, 0, 0, 0.0f);

        renderer.polygon(player.getCollisionBox().getTransformedVertices());
        for(Entity entity: dumbEntities) renderer.polygon(entity.getCollisionBox().getTransformedVertices());
        for(Entity entity: player.getProjectiles()) renderer.polygon(entity.getCollisionBox().getTransformedVertices());
        renderer.polygon(bounds.getTransformedVertices());

        renderer.end();
        return renderer;
    }

    /**
     * Checks the collisions of the diferent components in the level
     */
    protected void checkCollisions(){

        for(Entity e: dumbEntities){
            for(Entity p: player.getProjectiles()){
                CollisionHandler.isCollidingEntity(e,p);
                CollisionHandler.isCollidingLevel(p, bounds);
            }
            CollisionHandler.isCollidingEntity(player, e);
        }

        CollisionHandler.isCollidingLevel(player, bounds);

    }

    /**
     * Checks the entities that are still alive
     */
    protected void aliveEntities(){

        for(Entity entity: dumbEntities) 
            if (!entity.alive()) {
                explosion.play();
                deadEntities.add(entity);
            }

        for(Entity e: deadEntities)
            dumbEntities.remove(e);


        if(!player.alive()) failCondition();
    }


    /**
     * Checks if the criteria for failing a level has been met and
     * changes the failState variable
     */
    private void failCondition() {
        failed = true;
    }

    /**
     * Initializes the components after de-serializing the level
     */
    public void postDeSerialize(){

        dumbEntities = new ArrayList<>();
        for(int i = 0; i < dumbEntitiesPositions.size(); i++) {
            int v = (int) (Math.random()*2)+2;
            dumbEntities.add(new Entity("Asteroid_"+v+".png", new Polygon(new float[]{0,20,0,130,20,150,120,150,140,130,140,20,120,0,20,0}), null));
            dumbEntities.get(i).setPos(dumbEntitiesPositions.get(i));
        }
        background = new Texture(backgroundPath);

        batch = new SpriteBatch();
        player = new PlayerEntity(startPoint.cpy());
        player.revive();
        player.postDeSerialize();
        renderer = new ShapeRenderer();

        this.schut = Gdx.audio.newSound(Gdx.files.internal("sounds/schut.mp3"));
        this.explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.mp3"));

        font = new BitmapFont(Gdx.files.internal("fonts/testFont.fnt"), Gdx.files.internal("fonts/testFont.png"), false);
        font.setColor(1,1,1,255);
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
        InputHandler.getInstance().setPlayer(player);

        currentRender = new RenderClass[]{this, new Menu()};
    }

    /**
     * Disposes of the components before serializing a level
     */
    public void preSerialize(){
        background.dispose();
        batch.dispose();
        background = null;
        for(Entity e: dumbEntities){
            e.revive();
            e.getSprite().getTexture().dispose();
            e = null;
        }
        dumbEntities.clear();
        dumbEntities = null;
        player.getSprite().getTexture().dispose();
        player = null;
        renderer.dispose();
        renderer = null;
        t.cancel();
        t = null;
        this.schut.dispose();
        this.explosion.dispose();

    }

    public PlayerEntity getPlayer(){
        return player;
    }

    public SpriteBatch getBatch(){
        return batch;
    }

    /**
     * Moves the movable components of the level
     */
    protected void moveAndCollide(){

        if(player != null) {
            for (Entity entity : dumbEntities) {
                entity.move();
            }
            player.move();

            for (Entity entity : player.getProjectiles()) {
                entity.move();
            }
            checkCollisions();

            player.deSpawnProjectiles();
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
