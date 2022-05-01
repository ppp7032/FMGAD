package com.graph.algorithms;

import com.badlogic.gdx.Game;

import java.util.ArrayList;

public class Main extends Game {
    private ArrayList<EdgeWeight> edgeWeights;
    private ArrayList<Text> vertexLabels;
    private MainMenu mainMenu;
    private Settings settings;
    private SelectGraphType selectGraphType;
    private NewGraph newGraph;
    private SelectGraph selectGraph;
    private LoadGraph loadGraph;
    private Graph graph;

    @Override
    public void create() {
        mainMenu = new MainMenu();
        settings = new Settings();
        graph = new Graph(false);
        selectGraphType = new SelectGraphType();
        selectGraph = new SelectGraph();
        edgeWeights = new ArrayList<>();
        vertexLabels = new ArrayList<>();
        newGraph = new NewGraph(graph, edgeWeights, vertexLabels);
        loadGraph = new LoadGraph(graph, edgeWeights, vertexLabels);
        setScreen(mainMenu);
    }

    public void clearNewGraph(final boolean newStatus) {
        newGraph.clear(newStatus);
    }

    public void loadGraph(final Graph loadedGraph) {
        clearNewGraph(loadedGraph.isDigraph());
        for (int a = 0; a < loadedGraph.getNumberOfVertices(); a++) {
            graph.addVertex(loadedGraph.getXCoordinateOfVertex(a), loadedGraph.getYCoordinateOfVertex(a));
            vertexLabels.add(new Text(Character.toString((char) (a + 65)), graph.getXCoordinateOfVertex(a) * Graphics.scaleFactor, graph.getYCoordinateOfVertex(a) * Graphics.scaleFactor, Graphics.fonts[3], new float[]{0, 0, 0, 1}, 0, 0, -1));
            for (int b = 0; b < loadedGraph.getNumberOfEdgesConnectedToVertex(a); b++) {
                graph.addDirectedEdge(a, loadedGraph.getVertex(a, b), loadedGraph.getEdgeWeight(a, b));
            }
        }
        graph.changeName(loadedGraph.getName());
        EdgeWeight.addToEdgeWeights(graph, edgeWeights);
    }

    public void setScreen(ScreenKey screenKey) {
        switch (screenKey) {
            case NewGraph:
                setScreen(newGraph);
                break;
            case LoadGraph:
                setScreen(loadGraph);
                break;
            case MainMenu:
                setScreen(mainMenu);
                break;
            case Settings:
                setScreen(settings);
                break;
            case SelectGraphType:
                setScreen(selectGraphType);
                break;
            case SelectGraph:
                setScreen(selectGraph);
                break;
        }
    }

    public enum ScreenKey {MainMenu, Settings, SelectGraphType, NewGraph, SelectGraph, LoadGraph}
}