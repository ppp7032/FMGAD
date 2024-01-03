package com.graph.algorithms;

import java.util.ArrayList;
import java.util.Collections;

public class DijkstraContainer {

  private int currentVertex;
  private int endVertex;
  private ArrayList<ArrayList<Integer>> pathsToEachVertex;
  private int[] orderLabels;
  private int[] permanentLabels;
  private ArrayList<ArrayList<Integer>> temporaryLabels;
  private boolean setup;

  public DijkstraContainer(final int startVertex, final int endVertex, final int numberOfVertices) {
    final ArrayList<ArrayList<Integer>> pathsToEachVertex = new ArrayList<>();
    for (int a = 0; a < numberOfVertices; a++) {
      pathsToEachVertex.add(new ArrayList<>());
    }
    pathsToEachVertex.set(startVertex, new ArrayList<>(Collections.singletonList(startVertex)));
    final int[] orderLabels = new int[numberOfVertices];
    final int[] permanentLabels = new int[numberOfVertices];
    final ArrayList<ArrayList<Integer>> temporaryLabels = new ArrayList<>();
    for (int a = 0; a < numberOfVertices; a++) {
      permanentLabels[a] = -1;
      orderLabels[a] = -1;
      temporaryLabels.add(new ArrayList<>());
    }
    orderLabels[startVertex] = 1;
    permanentLabels[startVertex] = 0;
    this.currentVertex = startVertex;
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

  public int getLargestOrderLabel() {
    int biggestValue = -1;
    int biggestIndex = -1;
    for (int a = 0; a < orderLabels.length; a++) {
      if (orderLabels[a] > biggestValue) {
        biggestValue = orderLabels[a];
        biggestIndex = a;
      }
    }
    return biggestIndex;
  }
}
