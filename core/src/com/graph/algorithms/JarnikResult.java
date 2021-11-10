package com.graph.algorithms;

import java.util.ArrayList;

public class JarnikResult {
    private final ArrayList<int[]> minimumArcs=new ArrayList<>();
    private int totalTreeWeight=0;

    public void addEdge(int[] arc){
        minimumArcs.add(arc);
    }

    public void addToTreeWeight(int a){
        totalTreeWeight+=a;
    }
}