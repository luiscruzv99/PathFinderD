package dev.luisc.pathfinder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import dev.luisc.pathfinder.entities.ProjectilePool;
import dev.luisc.pathfinder.levels.Level;
import dev.luisc.pathfinder.levels.Menu;
import dev.luisc.pathfinder.levels.NavigationLevel;
import dev.luisc.pathfinder.levels.RenderClass;

public class PathfinderD extends ApplicationAdapter {

	NavigationLevel levelTest;
	OrthographicCamera camera;
	Menu menu;
	RenderClass renderObject;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = 0.8f;
		LevelIOTEST.saveTest();

		ProjectilePool.getInstance();
		menu = new Menu();
		renderObject = menu;

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderObject = renderObject.render(this.camera);

		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glDisable(GL20.GL_BLEND);

		//TODO: Weird graphical glicth with camera in the phase 2 of navigation levels

	}
	
	@Override
	public void dispose () {
		levelTest.cleanUp();
	}

}
