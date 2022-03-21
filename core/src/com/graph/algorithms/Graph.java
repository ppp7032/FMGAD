package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.*;

public class Graph {
    private final ArrayList<ArrayList<int[]>> adjacencyList = new ArrayList<>();
    private final ArrayList<float[]> coordinates = new ArrayList<>();
    private final boolean digraph;
    private String name;

    public Graph(final boolean digraph) {
        this.digraph = digraph;
    }

    public Graph(final FileHandle graph) {
        final Scanner scanner = new Scanner(graph.read());
        String currentLine = scanner.nextLine();
        int currentIndex = currentLine.indexOf('=');
        digraph = Boolean.parseBoolean(currentLine.substring(currentIndex + 1));
        while (scanner.hasNext()) {
            currentLine = scanner.nextLine();
            final ArrayList<ArrayList<Integer>> occurrences = new ArrayList<>(Arrays.asList(new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<Integer>()));
            for (int a = 0; a < currentLine.length(); a++) {
                switch (currentLine.charAt(a)) {
                    case '[':
                        occurrences.get(0).add(a);
                        break;
                    case ',':
                        occurrences.get(1).add(a);
                        break;
                    case ']':
                        occurrences.get(2).add(a);
                        break;
                    case '(':
                        occurrences.get(3).add(a);
                        break;
                    case ')':
                        occurrences.get(4).add(a);
                        break;
                }
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

    private static int find(final int[] parents, final int vertex) {
        if (parents[vertex] == -1) {
            return vertex;
        }
        return find(parents, parents[vertex]);
    }

    private static void union(final int[] parents, final int fromVertex, final int toVertex) {
        parents[fromVertex] = toVertex;
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
            for (int b = 0; b < getNumberOfEdgesConnectedToVertex(a); b++) {
                line.append(" [").append(getVertex(a, b)).append(",").append(getEdgeWeight(a, b)).append("]");
            }
            file.writeString("\n" + line, true);
        }
        changeName(fileName);
    }

    public boolean areTwoVerticesConnected(final int vertex1, final int vertex2) {
        for (int a = 0; a < getNumberOfEdgesConnectedToVertex(vertex1); a++) {
            if (getVertex(vertex1, a) == vertex2) {
                return true;
            }
        }
        return false;
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

    public int getNumberOfVertices() {
        return adjacencyList.size();
    }

    public float getXCoordinateOfVertex(final int a) {
        return coordinates.get(a)[0];
    }

    public float getYCoordinateOfVertex(final int a) {
        return coordinates.get(a)[1];
    }

    public int getNumberOfEdgesConnectedToVertex(final int a) {
        return adjacencyList.get(a).size();
    }

    public int getVertex(final int a, final int b) {
        return adjacencyList.get(a).get(b)[0];
    }

    public int getEdgeWeight(final int a, final int b) {
        return adjacencyList.get(a).get(b)[1];
    }

    public void addToEdgeWeights(ArrayList<EdgeWeight> edgeWeights, final float scaleFactor, final BitmapFont font) {
        for (int a = 0; a < getNumberOfVertices(); a++) {
            for (int b = 0; b < getNumberOfEdgesConnectedToVertex(a); b++) {
                edgeWeights.add(new EdgeWeight(this, a, getVertex(a, b), Integer.toString(getEdgeWeight(a, b)), font, new float[]{0, 0, 1, 1}, 0, 0, scaleFactor));
            }
        }
    }

    public void setCoordinates(final int index, final float[] element) {
        coordinates.set(index, element);
    }

    public void updateDijkstraLabels(final Text[][] dijkstraLabels, final int[] orderLabels, final int[] permanentLabels, final ArrayList<ArrayList<Integer>> temporaryLabels) {
        for (int a = 0; a < adjacencyList.size(); a++) {
            if (orderLabels[a] != -1) {
                dijkstraLabels[a][1].updateText(Integer.toString(orderLabels[a]));
            } else {
                dijkstraLabels[a][1].updateText("");
            }
            if (permanentLabels[a] != -1) {
                dijkstraLabels[a][2].updateText(Integer.toString(permanentLabels[a]));
            } else {
                dijkstraLabels[a][2].updateText("");
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

    public void dijkstraStep(final DijkstraContainer dijkstraContainer, final Text[][] dijkstraLabels) {
        for (int a = 0; a < getNumberOfEdgesConnectedToVertex(dijkstraContainer.currentVertex); a++) {
            final int edgeTo = getVertex(dijkstraContainer.currentVertex, a);
            final int edgeWeight = getEdgeWeight(dijkstraContainer.currentVertex, a);
            if (dijkstraContainer.temporaryLabels.get(edgeTo).size() == 0 || dijkstraContainer.permanentLabels[dijkstraContainer.currentVertex] + edgeWeight < dijkstraContainer.temporaryLabels.get(edgeTo).get(dijkstraContainer.temporaryLabels.get(edgeTo).size() - 1)) {
                dijkstraContainer.temporaryLabels.get(edgeTo).add(dijkstraContainer.permanentLabels[dijkstraContainer.currentVertex] + edgeWeight);
                dijkstraContainer.pathsToEachVertex[edgeTo] = dijkstraContainer.pathsToEachVertex[dijkstraContainer.currentVertex] + edgeTo;
            }
        }
        final int smallest = findSmallestNonPermanentTemporaryLabel(dijkstraContainer.temporaryLabels, dijkstraContainer.orderLabels);
        dijkstraContainer.permanentLabels[smallest] = dijkstraContainer.temporaryLabels.get(smallest).get(dijkstraContainer.temporaryLabels.get(smallest).size() - 1);
        dijkstraContainer.orderLabels[smallest] = dijkstraContainer.orderLabels[dijkstraContainer.currentVertex] + 1;
        dijkstraContainer.currentVertex = smallest;
        updateDijkstraLabels(dijkstraLabels, dijkstraContainer.orderLabels, dijkstraContainer.permanentLabels, dijkstraContainer.temporaryLabels);
    }

    public void jarnik(final ArrayList<int[]> minimumEdges, final int startVertex) {
        final ArrayList<Integer> includedVertices = new ArrayList<>();
        includedVertices.add(startVertex);
        while (includedVertices.size() < adjacencyList.size()) {
            final int[] smallestEdge = new int[]{-1, -1, -1}; // {from,to,weight}
            for (int a = 0; a < includedVertices.size(); a++) {
                for (int b = 0; b < getNumberOfEdgesConnectedToVertex(includedVertices.get(a)); b++) {
                    final int edgeTo = getVertex(includedVertices.get(a), b);
                    final int edgeWeight = getEdgeWeight(includedVertices.get(a), b);
                    if (!includedVertices.contains(edgeTo) && (smallestEdge[0] == -1 || edgeWeight < smallestEdge[2])) {
                        smallestEdge[0] = includedVertices.get(a);
                        smallestEdge[1] = edgeTo;
                        smallestEdge[2] = edgeWeight;
                    }
                }
            }
            minimumEdges.add(smallestEdge);
            includedVertices.add(smallestEdge[1]);
        }
    }

    public void kruskal(final ArrayList<int[]> minimumEdges) {
        final ArrayList<int[]> includedEdges = new ArrayList<>();
        {
            for (int a = 0; a < adjacencyList.size(); a++) {
                for (int b = 0; b < adjacencyList.get(a).size(); b++) {
                    final int[] currentEdge = new int[]{a, getVertex(a, b), getEdgeWeight(a, b)};
                    boolean found = false;
                    for (int[] includedEdge : includedEdges) {
                        if (includedEdge[0] == currentEdge[1] && includedEdge[1] == currentEdge[0] && includedEdge[2] == currentEdge[2]) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        includedEdges.add(currentEdge);
                    }
                }
            }
            Collections.sort(includedEdges, new Comparator<int[]>() {
                public int compare(int[] first, int[] second) {
                    return -1 * Integer.compare(first[2], second[2]);
                }
            });
        }
        while (minimumEdges.size() < adjacencyList.size() - 1) {
            minimumEdges.add(includedEdges.get(includedEdges.size() - 1));
            //System.out.println("adding " + minimumEdges.get(minimumEdges.size() - 1)[0] + " and " + minimumEdges.get(minimumEdges.size() - 1)[1]);
            includedEdges.remove(includedEdges.size() - 1);
            if (cycleDetection(minimumEdges)) {
                //System.out.println("cycle when adding " + minimumEdges.get(minimumEdges.size() - 1)[0] + " and " + minimumEdges.get(minimumEdges.size() - 1)[1]);
                minimumEdges.remove(minimumEdges.size() - 1);
            }
        }
    }

    private boolean cycleDetection(final ArrayList<int[]> minimumEdges) {
        final int[] parents = new int[adjacencyList.size()];
        for (int a = 0; a < adjacencyList.size(); a++) {
            parents[a] = -1;
        }
        for (int[] minimumEdge : minimumEdges) {
            final int fromVertex = find(parents, minimumEdge[0]);
            final int toVertex = find(parents, minimumEdge[1]);
            if (fromVertex == toVertex) {
                return true;
            }
            union(parents, fromVertex, toVertex);
        }
        return false;
    }

    public boolean depthFirstSearch() {
        ArrayList<Integer> toVisit = new ArrayList<>();
        ArrayList<Integer> visited = new ArrayList<>();
        toVisit.add(0);
        while (toVisit.size() > 0) {
            int currentVertex = toVisit.remove(toVisit.size() - 1);
            if (!visited.contains(currentVertex)) {
                visited.add(currentVertex);
                for (int a = 0; a < getNumberOfEdgesConnectedToVertex(currentVertex); a++) {
                    toVisit.add(getVertex(currentVertex, a));
                }
            }
        }
        return visited.size() == getNumberOfVertices();
    }

    public boolean isConnected() {
        if (digraph) {
            final Graph graph = new Graph(true);
            for (int a = 0; a < getNumberOfVertices(); a++) {
                graph.addVertex(getXCoordinateOfVertex(a), getYCoordinateOfVertex(a));
            }
            for (int a = 0; a < getNumberOfVertices(); a++) {
                for (int b = 0; b < getNumberOfEdgesConnectedToVertex(a); b++) {
                    final int toVertex = getVertex(a, b);
                    graph.addUndirectedEdge(a, toVertex, getEdgeWeight(a, b));
                }
            }
            return graph.depthFirstSearch();
        } else {
            return depthFirstSearch();
        }
    }
}