package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Main extends Game {
    @Override
    public void create() {
        Gdx.graphics.setTitle("FMGAD");
        int[] config = Settings.readFromConfigFile();
        switch (config[1]) {
            case 1:
                switch (config[0]) {
                    case (0):
                        Gdx.graphics.setWindowedMode(3840, 2160);
                        break;
                    case (1):
                        Gdx.graphics.setWindowedMode(2560, 1440);
                        break;
                    case (2):
                        Gdx.graphics.setWindowedMode(1920, 1080);
                        break;
                    case (3):
                        Gdx.graphics.setWindowedMode(1600, 900);
                        break;
                    case (4):
                        Gdx.graphics.setWindowedMode(1280, 720);
                        break;
                }
                break;
            case 0:
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                break;
        }
        setScreen(new MainMenu());
        Graph digraph = new Graph(Gdx.files.internal("graphs/digraph 3i.graph2"));
        DijkstraResult digraphResult = digraph.dijkstra(2, 4);
        Graph graph1 = new Graph(Gdx.files.internal("graphs/graph 1.graph2"));
        JarnikResult jarnikResult = graph1.jarnik();
    }
}