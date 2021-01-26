package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.AI.FriendlySwarm;
import dev.luisc.pathfinder.handlers.CollisionHandler;
import dev.luisc.pathfinder.entities.AiEntity;
import dev.luisc.pathfinder.entities.Entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class NavigationLevel extends Level{

    private FriendlySwarm allySwarm;
    private Queue<Entity> beacons;
    private Entity nextBeacon;

    private boolean phaseChanged = false;

    private ArrayList<Vector2> allyPositions;
    private ArrayList<AiEntity> allies;
    private ArrayList<AiEntity> deadAllies;

    private int timer = 0;
    private int maxBeacons = 10;


    Entity goal;

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

    @Override
    public void reset(){

        super.reset();

        //Reset allies
        for(AiEntity a: deadAllies)
            allies.add(a);

        deadAllies.clear();

        for(int i = 0; i < allyPositions.size(); i++) {
           allies.get(i).setPos(allyPositions.get(i));
           allies.get(i).reset();
           allies.get(i).revive();
        }

        allySwarm = new FriendlySwarm(allies);

        allySwarm.setObjective(goal.getPos());
        //Reset beacons
        nextBeacon = goal;

        beacons.clear();
    }


    @Override
    public RenderClass render(OrthographicCamera c){

        super.render(c);
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

        batch.end();
        phaseChanged = false;

        return this;
    }

    @Override
    public void moveCamera(OrthographicCamera c){

        if(phaseChanged) {
            c.position.set(getBestAlly().getPos(), 0);
        }else{
            c.position.set(playerTest.getPos(), 0);

            if (Math.abs(playerTest.getSpeedComponent()) > playerTest.MAX_SPEED / 4 && c.zoom < 1.2) {
                c.zoom += 1.33 * Gdx.graphics.getDeltaTime();
            } else if (Math.abs(playerTest.getSpeedComponent()) < playerTest.MAX_SPEED / 6 && c.zoom > 0.8) {
                c.zoom -= 0.33 * Gdx.graphics.getDeltaTime();
            }
        }

    }

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
    @Override
    public void preSerialize(){
        super.preSerialize();
        goal.preSerialize();
        for (Entity e: beacons) {
            e.preSerialize();
        }
        beacons.clear();
        beacons = null;
        allySwarm = null;
        for(AiEntity e: allies){
            e.revive();
            e.preSerialize();
        }
        allies.clear();
        allies = null;
    }

    @Override
    public void postDeSerialize(){
        super.postDeSerialize();
        allies = new ArrayList<>();
        for(int  i = 0; i < allyPositions.size(); i++){
            allies.add(new AiEntity("Ai_ship.png", new Polygon(new float[]{0,20,50,0,50,40}), allyPositions.get(i), new Vector2((float)Math.random()*100, (float)Math.random()*100)));
        }
        beacons = new LinkedList<>() ;
        nextBeacon = null;
        allySwarm = new FriendlySwarm(allies);
        goal.postDeSerialize();
        allySwarm.setObjective(goal.getPos());
        renderer = new ShapeRenderer();
        deadAllies = new ArrayList<>();

    }

    public void placeBeacon() {

        if (timer > 100 && getPlayerTest().getBeaconsPlaced() < maxBeacons) {
            beacons.offer(new Entity("Beacon.png", new Polygon(new float[]{10, 5, 10, 35, 45, 35, 45, 5}), new Vector2(getPlayerTest().getPos())));
            getPlayerTest().placeBeacon();
            timer = 0;
        }

        if(nextBeacon == null){
            nextBeacon = beacons.poll();
            allySwarm.setObjective(nextBeacon.getPos());
        }
    }

    @Override
    protected void checkCollisions(){
        super.checkCollisions();
        if(phaseChanged && getPlayerTest() != null){
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

        if(playerTest.getPos().dst(goal.getPos()) < 100){
            phaseChanged = true;
            playerTest.fullStop();

        }
    }

    @Override
    protected void moveAndCollide(){
        super.moveAndCollide();
        if(phaseChanged && getPlayerTest() != null){
            allySwarm.move();
        }
        timer++;
    }

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

    public boolean isPhaseChanged(){
        return phaseChanged;
    }

    public Entity getBestAlly(){
        return allySwarm.getBestParticle();
    }
}
