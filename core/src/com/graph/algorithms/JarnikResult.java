package com.graph.algorithms;

import java.util.ArrayList;

public class JarnikResult {
    final ArrayList<int[]> minimumArcs;
    final ArrayList<Integer> includedNodes;
    int totalTreeWeight;

    public JarnikResult() {
        this.minimumArcs = new ArrayList<>();
        this.includedNodes = new ArrayList<>();
        totalTreeWeight = 0;
    }
}