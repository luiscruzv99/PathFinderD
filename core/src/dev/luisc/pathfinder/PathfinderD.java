package dev.luisc.pathfinder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import dev.luisc.pathfinder.entities.ProjectilePool;
import dev.luisc.pathfinder.levels.Level;

public class PathfinderD extends ApplicationAdapter {

	Level levelTest;
	OrthographicCamera camera;

	@Override
	public void create () {

		camera = new OrthographicCamera();
		camera.setToOrtho(false,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = 0.8f;
		//LevelIOTEST.saveTest();
		levelTest= LevelIOTEST.loadTest();
		ProjectilePool.getInstance();

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//TODO: MOVE THIS TO CLASS CONTROL LISTENER
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			levelTest.getPlayerTest().rotate(false);
		}else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			levelTest.getPlayerTest().rotate(true);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			levelTest.getPlayerTest().accelerate(true);
		}else if(Gdx.input.isKeyPressed(Input.Keys.S)){
			levelTest.getPlayerTest().accelerate(false);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.SPACE))levelTest.getPlayerTest().boost();

		if(Gdx.input.isKeyJustPressed(Input.Keys.Z)) levelTest.getPlayerTest().shoot();


		camera.position.set(levelTest.getPlayerTest().getPos(), 0);

		if(Math.abs(levelTest.getPlayerTest().getSpeedComponent()) > levelTest.getPlayerTest().MAX_SPEED/4 && camera.zoom < 1.2){
			camera.zoom += 1.33 * Gdx.graphics.getDeltaTime();
		}else if(Math.abs(levelTest.getPlayerTest().getSpeedComponent()) < levelTest.getPlayerTest().MAX_SPEED/6 && camera.zoom > 0.8){
			camera.zoom -= 0.33 * Gdx.graphics.getDeltaTime();
		}

		camera.update();

		levelTest.getBatch().setProjectionMatrix(camera.combined);
		levelTest.getDebugRenderer().setProjectionMatrix(camera.combined);

		//Lo que va antes se renderiza por debajo (Uso para paralax?????)
		if(levelTest.render()){
			levelTest.cleanUp();
			levelTest.preSerialize();
			levelTest = LevelIOTEST.loadTest();
			//System.out.println("Recargando nivel, el jugador o ha muerto o ha matado a las entidades");
		};
		levelTest.debugRender();

		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glDisable(GL20.GL_BLEND);


	}
	
	@Override
	public void dispose () {
		levelTest.cleanUp();
	}

}
