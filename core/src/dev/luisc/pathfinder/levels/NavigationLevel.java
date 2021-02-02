package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.AI.FriendlySwarm;
import dev.luisc.pathfinder.handlers.CollisionHandler;
import dev.luisc.pathfinder.entities.AiEntity;
import dev.luisc.pathfinder.entities.Entity;
import dev.luisc.pathfinder.handlers.InputHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Navigation level of the game, in which a player must guide ships to the goal
 */
public class NavigationLevel extends Level{

    private FriendlySwarm allySwarm; //The PSO object that moves the allies
    private Queue<Entity> beacons; //Beacons for the allies to find
    private Entity nextBeacon; //The next beacon the allies must go to

    private boolean phaseChanged = false; //Determines if the allies must be moved

    private ArrayList<Vector2> allyPositions; //Initial position of the allies
    private ArrayList<AiEntity> allies; //The Ai allies
    private ArrayList<AiEntity> deadAllies; //The dead allies

    private int timer = 0; //Timer that controls the cooldown between beacon placements
    private int maxBeacons = 10; //The limit of beacons that can be placed by the player

    Entity goal; //The goal that the allies must reach

    /**
     * Populates the level with the information
     *
     * @param positions
     * @param startPoint
     * @param bounds
     * @param bgPath
     */
    public NavigationLevel(ArrayList<Vector2> positions, Vector2 startPoint, Polygon bounds, String bgPath, ArrayList<Vector2> allyPositions, Vector2 goalPos, int goalSize) {
        super(positions, startPoint, bounds, bgPath);
        this.allyPositions = allyPositions;
        this.goal = new Entity("Beacon.png", new Polygon(new float[]{0,0,0,goalSize,goalSize,goalSize,goalSize,0}),goalPos);
        goal.preSerialize();
    }

    /**
     * Resets all of the level components (allies, beacons...)
     */
    @Override
    public void reset(){

        for(AiEntity a: deadAllies)
            allies.add(a);

        deadAllies.clear();

        for(int i = 0; i < allyPositions.size(); i++) {
           allies.get(i).setPos(allyPositions.get(i).cpy());
           allies.get(i).reset();
           allies.get(i).revive();
        }

        allySwarm = new FriendlySwarm(allies);

        allySwarm.setObjective(goal.getPos());

        nextBeacon = null;

        beacons.clear();

        super.reset();
    }


    /**
     * Renders the level and all of its components, also determines which object to render next (the same level,
     *  the victory menu...)
     * @param c
     * @return
     */
    @Override
    public RenderClass render(OrthographicCamera c){

        RenderClass r = super.render(c);
        SpriteBatch batch = getBatch();

        c.update();
        batch.setProjectionMatrix(c.combined);
        renderer.setProjectionMatrix(c.combined);

        batch.begin();
        for(AiEntity e: allies){
            batch.draw(e.getSprite(), e.getPos().x, e.getPos().y,0,0,
                    50, 50,1,1,
                    e.getCollisionBox().getRotation());
        }

        if(!beacons.isEmpty()){
            for(Entity e: beacons){
                batch.draw(e.getSprite(), e.getPos().x, e.getPos().y,0,0,
                       50,50,1,1,
                        e.getCollisionBox().getRotation());
            }
        }
        if(!(nextBeacon == null)){
            batch.draw(nextBeacon.getSprite(), nextBeacon.getPos().x, nextBeacon.getPos().y,0,0,
                   50,50,1,1,
                    nextBeacon.getCollisionBox().getRotation());
        }
        batch.draw(goal.getSprite(),goal.getPos().x, goal.getPos().y, 0, 0, 50, 50, 1,1,0);
        font.draw(batch, Integer.toString(player.getBeaconsPlaced()), player.getPos().x-20, player.getPos().y+75);
        batch.end();
        phaseChanged = false;
        if(endState){
            VictoryMenu m = new VictoryMenu(allies.size());
            return m;
        }
        if(failed) return r;
        return currentRender[endState? 1:0];
    }

    /**
     * Moves the camera in the level, to follow the Ai allies when necessary
     * @param c
     */
    @Override
    public void moveCamera(OrthographicCamera c){

        if(phaseChanged) {
            c.position.set(allySwarm.getBestParticle().getPos(), 0);
        }else{
            c.position.set(player.getPos(), 0);

            if (Math.abs(player.getSpeedComponent()) > player.MAX_SPEED / 4 && c.zoom < 1.2) {
                c.zoom += 1.33 * Gdx.graphics.getDeltaTime();
            } else if (Math.abs(player.getSpeedComponent()) < player.MAX_SPEED / 6 && c.zoom > 0.8) {
                c.zoom -= 0.33 * Gdx.graphics.getDeltaTime();
            }
        }

    }

