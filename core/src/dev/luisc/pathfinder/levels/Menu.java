package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import dev.luisc.pathfinder.LevelIOTEST;

/**
 * Main Menu of the game
 */
public class Menu implements RenderClass{

    Texture backgorund; //Background
    Texture b1; //Button 1 (play)
    Texture b2; //Button 2 (exit)
    Texture banner; //Banner of the game
    SpriteBatch batch;

    Level lv; //Level to render after the menu

    private RenderClass[] currentNext;

    /**
     * Initializes the menu
     */
    public Menu(){

        banner = new Texture("banner.png");
        b2=new Texture("button1.png");
        b1=new Texture("button2.png");
        backgorund = new Texture("menu_background.png");

        batch = new SpriteBatch();

        currentNext = new RenderClass[] {this, lv};

    }

    /**
     * Renders the menu
     * @param c
     * @return
     */
    @Override
    public RenderClass render(OrthographicCamera c){

        batch.begin();
        batch.draw(backgorund,0,0);
        batch.draw(b1,576,144);
        batch.draw(b2, 576, 324);
        batch.draw(banner, 128, 448);
        batch.end();

        return currentNext[listenInput()];
    }

    /**
     * Listens to the users inputs
     * @return
     */
    public int listenInput(){

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            //Check positions
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            // If button 1, start playing
            if(x > 576 && x < 704 && y > 324 && y < 396) {
                lv = LevelIOTEST.loadTest();
                currentNext[1] = lv;
                return 1;

            }else if(x > 576 && x < 704 && y > 144 && y < 216) //If button 2, close program
                System.exit(0);
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
