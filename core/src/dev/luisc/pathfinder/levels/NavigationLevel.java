package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.AI.FriendlySwarm;
import dev.luisc.pathfinder.entities.Entity;

import java.util.ArrayList;

public class NavigationLevel extends Level{



    private FriendlySwarm allies;
    private ArrayList<Entity> beacons;
    /**
     * Populates the level with the information
     *
     * @param positions
     * @param startPoint
     * @param bounds
     * @param bgPath
     */
    public NavigationLevel(ArrayList<Vector2> positions, Vector2 startPoint, Polygon bounds, String bgPath) {
        super(positions, startPoint, bounds, bgPath);
    }
}
