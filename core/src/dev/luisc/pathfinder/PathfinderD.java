package dev.luisc.pathfinder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import dev.luisc.pathfinder.entities.ProjectilePool;
import dev.luisc.pathfinder.levels.*;

/**
 * Entry Point for the game
 */
public class PathfinderD extends ApplicationAdapter {

	OrthographicCamera camera; //Camera of the game
	Cinematic c; //The cinematic object
	RenderClass renderObject; //The current rendered scene, menu, level...
	//TODO: DOCUMENT EVERYTHING
	static Music bgm; //Background music

	/**
	 * Initializes the camera, the projectile pool for the player, the cinematic and the bgm
	 */
	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		ProjectilePool.getInstance();
		c = new Cinematic();
		renderObject = c;
		bgm = Gdx.audio.newMusic(Gdx.files.internal("sounds/PathFinderTheme.mp3"));

	}

	/**
	 * Invokes the render method in the currently rendered object
	 */
	@Override
	public void render () {
        bgm.play();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderObject = renderObject.render(this.camera);

		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	@Override
	public void dispose () {
	}

}
