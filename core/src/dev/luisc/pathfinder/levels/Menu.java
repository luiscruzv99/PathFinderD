package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Menu of the game
 */
public class Menu {

    Texture backgorund; //Background

    Texture b1; //Button 1 (play)

    Texture b2; //Button 2 (exit)

    Texture banner;

    SpriteBatch batch;


    public Menu(){

        banner = new Texture("bannerTest.png");
        //Initialize button
        b2=new Texture("button1Test.png");
        //Initialize button
        b1=new Texture("button1Test.png");
        //Initialize background
        backgorund = new Texture("bgTest.png");

        batch = new SpriteBatch();

    }

    public int render(){

        batch.begin();
        batch.draw(backgorund,0,0);
        batch.draw(b1,576,144);
        batch.draw(b2, 576, 324);
        batch.draw(banner, 128, 448);
        batch.end();

        return listenInput();
    }

    public int listenInput(){

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            //Check positions
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            // If button 1, start playing
            if(x > 576 && x < 704 && y > 324 && y < 396)
                return 1;
            //If button 2, close program
            else if(x > 576 && x < 704 && y > 144 && y < 216)
                return 2;
        }

        return 0;
    }

    public void dispose(){
        batch.dispose();
        backgorund.dispose();
        b1.dispose();
        b2.dispose();
        banner.dispose();
    }

}
