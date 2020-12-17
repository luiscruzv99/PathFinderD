package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.AI.FriendlySwarm;
import dev.luisc.pathfinder.collisions.CollisionHandler;
import dev.luisc.pathfinder.entities.AiEntity;
import dev.luisc.pathfinder.entities.Entity;

import java.util.ArrayList;

public class NavigationLevel extends Level{



    private FriendlySwarm allySwarm;
    private ArrayList<Entity> beacons;
    private Entity nextBeacon;

    private boolean phaseChanged = false;

    private ArrayList<Vector2> allyPositions;
    private ArrayList<AiEntity> allies;
    /**
     * Populates the level with the information
     *
     * @param positions
     * @param startPoint
     * @param bounds
     * @param bgPath
     */
    public NavigationLevel(ArrayList<Vector2> positions, Vector2 startPoint, Polygon bounds, String bgPath, ArrayList<Vector2> allyPositions) {
        super(positions, startPoint, bounds, bgPath);
        this.allyPositions = allyPositions;
    }

    @Override
    public boolean render(){
        boolean state = super.render();
        SpriteBatch batch = getBatch();

        batch.begin();
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

        batch.draw(nextBeacon.getSprite(), nextBeacon.getPos().x, nextBeacon.getPos().y,0,0,
                nextBeacon.getSprite().getWidth(), nextBeacon.getSprite().getHeight(),1,1,
                nextBeacon.getCollisionBox().getRotation());
        batch.end();
        phaseChanged = true;
        return state;
    }


    @Override
    public ShapeRenderer debugRender(){
        ShapeRenderer  renderer = super.debugRender();
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(0, 0, 0, 0.0f);
        for(AiEntity e: allies)
            renderer.polygon(e.getCollisionBox().getTransformedVertices());
        renderer.end();
        return renderer;
    }
    @Override
    public void preSerialize(){
        super.preSerialize();
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
            allies.add(new AiEntity("AIShip.png", new Polygon(new float[]{0,0,0,40,50,20}), allyPositions.get(i), new Vector2((float)Math.random()*100, (float)Math.random()*100)));
        }
        beacons = new ArrayList<>();
        nextBeacon = new Entity("Beacon.png", new Polygon(new float[]{10,5,10,35,45,35,45,5}), new Vector2(1500,1500));
        allySwarm = new FriendlySwarm(allies);
        allySwarm.setObjective(nextBeacon.getPos());

    }

    @Override
    protected void moveAndCollide(){
        if(phaseChanged && getPlayerTest() != null){
            allySwarm.move();
            for(AiEntity e: allies)
                CollisionHandler.isCollidingLevel(e, getBounds());
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
        for(Entity e: deadEntities){
            allies.remove(e);
            allySwarm.getParticles().remove(e);
            e.revive();
            e.preSerialize();
        }

        super.aliveEntities();
    }
}
