package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Main extends Game {
    @Override
    public void create() {
        Gdx.graphics.setTitle("FMGAD");
        setScreen(new MainMenu(Gdx.graphics.getHeight() / 720f));
        Graph threei = new Graph(Gdx.files.internal("graphs/digraph 3i.graph"));
        DijkstraResult threeiResult = threei.dijkstra(2, 4);

        Graph graph1 = new Graph(Gdx.files.internal("graphs/graph 1.graph"));
        JarnikResult jarnikResult = graph1.jarnik();
        System.out.println();
    }
}