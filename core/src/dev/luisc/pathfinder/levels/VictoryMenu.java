package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Menu to show when the player completes a Navigation Level
 */
public class VictoryMenu implements RenderClass{

    private Texture bg; //Background
    private Texture btnQuit; //Quit Button
    private Texture btnMenu; //Menu Button
    private Sprite dialog; //Dialog background
    private BitmapFont title; //Title font
    private BitmapFont score; //Score font
    private int numShips; //Number of ships that made it to the end
    private RenderClass[] renderCurrent;
    private SpriteBatch batch;

    /**
     * Initializes the VictoryMenu
     * @param numShips
     */
    public VictoryMenu(int numShips){
        bg = new Texture("menu_background.png");
        dialog = new Sprite(new Texture("end_dialog.png"));
        btnMenu = new Texture("btn_menu.png");
        btnQuit = new Texture("button2.png");
        this.numShips = numShips;
        title = new BitmapFont(Gdx.files.internal("fonts/font_big.fnt"), Gdx.files.internal("fonts/font_big.png"), false);
        score = new BitmapFont(Gdx.files.internal("fonts/testFont.fnt"), Gdx.files.internal("fonts/testFont.png"), false);
        renderCurrent = new RenderClass[]{this, new Menu()};
        batch = new SpriteBatch();
    }

    /**
     * Renders the victory menu and listens for inputs
     * @param c
     * @return
     */
    @Override
    public RenderClass render(OrthographicCamera c) {
        batch.begin();
        batch.draw(bg,0,0);
        batch.draw(dialog, 320, 40,0,0,640,630,1,1,0);
        title.draw(batch, "Level Complete", 360, 645);
        score.draw(batch,"Ships alive: "+numShips+"\n\n\n\n\nScore: "+numShips*25,360,550);
        batch.draw(btnMenu,512,150);
        batch.draw(btnQuit, 576, 70);
        batch.end();

        return renderCurrent[listenInput()];
    }

    /**
     * Listens for inputs and returns the appropiate value
     * @return
     */
    public int listenInput(){

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            //Check positions
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            // If button 1, start playing
            if(x > 512 && x < 768 && y > 150 && y < 222) {
                return 1;

            }else if(x > 576 && x < 704 && y > 70 && y < 142) //If button 2, close program
                System.exit(0);
        }

        return 0;
    }

    /**
     * Deconstructs the menu
     */
    public void dispose(){
        batch.dispose();
        bg.dispose();
        btnQuit.dispose();
        btnMenu.dispose();
        dialog.getTexture().dispose();
    }
}
