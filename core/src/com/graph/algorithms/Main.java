package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Main extends Game {
    @Override
    public void create() {
        Gdx.graphics.setTitle("FMGAD");
        final int[] config = Settings.readFromConfigFile();
        Graphics.setDisplayMode(config[1],config[0]);
        setScreen(new MainMenu());
        final Graph test = new Graph(Gdx.files.internal("graphs/test.graph2"));
        System.out.println();
        /*Graph digraph = new Graph(Gdx.files.internal("graphs/digraph 3i.graph2"));
        DijkstraResult digraphResult = digraph.dijkstra(2, 4);
        Graph graph1 = new Graph(Gdx.files.internal("graphs/graph 1.graph2"));
        JarnikResult jarnikResult = graph1.jarnik();*/
    }
}