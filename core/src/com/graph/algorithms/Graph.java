package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
    private final ArrayList<ArrayList<int[]>> adjacencyList = new ArrayList<>();
    private final ArrayList<float[]> coordinates = new ArrayList<>();
    private final boolean digraph;
    private String name;

    public Graph(final Boolean digraph) {
        this.digraph = digraph;
    }

    public Graph(final FileHandle graph) {
        final Scanner scanner = new Scanner(graph.read());
        final String firstLine = scanner.nextLine();
        int currentIndex = firstLine.indexOf('=');
        digraph = Boolean.parseBoolean(firstLine.substring(currentIndex + 1));
        while (scanner.hasNext()) {
            final String currentLine = scanner.nextLine();
            final ArrayList<ArrayList<Integer>> occurrences = new ArrayList<>();//[0] is open square bracket, [1] is comma, [2] is close square bracket, [3] is open bracket, [4] is close bracket
            currentIndex = currentLine.indexOf('[');
            occurrences.add(new ArrayList<Integer>());
            while (currentIndex != -1) {
                occurrences.get(0).add(currentIndex);
                currentIndex = currentLine.indexOf('[', currentIndex + 1);
            }
            currentIndex = currentLine.indexOf(',');
            occurrences.add(new ArrayList<Integer>());
            while (currentIndex != -1) {
                occurrences.get(1).add(currentIndex);
                currentIndex = currentLine.indexOf(',', currentIndex + 1);
            }
            currentIndex = currentLine.indexOf(']');
            occurrences.add(new ArrayList<Integer>());
            while (currentIndex != -1) {
                occurrences.get(2).add(currentIndex);
                currentIndex = currentLine.indexOf(']', currentIndex + 1);
            }
            currentIndex = currentLine.indexOf('(');
            occurrences.add(new ArrayList<Integer>());
            while (currentIndex != -1) {
                occurrences.get(3).add(currentIndex);
                currentIndex = currentLine.indexOf('(', currentIndex + 1);
            }
            currentIndex = currentLine.indexOf(')');
            occurrences.add(new ArrayList<Integer>());
            while (currentIndex != -1) {
                occurrences.get(4).add(currentIndex);
                currentIndex = currentLine.indexOf(')', currentIndex + 1);
            }
            adjacencyList.add(new ArrayList<int[]>());
            coordinates.add(new float[]{Float.parseFloat(currentLine.substring(occurrences.get(3).get(0) + 1, occurrences.get(1).get(0))), Float.parseFloat(currentLine.substring(occurrences.get(1).get(0) + 1, occurrences.get(4).get(0)))});
            for (int a = 0; a < occurrences.get(0).size(); a++) {
                adjacencyList.get(adjacencyList.size() - 1).add(new int[]{Integer.parseInt(currentLine.substring(occurrences.get(0).get(a) + 1, occurrences.get(1).get(a + 1))), Integer.parseInt(currentLine.substring(occurrences.get(1).get(a + 1) + 1, occurrences.get(2).get(a)))});
            }
        }
        scanner.close();
        name = graph.nameWithoutExtension();
    }

    private static int findSmallestNonPermanentTemporaryLabel(final ArrayList<ArrayList<Integer>> temporaryLabels,
                                                              final int[] orderLabels) {
        int smallest = -1;
        for (int a = 0; a < temporaryLabels.size(); a++) {
            if (temporaryLabels.get(a).size() != 0 && orderLabels[a] == -1) {
                if (smallest == -1) {
                    smallest = a;
                } else if (temporaryLabels.get(a).get(temporaryLabels.get(a).size() - 1) < temporaryLabels.get(smallest)
                        .get(temporaryLabels.get(smallest).size() - 1)) {
                    smallest = a;
                }
            }
        }
        return smallest;
    }

    public void changeName(final String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public boolean isDigraph() {
        return digraph;
    }

    public void saveGraph(final String fileName) {
        final FileHandle file = Gdx.files.local("graphs/" + fileName + ".graph");
        if (file.exists()) {
            file.delete();
        }
        file.writeString("digraph=" + digraph, true);
        for (int a = 0; a < adjacencyList.size(); a++) {
            final StringBuilder line = new StringBuilder("(" + coordinates.get(a)[0] + "," + coordinates.get(a)[1] + ")");
            for (int b = 0; b < adjacencyList.get(a).size(); b++) {
                line.append(" [").append(adjacencyList.get(a).get(b)[0]).append(",").append(adjacencyList.get(a).get(b)[1]).append("]");
            }

            file.writeString("\n" + line, true);
        }
        changeName(fileName);
    }

    public boolean areVerticesConnected(final int vertex1, final int vertex2) {
        boolean connected = false;
        for (int a = 0; a < adjacencyList.get(vertex1).size(); a++) {
            if (adjacencyList.get(vertex1).get(a)[0] == vertex2) {
                connected = true;
                break;
            }
        }
        return connected;
    }

    public void addVertex(final float x, final float y) {
        adjacencyList.add(new ArrayList<int[]>());
        coordinates.add(new float[]{x, y});
    }

    public void addDirectedEdge(final int from, final int to, final int length) {
        adjacencyList.get(from).add(new int[]{to, length});
    }

    public void addUndirectedEdge(final int vertex1, final int vertex2, final int length) {
        addDirectedEdge(vertex1, vertex2, length);
        addDirectedEdge(vertex2, vertex1, length);
    }

    public int getAdjacencyListSize() {
        return adjacencyList.size();
    }

    public float getXCoordinate(final int a) {
        return coordinates.get(a)[0];
    }

    public float getYCoordinate(final int a) {
        return coordinates.get(a)[1];
    }

    public int getNumberOfEdges(final int a) {
        return adjacencyList.get(a).size();
    }

    public int getVertex(final int a, final int b) {
        return adjacencyList.get(a).get(b)[0];
    }

    public int getEdgeWeight(final int a, final int b) {
        return adjacencyList.get(a).get(b)[1];
    }

    public void addToEdgeWeights(ArrayList<EdgeWeight> edgeWeights, final float scaleFactor, final BitmapFont font) {
        for (int a = 0; a < getAdjacencyListSize(); a++) {
            for (int b = 0; b < getNumberOfEdges(a); b++) {
                edgeWeights.add(new EdgeWeight(this, a, getVertex(a, b), Integer.toString(getEdgeWeight(a, b)), font, new float[]{0, 0, 0, 1}, 0, 0, scaleFactor));
            }
        }
    }

    public void setCoordinates(final int index, final float[] element) {
        coordinates.set(index, element);
    }

    private void updateDijkstraLabels(Text[][] dijkstraLabels, final int[] orderLabels, final int[] permanentLabels, final ArrayList<ArrayList<Integer>> temporaryLabels) {
        for (int a = 0; a < adjacencyList.size(); a++) {
            if (orderLabels[a] != -1) {
                dijkstraLabels[a][1].updateText(Integer.toString(orderLabels[a]));
            }
            if (permanentLabels[a] != -1) {
                dijkstraLabels[a][2].updateText(Integer.toString(permanentLabels[a]));
            }
            StringBuilder temp = new StringBuilder();
            for (int b = 0; b < temporaryLabels.get(a).size(); b++) {
                temp.append(temporaryLabels.get(a).get(b));
                if (b != temporaryLabels.get(a).size() - 1) {
                    temp.append(", ");
                }
            }
            dijkstraLabels[a][3].updateText(temp.toString());
        }
    }

    public void dijkstra(final int startVertex, final int endVertex, Text[][] dijkstraLabels) {
        final String[] pathsToEachVertex = new String[adjacencyList.size()];
        pathsToEachVertex[startVertex] = Integer.toString(startVertex);
        final int[] orderLabels = new int[adjacencyList.size()];
        final int[] permanentLabels = new int[adjacencyList.size()];
        final ArrayList<ArrayList<Integer>> temporaryLabels = new ArrayList<>();
        for (int a = 0; a < adjacencyList.size(); a++) {
            permanentLabels[a] = -1;
            orderLabels[a] = -1;
            temporaryLabels.add(new ArrayList<Integer>());
        }
        orderLabels[startVertex] = 1;
        permanentLabels[startVertex] = 0;
        dijkstraRecursion(startVertex, endVertex, pathsToEachVertex, orderLabels, permanentLabels, temporaryLabels, dijkstraLabels);
    }

    private void dijkstraRecursion(final int currentVertex, final int endVertex, final String[] pathsToEachVertex, final int[] orderLabels,
                                   final int[] permanentLabels, final ArrayList<ArrayList<Integer>> temporaryLabels, Text[][] dijkstraLabels) {
        for (int a = 0; a < adjacencyList.get(currentVertex).size(); a++) {
            final int edgeTo = adjacencyList.get(currentVertex).get(a)[0];
            final int edgeWeight = adjacencyList.get(currentVertex).get(a)[1];
            if (temporaryLabels.get(edgeTo).size() == 0 || permanentLabels[currentVertex] + edgeWeight < temporaryLabels.get(edgeTo).get(temporaryLabels.get(edgeTo).size() - 1)) {
                temporaryLabels.get(edgeTo).add(permanentLabels[currentVertex] + edgeWeight);
                pathsToEachVertex[edgeTo] = pathsToEachVertex[currentVertex] + edgeTo;
                updateDijkstraLabels(dijkstraLabels, orderLabels, permanentLabels, temporaryLabels);
            }
        }
        final int smallest = findSmallestNonPermanentTemporaryLabel(temporaryLabels, orderLabels);
        permanentLabels[smallest] = temporaryLabels.get(smallest).get(temporaryLabels.get(smallest).size() - 1);
        orderLabels[smallest] = orderLabels[currentVertex] + 1;
        updateDijkstraLabels(dijkstraLabels, orderLabels, permanentLabels, temporaryLabels);
        if (permanentLabels[endVertex] == -1) {
            dijkstraRecursion(smallest, endVertex, pathsToEachVertex, orderLabels, permanentLabels, temporaryLabels, dijkstraLabels);
        }
    }

    public JarnikResult jarnik() {
        JarnikResult jarnikResult = new JarnikResult();
        final ArrayList<Integer> includedVertices = new ArrayList<>();
        includedVertices.add(0);
        jarnikResult = jarnikRecursion(jarnikResult, includedVertices);
        return jarnikResult;
    }

    private JarnikResult jarnikRecursion(final JarnikResult jarnikResult, final ArrayList<Integer> includedVertices) {
        final int[] smallestEdge = new int[]{-1, -1, -1}; // {from,to,weight}
        for (int a = 0; a < includedVertices.size(); a++) {
            for (int b = 0; b < adjacencyList.get(a).size(); b++) {
                final int edgeTo = adjacencyList.get(includedVertices.get(a)).get(b)[0];
                final int edgeWeight = adjacencyList.get(includedVertices.get(a)).get(b)[1];
                if (!includedVertices.contains(edgeTo) && (smallestEdge[0] == -1 || edgeWeight < smallestEdge[2])) {
                    smallestEdge[0] = includedVertices.get(a);
                    smallestEdge[1] = edgeTo;
                    smallestEdge[2] = edgeWeight;
                }
            }
        }
        jarnikResult.addEdge(smallestEdge);
        includedVertices.add(smallestEdge[1]);
        jarnikResult.addToTreeWeight(smallestEdge[2]);
        if (includedVertices.size() < adjacencyList.size()) {
            return jarnikRecursion(jarnikResult, includedVertices);
        }
        return jarnikResult;
    }
}