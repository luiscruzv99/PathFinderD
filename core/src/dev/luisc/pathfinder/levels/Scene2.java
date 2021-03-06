package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.entities.MovingEntity;


/**
 * Scene 5 of the cinematic
 */
public class Scene2 implements RenderClass {

    private Texture background;
    private Texture textBox;
    private MovingEntity obj1;
    private RenderClass[] renderCurrent;
    private int frames; //Duration of the scene
    private int currentFrame; //Actual frame of the scene
    private SpriteBatch batch; //There will only be one SpriteBatch for all scenes
    private BitmapFont font;
    private String message;


    public Scene2 (RenderClass next, int duration, SpriteBatch batch){
        this.renderCurrent = new RenderClass[]{this, next};
        this.frames = duration;
        this.currentFrame = 0;
        this.batch = batch;
        background = new Texture("cinematic/scn_2_bg.png");
        textBox = new Texture("cinematic/textBox.png");
        obj1 = new MovingEntity("cinematic/scn_2_obj_1.png",
                new Polygon(new float[]{0f,0f,0f,1f,1f,1f,1f,0f}),
                new Vector2(250,250), new Vector2(180,60));
        font = new BitmapFont(Gdx.files.internal("fonts/testFont.fnt"), Gdx.files.internal("fonts/testFont.png"), false);
        message = "In hopes of finding a better future somewhere else,\nthousands of people headed to outer space...";
    }




    @Override
    public RenderClass render(OrthographicCamera c) {

        if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))
            return renderCurrent[1];

        //In this scene I dont move the camera
        if(currentFrame < frames/2){
            batch.setColor(1f,1f,1f, currentFrame/20.0f);
            font.setColor(1f,1f,1f,currentFrame/20.0f);
        }else {
            batch.setColor(1f,1f,1f, (frames-currentFrame)/20.0f);
            font.setColor(1f,1f,1f,(frames-currentFrame)/20.0f);

        }
        batch.begin();
        batch.draw(background,0,0);
        batch.draw(textBox, 80,10,1120,180);
        batch.draw(obj1.getSprite(), obj1.getPos().x,obj1.getPos().y, 0,0,
                300,100,0.5f+(currentFrame/(4f*frames)),0.5f+(currentFrame/(4f*frames)), obj1.getPos().angle());
        font.draw(batch, message.substring(0,Integer.min(currentFrame/2, message.length())),105, 170);
        batch.end();
        obj1.move();
        currentFrame++;
        return renderCurrent[currentFrame/frames];
    }
}
