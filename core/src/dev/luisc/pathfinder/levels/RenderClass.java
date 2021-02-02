package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Interface that defines a renderable object
 */
public interface RenderClass {

   /**
    * Render function of the renderable object
    * @param c
    * @return
    */
   RenderClass render(OrthographicCamera c);
}
