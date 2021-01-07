package dev.luisc.pathfinder;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.luisc.pathfinder.entities.Entity;
import dev.luisc.pathfinder.levels.Level;

import java.io.*;
import java.util.ArrayList;

public class LevelIOTEST {

    public static Level loadTest(){

        Level lev = null;

        try(Reader reader = new FileReader("core/assets/levels/levelTest.json")){
            Gson gson = new GsonBuilder().create();
            lev = gson.fromJson(reader, Level.class);
            lev.postDeSerialize();

        }catch(IOException e){
            System.out.println("ERROR");
        }
        return lev;
    }

    public static void saveTest(){

        /**
         * You cannot deserialize Texture objects, therefore they are set to null
         * Proposed solution: add a texturePath String in the objects, which will save
         * the path of the texture, which will have to be loaded after the object's
         * deserialization.
         */

        ArrayList<Vector2> entities= new ArrayList<>();
        for(int i=0; i<10; i++){
            entities.add(new Vector2(i*150+2500, i*150*(float)Math.random()+400));
        }
        Level lev = new Level(entities, new Vector2(1000,1000),new Polygon(new float[]{800,400,800,4600,4200,4600,4200,400}),"bgTest.png");
        //lev.preSerialize();

        try(Writer writer = new FileWriter("core/assets/levels/levelTest.json")){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(lev, writer);
        }catch(IOException e){
            System.out.println("ERROR");
        }
    }
}
