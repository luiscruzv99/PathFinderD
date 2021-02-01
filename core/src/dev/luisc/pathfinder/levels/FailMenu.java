package dev.luisc.pathfinder.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FailMenu implements RenderClass{

    private Texture bg;
    private Texture btnRetry;
    private Texture btnMenu;
    private Sprite dialog;
    private BitmapFont title;
    private BitmapFont score;
    private RenderClass[] renderCurrent;
    private SpriteBatch batch;
    private Level lv;

    public FailMenu(Level lv){
        bg = new Texture("menu_background.png");
        dialog = new Sprite(new Texture("end_dialog.png"));
        btnMenu = new Texture("btn_menu.png");
        btnRetry = new Texture("btn_retry.png");
        title = new BitmapFont(Gdx.files.internal("fonts/font_big.fnt"), Gdx.files.internal("fonts/font_big.png"), false);
        score = new BitmapFont(Gdx.files.internal("fonts/testFont.fnt"), Gdx.files.internal("fonts/testFont.png"), false);
        renderCurrent = new RenderClass[]{this, lv, new Menu()};
        batch = new SpriteBatch();
        this.lv = lv;
    }

    @Override
    public RenderClass render(OrthographicCamera c) {
        batch.begin();
        batch.draw(bg,0,0);
        batch.draw(dialog, 320, 40,0,0,640,630,1,1,0);
        title.draw(batch, "Level Failed", 360, 645);
        batch.draw(btnMenu,512,150);
        batch.draw(btnRetry, 576, 70);
        batch.end();

        return renderCurrent[listenInput()];
    }

    public int listenInput(){

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            //Check positions
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            // If button 1, start playing
            if(x > 512 && x < 768 && y > 150 && y < 222) {
                return 2;

            }else if(x > 576 && x < 704 && y > 70 && y < 142) { //If button 2, close program
                lv.reset();
                return 1;
            }
        }

        return 0;
    }
}
