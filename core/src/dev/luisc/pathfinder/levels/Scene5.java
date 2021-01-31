package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import dev.luisc.pathfinder.entities.Entity;
import dev.luisc.pathfinder.entities.MovingEntity;

import java.net.Inet4Address;

public class Scene5 implements RenderClass{

        private Texture background;
        private Texture textBox;
        private RenderClass[] renderCurrent;
        private int frames; //Duration of the scene
        private int currentFrame; //Actual frame of the scene
        private SpriteBatch batch; //There will only be one SpriteBatch for all scenes
        private MovingEntity cam;
        private Entity obj1;
        private MovingEntity obj2;
        private MovingEntity obj3;
        private BitmapFont font;
        private String[] messages;

        public Scene5 (RenderClass next, int duration, SpriteBatch batch){
            this.renderCurrent = new RenderClass[]{this, next};
            this.frames = duration;
            this.currentFrame = 0;
            this.batch = batch;
            background = new Texture("cinematic/scn_5_bg.png");
            textBox = new Texture("cinematic/textBox.png");
            cam = new MovingEntity(null,
                    new Polygon(new float[]{0,0,0,1,1,1,1,0}),
                    new Vector2(640,360),new Vector2(0,0));
            obj1 = new Entity("cinematic/scn_5_obj_1.png",
                    new Polygon(new float[]{0,0,0,1,1,1,1,0}), new Vector2(0,0));
            obj2 = new MovingEntity("cinematic/scn_5_obj_2.png",
                    new Polygon(new float[]{0,0,0,1,1,1,1,0}),
                    new Vector2(-1500,0), new Vector2(1000, 0));
            obj3 = new MovingEntity("PD_Player_Ship.png",
                    new Polygon(new float[]{0,0,0,1,1,1,1,0}),
                    new Vector2(-20000, 330), new Vector2(5000,0));
            font = new BitmapFont(Gdx.files.internal("fonts/testFont.fnt"), Gdx.files.internal("fonts/testFont.png"), false);
            messages = new String[]{"Ten years later, only the most skilled pilots guide the\nrest of humanity through these hazardous zones.\nThey are vital to humanity.",
                    "You are one of them...a PathFinder."};
        }

        @Override
        public RenderClass render(OrthographicCamera c) {
            c.position.set(cam.getPos(), c.position.z);
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
            batch.draw(obj2.getSprite(), obj2.getPos().x, obj2.getPos().y);
            batch.draw(obj3.getSprite(), obj3.getPos().x, obj3.getPos().y,0,0,100,80,1,1,0);
            batch.draw(obj1.getSprite(),0,0);
            batch.draw(textBox, cam.getPos().x-560,cam.getPos().y-350,1120,180);
            font.draw(batch, messages[currentFrame*9/(frames*5)].substring(0, Integer.min((currentFrame-((9*currentFrame)/(5*frames))*((5*frames)/9))/2, messages[currentFrame*9/(frames*5)].length())),
                    cam.getPos().x-535, cam.getPos().y-190);

            batch.end();
            currentFrame++;
            cam.setPos(new Vector2(Float.max(cam.getPos().x, obj3.getPos().x), cam.getPos().y));
            obj2.move();
            obj3.move();
            return renderCurrent[currentFrame/frames];
        }
}
