package com.graph.algorithms;

import java.util.ArrayList;

public class DijkstraContainer {
    int currentVertex;
    int endVertex;
    ArrayList<ArrayList<Integer>> pathsToEachVertex;
    int[] orderLabels;
    int[] permanentLabels;
    ArrayList<ArrayList<Integer>> temporaryLabels;
    boolean setup;

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
}
