package dev.luisc.pathfinder;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.luisc.pathfinder.levels.Level;
import dev.luisc.pathfinder.levels.NavigationLevel;

import java.io.*;
import java.util.ArrayList;

/**
 * Reads and writes levels from files
 */
public class LevelIOTEST {

    private static Level lv; //Test Level

    /**
     * Load the testLevel for the game
     * @return the loaded level
     */
    public static Level loadTest(){

        if(lv!=null){
            lv.reset();
        }else {
            try (Reader reader = new FileReader("levelTest.json")) {
                Gson gson = new GsonBuilder().create();
                lv = gson.fromJson(reader, NavigationLevel.class);
                lv.postDeSerialize();

            } catch (IOException e) {
                System.out.println("ERROR");
            }
        }
        return lv;
    }

    /**
     * Create the test level and save it
     */
    public static void saveTest(){


        ArrayList<Vector2> entities= new ArrayList<>();
        for(int i=0; i<10; i++){
            entities.add(new Vector2(i*150+2500, i*150*(float)Math.random()+400));
        }
        ArrayList<Vector2> allies = new ArrayList<>();
        for(int i = 0; i<5; i++){
            allies.add(new Vector2(1500+i*50, 900));
        }
        NavigationLevel lev = new NavigationLevel(entities, new Vector2(1000,1000),new Polygon(new float[]{800,400,800,4600,4200,4600,4200,400}),
                "background_v1.png",allies, new Vector2(3000,3000), 100);
        lev.preSerialize();

        try(Writer writer = new FileWriter("levelTest.json")){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(lev, writer);
        }catch(IOException e){
            System.out.println("ERROR");
        }
    }
}
