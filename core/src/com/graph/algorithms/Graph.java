package com.graph.algorithms;

import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
    private final ArrayList<ArrayList<int[]>> adjacencyList = new ArrayList<>();
    private final ArrayList<float[]> coordinates = new ArrayList<>();

    public Graph() {
    }

    public Graph(FileHandle graph) {
        Scanner scanner = new Scanner(graph.read());
        while (scanner.hasNext()) {
            String currentLine = scanner.nextLine();
            ArrayList<ArrayList<Integer>> occurrences = new ArrayList<>();//[0] is open square bracket, [1] is comma, [2] is close square bracket
            int currentIndex = currentLine.indexOf('[');
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
            adjacencyList.add(new ArrayList<int[]>());
            for (int a = 0; a < occurrences.get(0).size(); a++) {
                adjacencyList.get(adjacencyList.size() - 1).add(new int[]{Integer.parseInt(currentLine.substring(occurrences.get(0).get(a) + 1, occurrences.get(1).get(a))), Integer.parseInt(currentLine.substring(occurrences.get(1).get(a) + 1, occurrences.get(2).get(a)))});
            }
        }
    }

    public boolean areVerticesConnected(int vertex1,int vertex2) {
        boolean connected = false;
        for(int a=0;a<adjacencyList.get(vertex1).size(); a++){
            if(adjacencyList.get(vertex1).get(a)[0]==vertex2){
                connected=true;
                break;
            }
        }
        return connected;
    }

    public void addVertex(float x, float y){
        adjacencyList.add(new ArrayList<int[]>());
        coordinates.add(new float[]{x, y});
    }

    public void addDirectedEdge(int from, int to, int length){
        adjacencyList.get(from).add(new int[]{to, length});
    }

    public void addUndirectedEdge(int vertex1, int vertex2, int length){
        addDirectedEdge(vertex1,vertex2,length);
        addDirectedEdge(vertex2,vertex1,length);
    }

    public int getAdjacencyListSize(){
        return adjacencyList.size();
    }

    public float getXCoordinate(int a){
        return coordinates.get(a)[0];
    }

    public float getYCoordinate(int a){
        return coordinates.get(a)[1];
    }

    public int getNumberOfEdges(int a){
        return adjacencyList.get(a).size();
    }

    public int getVertex(int a, int b){
        return adjacencyList.get(a).get(b)[0];
    }

    public void setCoordinates(int index, float[] element){
        coordinates.set(index, element);
    }

    public static int findSmallestNonPermanentTemporaryLabel(ArrayList<ArrayList<Integer>> temporaryLabels,
                                                             int[] orderLabels) {
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

    public DijkstraResult dijkstra(int startNode, int endNode) {
        String[] pathsToEachNode = new String[adjacencyList.size()];
        pathsToEachNode[startNode] = Integer.toString(startNode);
        int[] orderLabels = new int[adjacencyList.size()];
        int[] permanentLabels = new int[adjacencyList.size()];
        ArrayList<ArrayList<Integer>> temporaryLabels = new ArrayList<>();
        for (int a = 0; a < adjacencyList.size(); a++) {
            permanentLabels[a] = -1;
            orderLabels[a] = -1;
            temporaryLabels.add(new ArrayList<Integer>());
        }
        orderLabels[startNode] = 1;
        permanentLabels[startNode] = 0;
        return dijkstraRecursion(startNode, endNode, pathsToEachNode, orderLabels, permanentLabels, temporaryLabels);
    }

    public DijkstraResult dijkstraRecursion(int currentNode, int endNode, String[] pathsToEachNode, int[] orderLabels,
                                            int[] permanentLabels, ArrayList<ArrayList<Integer>> temporaryLabels) {
        for (int a = 0; a < adjacencyList.get(currentNode).size(); a++) {
            int edgeTo = adjacencyList.get(currentNode).get(a)[0];
            int edgeWeight = adjacencyList.get(currentNode).get(a)[1];
            if (temporaryLabels.get(edgeTo).size() == 0 || permanentLabels[currentNode] + edgeWeight < temporaryLabels.get(edgeTo).get(temporaryLabels.get(edgeTo).size() - 1)) {
                temporaryLabels.get(edgeTo).add(permanentLabels[currentNode] + edgeWeight);
                pathsToEachNode[edgeTo] = pathsToEachNode[currentNode] + edgeTo;
            }
        }
        int smallest = findSmallestNonPermanentTemporaryLabel(temporaryLabels, orderLabels);
        permanentLabels[smallest] = temporaryLabels.get(smallest).get(temporaryLabels.get(smallest).size() - 1);
        orderLabels[smallest] = orderLabels[currentNode] + 1;
        if (permanentLabels[endNode] != -1) {
            return new DijkstraResult(pathsToEachNode[endNode], permanentLabels[endNode]);
        }
        return dijkstraRecursion(smallest, endNode, pathsToEachNode, orderLabels, permanentLabels, temporaryLabels);
    }

    public JarnikResult jarnik() {
        JarnikResult jarnikResult = new JarnikResult();
        ArrayList<Integer> includedNodes = new ArrayList<>();
        includedNodes.add(0);
        jarnikResult = jarnikRecursion(jarnikResult, includedNodes);
        return jarnikResult;
    }

    public JarnikResult jarnikRecursion(JarnikResult jarnikResult, ArrayList<Integer> includedNodes) {
        int[] smallestArc = new int[]{-1, -1, -1}; // {from,to,weight}
        for (int a = 0; a < includedNodes.size(); a++) {
            for (int b = 0; b < adjacencyList.get(a).size(); b++) {
                int edgeTo = adjacencyList.get(includedNodes.get(a)).get(b)[0];
                int edgeWeight = adjacencyList.get(includedNodes.get(a)).get(b)[1];
                if (!includedNodes.contains(edgeTo) && (smallestArc[0] == -1 || edgeWeight < smallestArc[2])) {
                    smallestArc[0] = includedNodes.get(a);
                    smallestArc[1] = edgeTo;
                    smallestArc[2] = edgeWeight;
                }
            }
        }
        jarnikResult.addEdge(smallestArc);
        includedNodes.add(smallestArc[1]);
        jarnikResult.addToTreeWeight(smallestArc[2]);
        if (includedNodes.size() < adjacencyList.size()) {
            return jarnikRecursion(jarnikResult, includedNodes);
        }
        return jarnikResult;
    }
}