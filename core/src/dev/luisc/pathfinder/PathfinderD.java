package dev.luisc.pathfinder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.luisc.pathfinder.entities.*;
import dev.luisc.pathfinder.levels.Level;

import java.io.*;

public class PathfinderD extends ApplicationAdapter {

	Level levelTest;
	OrthographicCamera camera;




	@Override
	public void create () {

		camera = new OrthographicCamera();
		camera.setToOrtho(false,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		LevelIOTEST.saveTest();
		levelTest= LevelIOTEST.loadTest();

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			levelTest.playerTest.rotate(false);
		}else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			levelTest.playerTest.rotate(true);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			levelTest.playerTest.accelerate(true);
		}else if(Gdx.input.isKeyPressed(Input.Keys.S)){
			levelTest.playerTest.accelerate(false);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.SPACE))levelTest.playerTest.boost();

		if(Gdx.input.isKeyJustPressed(Input.Keys.Z)) levelTest.playerShoot();

		camera.position.set(levelTest.playerTest.getPos(), 0); // Maybe add dynamic zoom based on speed??
		camera.update();

		levelTest.batch.setProjectionMatrix(camera.combined);
		levelTest.shapeRenderer.setProjectionMatrix(camera.combined);

		//Lo que va antes se renderiza por debajo (Uso para paralax?????)
		if(levelTest.render()){
			levelTest = LevelIOTEST.loadTest();
			System.out.println("Recargando nivel, el jugador o ha muerto o ha matado a las entidades");
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
