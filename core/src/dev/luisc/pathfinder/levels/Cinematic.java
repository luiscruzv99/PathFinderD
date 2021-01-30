package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Cinematic implements RenderClass {

    //Composed of scenes
    Menu m;
    Scene1 scn1;
    Scene2 scn2;
    Scene3 scn3;
    Scene4 scn4;
    Scene5 scn5;
    SpriteBatch batch;
    RenderClass currentRender;
    //Game's running at 60 fps, won't change that
    //TODO: FadeOut animation at the end of a scene
    //TODO: Better timing for scene durations, specially the last one
    
    public Cinematic (){
        batch = new SpriteBatch();
        m=new Menu();
        scn5 = new Scene5(m,540,batch);
        scn4 = new Scene4(scn5,480,batch);
        scn3 = new Scene3(scn4,280,batch);
        scn2 = new Scene2(scn3,280,batch);
        scn1 = new Scene1(scn2, 220, batch);
        currentRender = scn1;
    }

    @Override
    public RenderClass render(OrthographicCamera c) {
        return currentRender.render(c);
    }
}
