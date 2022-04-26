package com.graph.algorithms;

import java.util.ArrayList;

public class DijkstraContainer {
    private int currentVertex;
    private int endVertex;
    private ArrayList<ArrayList<Integer>> pathsToEachVertex;
    private int[] orderLabels;
    private int[] permanentLabels;
    private ArrayList<ArrayList<Integer>> temporaryLabels;
    private boolean setup;

    public DijkstraContainer(final int currentVertex, final int endVertex, final ArrayList<ArrayList<Integer>> pathsToEachVertex, final int[] orderLabels,
                             final int[] permanentLabels, final ArrayList<ArrayList<Integer>> temporaryLabels) {
        this.currentVertex = currentVertex;
        this.endVertex = endVertex;
        this.pathsToEachVertex = pathsToEachVertex;
        this.orderLabels = orderLabels;
        this.permanentLabels = permanentLabels;
        this.temporaryLabels = temporaryLabels;
        setup = true;
    }

    public DijkstraContainer() {
        setup = false;
    }

    public ArrayList<Integer> getPathToVertex(final int a) {
        return pathsToEachVertex.get(a);
    }

    public ArrayList<ArrayList<Integer>> getTemporaryLabels() {
        return temporaryLabels;
    }

    public int[] getPermanentLabels() {
        return permanentLabels;
    }

    public int getCurrentVertex() {
        return currentVertex;
    }

    public void setCurrentVertex(final int newVertex) {
        currentVertex = newVertex;
    }

    public int getEndVertex() {
        return endVertex;
    }

    public int[] getOrderLabels() {
        return orderLabels;
    }

    public void markAsNotSetup() {
        setup = false;
    }

    public boolean getSetup() {
        return setup;
    }
}
