package dev.luisc.pathfinder.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.LifecycleListener;
import dev.luisc.pathfinder.entities.PlayerEntity;
import dev.luisc.pathfinder.levels.Level;
import dev.luisc.pathfinder.levels.NavigationLevel;

public class InputHandler {

    private static PlayerEntity player;
    private static Level level;
    private static InputHandler INSTANCE = null;

    private InputHandler(){}

    public static InputHandler getInstance(){
        if(INSTANCE==null) INSTANCE = new InputHandler();
        return INSTANCE;
    }

    public static void setLevel(Level level) {
        InputHandler.level = level;
    }

    public static void setPlayer(PlayerEntity player) {
        InputHandler.player = player;
    }

    public static void listenInputs(){

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.rotate(false);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.rotate(true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.accelerate(true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.accelerate(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) player.boost();

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) player.shoot();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q) && level.getClass().getSimpleName().equals("NavigationLevel"))
            ((NavigationLevel)level).placeBeacon();

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) System.exit(0);
    }
}
