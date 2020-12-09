package dev.luisc.pathfinder.entities;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class AiEntity extends MovingEntity{
    /**
     * Create the moving entity
     *
     * @param spritePath
     * @param polygon
     * @param pos
     * @param vel
     */
    public AiEntity(String spritePath, Polygon polygon, Vector2 pos, Vector2 vel) {
        super(spritePath, polygon, pos, vel);
    }
}