    /**
     * Debug Renderer that shows the collisions for the level components
     * @return
     */
    @Override
    public ShapeRenderer debugRender(){
        renderer = super.debugRender();
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(0, 0, 0, 0.0f);

        for(AiEntity e: allies) renderer.polygon(e.getCollisionBox().getTransformedVertices());
        if(beacons!= null) {
            for (Entity e : beacons)
                renderer.polygon(e.getCollisionBox().getTransformedVertices());
        }
        if(nextBeacon != null) {
            renderer.polygon(nextBeacon.getCollisionBox().getTransformedVertices());
        }
        renderer.polygon(goal.getCollisionBox().getTransformedVertices());

        renderer.end();
        return renderer;
    }

    /**
     * Disposes of the components before serializing a level
     */
    @Override
    public void preSerialize(){
        super.preSerialize();
        goal.preSerialize();
        for (Entity e: beacons) {
            e.getSprite().getTexture().dispose();
            e = null;
        }
        beacons.clear();
        beacons = null;
        allySwarm.preSerialize();
        allySwarm = null;
        for(AiEntity e: allies){
            e.revive();
            e.getSprite().getTexture().dispose();
            e = null;
        }
        allies.clear();
        allies = null;
    }

    /**
     * Initializes the components after de-serializing the level
     */
    @Override
    public void postDeSerialize(){
        super.postDeSerialize();
        allies = new ArrayList<>();
        for(int  i = 0; i < allyPositions.size(); i++){
            allies.add(new AiEntity("Ai_ship.png", new Polygon(new float[]{0,20,50,0,50,40}), allyPositions.get(i).cpy(), new Vector2((float)Math.random()*500, (float)Math.random()*500)));
        }
        beacons = new LinkedList<>() ;
        nextBeacon = null;
        allySwarm = new FriendlySwarm(allies);
        goal.postDeSerialize();
        allySwarm.setObjective(goal.getPos());
        renderer = new ShapeRenderer();
        deadAllies = new ArrayList<>();

    }

    /**
     * Creates a beacon object in the position of the player
     */
    public void placeBeacon() {

        if (timer > 100 && getPlayer().getBeaconsPlaced() < maxBeacons) {
            beacons.offer(new Entity("Beacon.png", new Polygon(new float[]{10, 5, 10, 35, 45, 35, 45, 5}), new Vector2(getPlayer().getPos())));
            getPlayer().placeBeacon();
            timer = 0;
        }

        if(nextBeacon == null){
            nextBeacon = beacons.poll();
            allySwarm.setObjective(nextBeacon.getPos());
        }
    }

    /**
     * Checks the collisions of the diferent components in the level
     */
    @Override
    protected void checkCollisions(){
        super.checkCollisions();
        if(phaseChanged && getPlayer() != null){
            for(AiEntity e: allies)
                CollisionHandler.isCollidingLevel(e, getBounds());
            for(AiEntity a: allies){
                for(Entity e: dumbEntities){
                    CollisionHandler.isCollidingEntity(a,e);
                }
            }
            if(nextBeacon != null){
                Iterator<AiEntity> i = allies.iterator();
                boolean goal = false;

                while(i.hasNext() && !goal){
                    if(i.next().getPos().dst(nextBeacon.getPos()) < 50){
                        if(nextBeacon.getPos().equals(this.goal.getPos())){
                            endState = true;
                        }
                        goal = true;
                    }
                }
                if(goal){
                    if(!beacons.isEmpty()) {
                        nextBeacon = beacons.poll();
                    }else{
                        nextBeacon = this.goal;
                    }
                    allySwarm.setObjective(nextBeacon.getPos());

                }

            }
        }

        if(player.getPos().dst(goal.getPos()) < 100){
            phaseChanged = true;
            player.fullStop();

        }
    }

    /**
     * Moves the movable components of the level
     */
    @Override
    protected void moveAndCollide(){
        super.moveAndCollide();
        if(phaseChanged && getPlayer() != null){
            allySwarm.move();
            boolean finished = true;
            Iterator<AiEntity> i = allies.iterator();
            while(i.hasNext() && finished){
                if(i.next().getPos().dst(goal.getPos())>100) finished = false;
            }
            endState = finished;
        }
        timer++;
        listenInputs();
    }

    /**
     * Checks the user's inputs
     */
    @Override
    protected void listenInputs(){
        if(!phaseChanged) InputHandler.listenInputs();
    }

    /**
     * Checks the entities that are still alive
     */
    @Override
    protected void aliveEntities(){

        if(allies != null){
        for(AiEntity a: allies) {
            if (!a.alive()) {
                deadAllies.add(a);
            }
        }
        }

        if(nextBeacon != null && !nextBeacon.alive()) {
            nextBeacon.preSerialize();
            nextBeacon = null;
        }
        for(AiEntity e: deadAllies){
            allySwarm.getParticles().remove(e);
            allies.remove(e);
        }

        super.aliveEntities();
    }

}
