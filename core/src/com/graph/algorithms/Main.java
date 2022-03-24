package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Main extends Game {
    @Override
    public void create() {
        Gdx.graphics.setTitle("FMGAD");
        final String[] config = Settings.readFromConfigFile();
        Graphics.setDisplayMode(config[1], config[0]);
        setScreen(new MainMenu());
        /*Graph digraph = new Graph(Gdx.files.internal("graphs/digraph 3i.graph"));
        DijkstraResult digraphResult = digraph.dijkstra(2, 4);
        System.out.println();*/
//        Graph graph1 = new Graph(Gdx.files.internal("graphs/New Graph.graph"));
//        graph1.routeInspection();
        //JarnikResult jarnikResult = graph1.jarnik();
        //System.out.println(graph1.depthFirstSearch());
    }
}