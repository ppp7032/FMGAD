package com.graph.algorithms;

import java.util.ArrayList;

public class JarnikResult {
    private final ArrayList<int[]> minimumEdges = new ArrayList<>();
    private int totalTreeWeight = 0;
    private int counter = 1;

    public void addEdge(final int[] edge) {
        minimumEdges.add(edge);
    }

    public void addToTreeWeight(final int a) {
        totalTreeWeight += a;
    }

    public int getCounter() {
        return counter;
    }

    public void incrementCounter() {
        counter++;
    }

    public int getFromVertex(final int edge) {
        return minimumEdges.get(edge)[0];
    }

    public int getToVertex(final int edge) {
        return minimumEdges.get(edge)[1];
    }
}