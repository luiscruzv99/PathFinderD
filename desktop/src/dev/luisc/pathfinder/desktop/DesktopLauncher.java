package dev.luisc.pathfinder.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import dev.luisc.pathfinder.PathfinderD;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title="Pathfinder Type.D";
		config.width=1280;
		config.height=720;
		config.resizable=true;
		new LwjglApplication(new PathfinderD(), config);
	}
}
