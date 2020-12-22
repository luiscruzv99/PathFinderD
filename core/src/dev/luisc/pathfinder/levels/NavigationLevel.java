package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.AI.FriendlySwarm;
import dev.luisc.pathfinder.collisions.CollisionEvent;
import dev.luisc.pathfinder.collisions.CollisionHandler;
import dev.luisc.pathfinder.entities.AiEntity;
import dev.luisc.pathfinder.entities.Entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class NavigationLevel extends Level{

    private FriendlySwarm allySwarm;
    private Queue<Entity> beacons;
    private Entity nextBeacon;

    private boolean phaseChanged = false;

    private ArrayList<Vector2> allyPositions;
    private ArrayList<AiEntity> allies;

    private int timer = 0;
    private int maxBeacons = 10;

    private CollisionEvent aiBeaconCollision;
    private CollisionEvent aiCollision;
    private CollisionEvent beaconCollision;


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
        this.goal = new Entity(null, new Polygon(new float[]{0,0,0,goalSize,goalSize,goalSize,goalSize,0}),goalPos);
    }

    @Override
    public boolean render(){

        SpriteBatch batch = getBatch();
        batch.begin();
        batch.draw(background, 0,0);
        for(Entity entity: dumbEntities){
            batch.draw(entity.getSprite(), entity.getPos().x, entity.getPos().y,0,0,
                    entity.getSprite().getWidth(), entity.getSprite().getHeight(),1,1,
                    entity.getCollisionBox().getRotation());
        }

        for(AiEntity e: allies){
            batch.draw(e.getSprite(), e.getPos().x, e.getPos().y,0,0,
                    e.getSprite().getWidth(), e.getSprite().getHeight(),1,1,
                    e.getCollisionBox().getRotation());
        }

        if(!beacons.isEmpty()){
            for(Entity e: beacons){
                batch.draw(e.getSprite(), e.getPos().x, e.getPos().y,0,0,
                        e.getSprite().getWidth(), e.getSprite().getHeight(),1,1,
                        e.getCollisionBox().getRotation());
            }
        }
        if(!(nextBeacon == null)){
            batch.draw(nextBeacon.getSprite(), nextBeacon.getPos().x, nextBeacon.getPos().y,0,0,
                    nextBeacon.getSprite().getWidth(), nextBeacon.getSprite().getHeight(),1,1,
                    nextBeacon.getCollisionBox().getRotation());
        }

        for(Entity entity: playerTest.getProjectiles()){
            batch.draw(entity.getSprite(), entity.getPos().x, entity.getPos().y,0,0,
                    entity.getSprite().getWidth(), entity.getSprite().getHeight(),1,1,
                    entity.getCollisionBox().getRotation());
        }

        batch.draw(playerTest.getSprite(), playerTest.getPos().x, playerTest.getPos().y,
                30,20,50,40,1,1,playerTest.getRotation());

        font.draw(batch, Integer.toString(Math.round(playerTest.getSpeedComponent())),playerTest.getPos().x+75, playerTest.getPos().y+75);
        font.draw(batch, Integer.toString(playerTest.getBeaconsPlaced()), playerTest.getPos().x-20, playerTest.getPos().y+75);



        batch.end();
        phaseChanged = false;
        return endState || failState;
    }


    @Override
    public ShapeRenderer debugRender(){
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(0, 0, 0, 0.0f);

        renderer.polygon(playerTest.getCollisionBox().getTransformedVertices());
        for(Entity entity: dumbEntities) renderer.polygon(entity.getCollisionBox().getTransformedVertices());
        for(AiEntity e: allies) renderer.polygon(e.getCollisionBox().getTransformedVertices());
        for(Entity entity: playerTest.getProjectiles())renderer.polygon(entity.getCollisionBox().getTransformedVertices());
        if(beacons!= null) {
            for (Entity e : beacons)
                renderer.polygon(e.getCollisionBox().getTransformedVertices());
        }
        if(nextBeacon != null) {
            renderer.polygon(nextBeacon.getCollisionBox().getTransformedVertices());
        }
        renderer.polygon(goal.getCollisionBox().getTransformedVertices());
        renderer.polygon(getBounds().getTransformedVertices());

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
        aiCollision = new CollisionEvent() {
            @Override
            public void onCollision(Entity e) {
                e.setHitPoints(0);
            }
        };
        aiBeaconCollision = new CollisionEvent() {
            @Override
            public void onCollision(Entity e) {

            }
        };
        beaconCollision = new CollisionEvent() {
            @Override
            public void onCollision(Entity e) {
                e.setHitPoints(0);
            }
        };
        allies = new ArrayList<>();
        for(int  i = 0; i < allyPositions.size(); i++){
            allies.add(new AiEntity("AIShip.png", new Polygon(new float[]{0,0,0,40,50,20}), allyPositions.get(i), new Vector2((float)Math.random()*100, (float)Math.random()*100)));
        }
        beacons = new LinkedList<>() ;
        nextBeacon = null;
        allySwarm = new FriendlySwarm(allies);
        renderer = new ShapeRenderer();

    }

    public void placeBeacon() {

        if (timer > 100 && getPlayerTest().getBeaconsPlaced() < maxBeacons) {
            beacons.offer(new Entity("Beacon.png", new Polygon(new float[]{10, 5, 10, 35, 45, 35, 45, 5}), new Vector2(getPlayerTest().getPos())));
            getPlayerTest().placeBeacon();
            timer = 0;
        }
    }

    @Override
    protected void moveAndCollide(){
        if(phaseChanged && getPlayerTest() != null){
            allySwarm.move();
            for(AiEntity e: allies)
                CollisionHandler.isCollidingLevel(e, getBounds());
            for(AiEntity a: allies){
                for(Entity e: dumbEntities){
                    a.setBehaviour(aiCollision);
                    CollisionHandler.isCollidingEntity(a,e);
                }
            }
            if(nextBeacon != null){
                for(AiEntity a: allies){
                    a.setBehaviour(aiBeaconCollision);
                    CollisionHandler.isCollidingEntity(a, nextBeacon);
                }
            }
        }
        timer++;
        if(nextBeacon == null && !beacons.isEmpty()) {
            nextBeacon = beacons.poll();
            nextBeacon.setBehaviour(beaconCollision);
            allySwarm.setObjective(nextBeacon.getPos());
        }
        super.moveAndCollide();
    }

    @Override
    protected void aliveEntities(){
        ArrayList<Entity> deadEntities = new ArrayList<>();

        for(Entity entity: allies) {
            if (!entity.alive()) {
                System.out.println("Entidad: " + allies.indexOf(entity) + "murio");
                deadEntities.add(entity);
            }
        }

        if(nextBeacon != null && !nextBeacon.alive()) {
            nextBeacon.preSerialize();
            nextBeacon = null;
        }
        for(Entity e: deadEntities){
            allies.remove(e);
            allySwarm.getParticles().remove(e);
            e.revive();
            e.preSerialize();
        }

        super.aliveEntities();
    }
}
