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

public class Scene4 implements RenderClass {

    private Texture background;
    private Texture textBox;
    private RenderClass[] renderCurrent;
    private int frames; //Duration of the scene
    private int currentFrame; //Actual frame of the scene
    private SpriteBatch batch; //There will only be one SpriteBatch for all scenes
    private MovingEntity cam;
    private BitmapFont font;
    private String message;

    public Scene4 (RenderClass next, int duration, SpriteBatch batch){
        this.renderCurrent = new RenderClass[]{this, next};
        this.frames = duration;
        this.currentFrame = 0;
        this.batch = batch;
        background = new Texture("cinematic/scn_4_bg.png");
        textBox = new Texture("cinematic/textBox.png");
        cam = new MovingEntity(null,new Polygon(new float[]{0,0,0,1,1,1,1,0}),
                new Vector2(640,360),new Vector2(0,300));
        font = new BitmapFont(Gdx.files.internal("fonts/testFont.fnt"), Gdx.files.internal("fonts/testFont.png"), false);
        message = "However, those who escaped did it with premature\ntechnology, " +
                "incapable of navigating the asteroid clusters\nthat plagued the outsides of the solar system.";
    }

    @Override
    public RenderClass render(OrthographicCamera c) {

        if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))
            return renderCurrent[1];

        //In this scene I dont move the camera
        c.position.set(cam.getPos(),c.position.z);
        c.zoom = 1;
        c.update();
        batch.setProjectionMatrix(c.combined);
        if(currentFrame < frames/2){
            batch.setColor(1f,1f,1f, currentFrame/20.0f);
            font.setColor(1f,1f,1f,currentFrame/20.0f);
        }else {
            batch.setColor(1f,1f,1f, (frames-currentFrame)/20.0f);
            font.setColor(1f,1f,1f,(frames-currentFrame)/20.0f);

        }
        batch.begin();
        batch.draw(background,0,0);
        batch.draw(textBox, cam.getPos().x-560,cam.getPos().y-350,1120,180);
        font.draw(batch, message.substring(0,Integer.min(currentFrame/2, message.length())) ,cam.getPos().x-535, cam.getPos().y-190);
        batch.end();
        currentFrame++;
        cam.move();
        return renderCurrent[currentFrame/frames];
    }
}
