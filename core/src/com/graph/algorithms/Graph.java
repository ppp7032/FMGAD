package com.graph.algorithms;

import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
    final ArrayList<ArrayList<int[]>> adjacencyList = new ArrayList<>();

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
        ArrayList<Integer> includedNodes=new ArrayList<>();
        includedNodes.add(0);
        jarnikResult = jarnikRecursion(jarnikResult,includedNodes);
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
        jarnikResult.minimumArcs.add(smallestArc);
        includedNodes.add(smallestArc[1]);
        jarnikResult.totalTreeWeight += smallestArc[2];
        if(includedNodes.size()<adjacencyList.size()){
            return jarnikRecursion(jarnikResult,includedNodes);
        }
        return jarnikResult;
    }
}