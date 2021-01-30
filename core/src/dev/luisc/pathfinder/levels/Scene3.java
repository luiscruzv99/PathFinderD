package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.entities.Entity;

public class Scene3 implements RenderClass{

    private Texture background;
    private Texture textBox;
    private Entity obj1;
    private Entity obj2;
    private RenderClass[] renderCurrent;
    private int frames; //Duration of the scene
    private int currentFrame; //Actual frame of the scene
    private SpriteBatch batch; //There will only be one SpriteBatch for all scenes
    private BitmapFont font;
    private String message;


    public Scene3 (RenderClass next, int duration, SpriteBatch batch){
        this.renderCurrent = new RenderClass[]{this, next};
        this.frames = duration;
        this.currentFrame = 0;
        this.batch = batch;
        background = new Texture("cinematic/bg_scn_3.png");
        textBox = new Texture("cinematic/textBox.png");
        obj1 = new Entity("cinematic/scn_3_obj_1.png",
                new Polygon(new float[]{0f,0f,0f,1f,1f,1f,1f,0f}),
                new Vector2(670,90));
        obj2 = new Entity("cinematic/scn_3_obj_2.png",
                new Polygon(new float[]{0f,0f,0f,1f,1f,1f,1f,0f}),
                new Vector2(300,90));
        font = new BitmapFont(Gdx.files.internal("fonts/testFont.fnt"), Gdx.files.internal("fonts/testFont.png"), false);
        font.setColor(255,255,255,255);
        message = "...as the rest of humanity was left in a crumbling and\ndesolated planet to die.";
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
        batch.draw(textBox, 80,10,1120,180);
        font.draw(batch, message.substring(0,Integer.min(currentFrame/2, message.length())),105, 170);
        batch.end();
        currentFrame++;
        return renderCurrent[currentFrame/frames];
    }
}
