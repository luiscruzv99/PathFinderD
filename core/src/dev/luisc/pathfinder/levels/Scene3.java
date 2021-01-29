package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.entities.Entity;

public class Scene3 implements RenderClass{

    private Texture background;
    private Entity obj1;
    private Entity obj2;
    private RenderClass[] renderCurrent;
    private int frames; //Duration of the scene
    private int currentFrame; //Actual frame of the scene
    private SpriteBatch batch; //There will only be one SpriteBatch for all scenes

    public Scene3 (RenderClass next, int duration, SpriteBatch batch){
        this.renderCurrent = new RenderClass[]{this, next};
        this.frames = duration;
        this.currentFrame = 0;
        this.batch = batch;
        background = new Texture("cinematic/bg_scn_3.png");
        obj1 = new Entity("cinematic/scn_3_obj_1.png",
                new Polygon(new float[]{0f,0f,0f,1f,1f,1f,1f,0f}),
                new Vector2(670,90));
        obj2 = new Entity("cinematic/scn_3_obj_2.png",
                new Polygon(new float[]{0f,0f,0f,1f,1f,1f,1f,0f}),
                new Vector2(300,90));
    }




    @Override
    public RenderClass render(OrthographicCamera c) {
        //In this scene I dont move the camera
        batch.setColor(1f,1f,1f, currentFrame/20.0f);
        batch.begin();
        batch.draw(background,0,0);
        batch.draw(obj1.getSprite(), obj1.getPos().x,obj1.getPos().y, 0,0,
                337,552,1,1, -(((float)currentFrame/(float)frames)*5f));
        batch.draw(obj2.getSprite(), obj2.getPos().x,obj2.getPos().y, 337,0,
                337,552,1,1, (((float)currentFrame/(float)frames)*5f));
        batch.end();
        currentFrame++;
        return renderCurrent[currentFrame/frames];
    }
}
