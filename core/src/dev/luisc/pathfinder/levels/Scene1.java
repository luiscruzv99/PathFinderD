package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Scene1 implements RenderClass {

    private Texture background;
    private RenderClass[] renderCurrent;
    private int frames; //Duration of the scene
    private int currentFrame; //Actual frame of the scene
    private SpriteBatch batch; //There will only be one SpriteBatch for all scenes

    public Scene1 (RenderClass next, int duration, SpriteBatch batch){
        this.renderCurrent = new RenderClass[]{this, next};
        this.frames = duration;
        this.currentFrame = 0;
        this.batch = batch;
        background = new Texture("cinematic/bg_scn_1.png");
    }




    @Override
    public RenderClass render(OrthographicCamera c) {
        //In this scene I dont move the camera
        batch.setColor(1f,1f,1f, currentFrame/20.0f);
        batch.begin();
        batch.draw(background,0,0);
        batch.end();
        currentFrame++;
        return renderCurrent[currentFrame/frames];
    }
}
