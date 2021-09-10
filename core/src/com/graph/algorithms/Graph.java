package com.graph.algorithms;

import java.util.ArrayList;
import java.util.Scanner;
import com.badlogic.gdx.files.FileHandle;

public class Graph {
    final ArrayList<ArrayList<Integer>> adjacencyMatrix;

    public Graph() {
        adjacencyMatrix = new ArrayList<>();
    }

    public Graph(FileHandle graph) {
        Scanner scanner = new Scanner(graph.read());
        adjacencyMatrix = new ArrayList<>();
        while (scanner.hasNext()) {
            ArrayList<Integer> currentList = new ArrayList<>();
            String currentLine = scanner.nextLine();
            StringBuilder currentValue = new StringBuilder();
            for (int a = 0; a < currentLine.length(); a++) {
                char currentCharacter = currentLine.charAt(a);
                if (currentCharacter == ",".toCharArray()[0]) {
                    if (currentValue.toString().equals("&")) {
                        currentList.add(null);
                    }
                    else {
                        currentList.add(Integer.valueOf(currentValue.toString()));
                    }
                    currentValue = new StringBuilder();
                }
                else {
                    currentValue.append(currentCharacter);
                }
            }
            adjacencyMatrix.add(currentList);
        }
    }

    public DijkstraResult dijkstra(int startNode, int endNode) {
        String[] pathsToEachNode = new String[adjacencyMatrix.size()];
        pathsToEachNode[startNode] = Integer.toString(startNode);
        int[] orderLabels = new int[adjacencyMatrix.size()];
        int[] permanentLabels = new int[adjacencyMatrix.size()];
        ArrayList<ArrayList<Integer>> temporaryLabels = new ArrayList<>();
        for (int a = 0; a < adjacencyMatrix.size(); a++) {
            permanentLabels[a] = -1;
            orderLabels[a] = -1;
            temporaryLabels.add(new ArrayList<Integer>());
        }
        orderLabels[startNode] = 1;
        permanentLabels[startNode] = 0;
        return dijkstraNode(startNode, endNode, pathsToEachNode, orderLabels, permanentLabels, temporaryLabels);
    }

    public DijkstraResult dijkstraNode(int currentNode, int endNode, String[] pathsToEachNode, int[] orderLabels,
                                       int[] permanentLabels, ArrayList<ArrayList<Integer>> temporaryLabels) {
        for (int a = 0; a < adjacencyMatrix.size(); a++) {
            if (adjacencyMatrix.get(currentNode).get(a) != null && (temporaryLabels.get(a).size() == 0 || permanentLabels[currentNode] + adjacencyMatrix.get(currentNode).get(a) < temporaryLabels.get(a).get(temporaryLabels.get(a).size() - 1))) {
                temporaryLabels.get(a).add(permanentLabels[currentNode] + adjacencyMatrix.get(currentNode).get(a));
                pathsToEachNode[a] = pathsToEachNode[currentNode] + a;
            }
        }
        int smallest = findSmallestNonPermanentTemporaryLabel(temporaryLabels, orderLabels);
        permanentLabels[smallest] = temporaryLabels.get(smallest).get(temporaryLabels.get(smallest).size() - 1);
        orderLabels[smallest] = orderLabels[currentNode] + 1;

        if (permanentLabels[endNode] != -1) {
            return new DijkstraResult(pathsToEachNode[endNode], permanentLabels[endNode]);
        }
        return dijkstraNode(smallest, endNode, pathsToEachNode, orderLabels, permanentLabels, temporaryLabels);
    }

    public static int findSmallestNonPermanentTemporaryLabel(ArrayList<ArrayList<Integer>> temporaryLabels,
                                                             int[] orderLabels) {
        int smallest = -1;
        for (int a = 0; a < temporaryLabels.size(); a++) {
            if (temporaryLabels.get(a).size() != 0 && orderLabels[a] == -1) {
                if (smallest == -1) {
                    smallest = a;
                }
                else if (temporaryLabels.get(a).get(temporaryLabels.get(a).size() - 1) < temporaryLabels.get(smallest)
                        .get(temporaryLabels.get(smallest).size() - 1)) {
                    smallest = a;
                }
            }
        }
        return smallest;
    }

    public JarnikResult jarnik() {
        JarnikResult jarnikResult = new JarnikResult();
        jarnikResult.includedNodes.add(0);
        while (jarnikResult.includedNodes.size() < adjacencyMatrix.size()) {
            jarnikResult = jarnikNode(jarnikResult);
        }
        return jarnikResult;
    }

    public JarnikResult jarnikNode(JarnikResult jarnikResult) {
        int[] smallestArc = new int[] { -1, -1 }; // {from,to}
        for (int a = 0; a < jarnikResult.includedNodes.size(); a++) {
            for (int b = 0; b < adjacencyMatrix.size(); b++) {
                if (adjacencyMatrix.get(jarnikResult.includedNodes.get(a)).get(b) != null && !jarnikResult.includedNodes.contains(b) && (smallestArc[0] == -1 || adjacencyMatrix.get(jarnikResult.includedNodes.get(a)).get(b) < adjacencyMatrix.get(smallestArc[0]).get(smallestArc[1]))) {
                    smallestArc[0] = jarnikResult.includedNodes.get(a);
                    smallestArc[1] = b;
                }
            }
        }
        jarnikResult.minimumArcs.add(smallestArc);
        jarnikResult.includedNodes.add(smallestArc[1]);
        jarnikResult.totalTreeWeight += adjacencyMatrix.get(smallestArc[0]).get(smallestArc[1]);
        return jarnikResult;
    }
}