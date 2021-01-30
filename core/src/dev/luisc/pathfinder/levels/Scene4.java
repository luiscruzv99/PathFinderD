package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.entities.MovingEntity;

public class Scene4 implements RenderClass {
    private Texture background;
    private RenderClass[] renderCurrent;
    private int frames; //Duration of the scene
    private int currentFrame; //Actual frame of the scene
    private SpriteBatch batch; //There will only be one SpriteBatch for all scenes
    private MovingEntity cam;

    public Scene4 (RenderClass next, int duration, SpriteBatch batch){
        this.renderCurrent = new RenderClass[]{this, next};
        this.frames = duration;
        this.currentFrame = 0;
        this.batch = batch;
        background = new Texture("cinematic/scn_4_bg.png");
        cam = new MovingEntity(null,new Polygon(new float[]{0,0,0,1,1,1,1,0}),new Vector2(640,360),new Vector2(0,300));
    }




    @Override
    public RenderClass render(OrthographicCamera c) {
        //In this scene I dont move the camera
        c.position.set(cam.getPos(),c.position.z);
        c.zoom = 1;
        c.update();
        batch.setProjectionMatrix(c.combined);
        batch.setColor(1f,1f,1f, currentFrame/20.0f);
        batch.begin();
        batch.draw(background,0,0);
        batch.end();
        currentFrame++;
        cam.move();
        return renderCurrent[currentFrame/frames];
    }
}
