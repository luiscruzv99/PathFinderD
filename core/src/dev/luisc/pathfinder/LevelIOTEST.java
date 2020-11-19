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

        Level lev = new Level(null);

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
        Level lev = new Level(null);
        ArrayList<Entity> entities= new ArrayList<>();
        for(int i=0; i<10; i++){
            entities.add(new Entity("playerTest.png",new Polygon(new float[]{0,0, 10,10, 0,10}), new Vector2(i*300, i*250+30)));
        }
        lev.setEntities(entities);
        lev.setStartPoint(new Vector2(50,50));
        lev.setBounds(new Polygon(new float[]{0,0,25,500,50,500,500,1500,1500,2000,2500,500,3000,0}));
        lev.setBackgroundPath("bgTest.png");
        lev.preSerialize();

        try(Writer writer = new FileWriter("core/assets/levels/levelTest.json")){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(lev, writer);
        }catch(IOException e){
            System.out.println("ERROR");
        }
    }
}
