package com.graph.algorithms;

import java.util.ArrayList;

public class JarnikResult {
    final ArrayList<int[]> minimumArcs;
    int totalTreeWeight;

    public JarnikResult() {
        this.minimumArcs = new ArrayList<>();
        totalTreeWeight = 0;
    }
}