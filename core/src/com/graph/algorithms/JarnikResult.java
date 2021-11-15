package com.graph.algorithms;

import java.util.ArrayList;

public class JarnikResult {
    private final ArrayList<int[]> minimumEdges = new ArrayList<>();
    private int totalTreeWeight = 0;

    public void addEdge(int[] edge) {
        minimumEdges.add(edge);
    }

    public void addToTreeWeight(int a) {
        totalTreeWeight += a;
    }
}