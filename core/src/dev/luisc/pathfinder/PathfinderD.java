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
	SpriteBatch batch;
	PlayerEntity playerTest;
	ShapeRenderer shapeRenderer;
	Level levelTest;
	OrthographicCamera camera;




	@Override
	public void create () {

		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		playerTest = new PlayerEntity(new Vector2(100,100));
		levelTest = LevelIOTEST.loadTest();
		shapeRenderer = new ShapeRenderer();



	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			playerTest.rotate(false);
		}else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			playerTest.rotate(true);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			playerTest.accelerate(true);
		}else if(Gdx.input.isKeyPressed(Input.Keys.S)){
			playerTest.accelerate(false);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.SPACE))playerTest.boost();

		playerTest.move();



		batch.begin();
		batch.draw(levelTest.getBackground(), 0,0);
		batch.draw(levelTest.getEntity().getSprite(), levelTest.getEntity().getPos().x, levelTest.getEntity().getPos().y);
		batch.draw(playerTest.getSprite(), playerTest.getPos().x, playerTest.getPos().y,0,0,50,50,1,1,playerTest.getRotation());
		batch.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(0, 0, 0, 0.0f);

		shapeRenderer.polygon(playerTest.getCollisionBox().getTransformedVertices());
		shapeRenderer.end();


		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glDisable(GL20.GL_BLEND);

	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
