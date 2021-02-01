package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Scene1 implements RenderClass {

    private Texture background;
    private RenderClass[] renderCurrent;
    private int frames; //Duration of the scene
    private int currentFrame; //Actual frame of the scene
    private SpriteBatch batch; //There will only be one SpriteBatch for all scenes
    private Texture textBox;
    private BitmapFont font;
    private String message;

    public Scene1 (RenderClass next, int duration, SpriteBatch batch){
        this.renderCurrent = new RenderClass[]{this, next};
        this.frames = duration;
        this.currentFrame = 0;
        this.batch = batch;
        background = new Texture("cinematic/bg_scn_1.png");
        textBox = new Texture("cinematic/textBox.png");
        font = new BitmapFont(Gdx.files.internal("fonts/testFont.fnt"), Gdx.files.internal("fonts/testFont.png"), false);
        message = "In the year 2042, humanity had wrung out every available\nresource from Earth.";

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
        font.draw(batch, message.substring(0,Integer.min(currentFrame/2,message.length())) ,105, 170);
        batch.end();
        currentFrame++;
        return renderCurrent[currentFrame/frames];
    }
}
