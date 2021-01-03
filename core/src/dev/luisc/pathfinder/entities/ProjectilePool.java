package dev.luisc.pathfinder.entities;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.util.Stack;

public class ProjectilePool {

    static final int PROJECTILE_NUMBER = 100;
    Stack<MovingEntity> projectiles;
    static ProjectilePool INSTANCE = null;

    private ProjectilePool(){
        projectiles = new Stack<>();
        refill();
    }

    public static ProjectilePool getInstance(){
        if(INSTANCE == null) INSTANCE = new ProjectilePool();
        return INSTANCE;
    }

    public Entity lendAsteroid(){
        if(projectiles.isEmpty()){
            System.out.println("POOL VACIA, RELLENANDOLA");
            refill();
        }
        return projectiles.pop();
    }

    public void receiveAsteroid(MovingEntity asteroid){
        //asteroid.setPos(null);
        if(projectiles.size() < PROJECTILE_NUMBER){
            asteroid.setVel(new Vector2(0,0));
            asteroid.setPos(new Vector2(0,0));
            projectiles.add(asteroid);
        }
        else {
            System.out.println("POOL LLENA, BORRANDO ENTIDAD");
            asteroid.preSerialize(); // """DELETE""" the object if stack is full
        }
    }

    private void refill(){
        for(int i = 0; i < PROJECTILE_NUMBER; i++){
            projectiles.add( new MovingEntity("Projectile.png", new Polygon(new float[]{4,10,4,0,0,0,0,10}), null, null));

        }
    }
}
