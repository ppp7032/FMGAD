package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Graph {

  private final ArrayList<ArrayList<int[]>> adjacencyList = new ArrayList<>();
  private final ArrayList<float[]> coordinates = new ArrayList<>();
  private boolean digraph;
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
      final ArrayList<ArrayList<Integer>> occurrences = new ArrayList<>(Arrays.asList(
          new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
          new ArrayList<>()));
      for (int a = 0; a < currentLine.length(); a++) {
        switch (currentLine.charAt(a)) {
          case '[':
            occurrences.getFirst().add(a);
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
      adjacencyList.add(new ArrayList<>());
      coordinates.add(new float[]{
          Float.parseFloat(currentLine.substring(occurrences.get(3).getFirst() + 1,
              occurrences.get(1).getFirst())),
          Float.parseFloat(currentLine.substring(occurrences.get(1).getFirst() + 1,
              occurrences.get(4).getFirst()))
      });
      for (int a = 0; a < occurrences.get(0).size(); a++) {
        adjacencyList.get(getNumberOfVertices() - 1).add(new int[]{
            Integer.parseInt(currentLine.substring(occurrences.get(0).get(a) + 1,
                occurrences.get(1).get(a + 1))),
            Integer.parseInt(
                currentLine.substring(occurrences.get(1).get(a + 1) + 1, occurrences.get(2).get(a)))
        });
      }
    }
    scanner.close();
    name = graph.nameWithoutExtension();
  }

  private static int findSmallestNonPermanentTemporaryLabel(
      final ArrayList<ArrayList<Integer>> temporaryLabels,
      final int[] orderLabels) {
    int smallest = -1;
    for (int a = 0; a < temporaryLabels.size(); a++) {
      if (!temporaryLabels.get(a).isEmpty() && orderLabels[a] == -1) {
        if (smallest == -1) {
          smallest = a;
        } else if (temporaryLabels.get(a).getLast() < temporaryLabels.get(smallest)
            .getLast()) {
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

  private static int getMinimumSpanningTreeSize(final ArrayList<int[]> minimumEdges) {
    int size = 0;
    for (int[] minimumEdge : minimumEdges) {
      size += minimumEdge[2];
    }
    return size;
  }

  public void nearestNeighbour(final int startVertex, final ArrayList<Integer> includedVertices) {
    int currentVertex = startVertex;
    Integer totalWeight = 0;
    includedVertices.clear();
    includedVertices.add(startVertex);
    while (true) {
      int smallestEdge = -1;
      int smallestVertex = -1;
      for (int a = 0; a < getNumberOfEdgesConnectedToVertex(currentVertex); a++) {
        if ((smallestVertex == -1 || getEdgeWeight(currentVertex, a) < smallestEdge)
            && !includedVertices.contains(getVertex(currentVertex, a))) {
          smallestEdge = getEdgeWeight(currentVertex, a);
          smallestVertex = getVertex(currentVertex, a);
        }
      }
      if (smallestVertex != -1) {
        includedVertices.add(smallestVertex);
        totalWeight += smallestEdge;
      } else if (includedVertices.size() == getNumberOfVertices()) {
        final int relativeVertexNumber = getRelativeVertexNumber(currentVertex, startVertex);
        if (relativeVertexNumber != -1) {
          smallestEdge = getEdgeWeight(currentVertex, relativeVertexNumber);
          includedVertices.add(startVertex);
          totalWeight += smallestEdge;
        } else {
          totalWeight = null;
        }
        break;
      } else {
        totalWeight = null;
        break;
      }
      currentVertex = smallestVertex;
    }
    includedVertices.add(totalWeight);
    //Final int is the weight, unless it stalled.
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
    for (int a = 0; a < getNumberOfVertices(); a++) {
      final StringBuilder line = new StringBuilder(
          "(" + coordinates.get(a)[0] + "," + coordinates.get(a)[1] + ")");
      for (int b = 0; b < getNumberOfEdgesConnectedToVertex(a); b++) {
        line.append(" [").append(getVertex(a, b)).append(",").append(getEdgeWeight(a, b))
            .append("]");
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
    adjacencyList.add(new ArrayList<>());
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

  public void clear(final boolean newStatus) {
    adjacencyList.clear();
    coordinates.clear();
    digraph = newStatus;
    FileHandle file = Gdx.files.local("graphs/New Graph.graph");
    int counter = 1;
    while (file.exists()) {
      file = Gdx.files.local("graphs/New Graph (" + counter + ").graph");
      counter++;
    }
    changeName(file.name().substring(0, file.name().lastIndexOf(".")));
  }

  public int getRelativeVertexNumber(final int vertex1, final int vertex2) {
    for (int a = 0; a < getNumberOfEdgesConnectedToVertex(vertex1); a++) {
      if (getVertex(vertex1, a) == vertex2) {
        return a;
      }
    }
    return -1;
  }

  public void setCoordinates(final int index, final float[] element) {
    coordinates.set(index, element);
  }

  public void updateDijkstraLabels(final Text[][] dijkstraLabels, final int[] orderLabels,
      final int[] permanentLabels, final ArrayList<ArrayList<Integer>> temporaryLabels) {
    for (int a = 0; a < getNumberOfVertices(); a++) {
      if (orderLabels[a] != -1) {
        dijkstraLabels[a][1].updateText(Integer.toString(orderLabels[a]));
        dijkstraLabels[a][2].updateText(Integer.toString(permanentLabels[a]));
      } else {
        dijkstraLabels[a][1].updateText("");
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

  private void dijkstraStep(final DijkstraContainer dijkstraContainer) {
    for (int a = 0; a < getNumberOfEdgesConnectedToVertex(dijkstraContainer.getCurrentVertex());
        a++) {
      final int edgeTo = getVertex(dijkstraContainer.getCurrentVertex(), a);
      final int edgeWeight = getEdgeWeight(dijkstraContainer.getCurrentVertex(), a);
      if (dijkstraContainer.getTemporaryLabels().get(edgeTo).isEmpty()
          || dijkstraContainer.getPermanentLabels()[dijkstraContainer.getCurrentVertex()]
          + edgeWeight < dijkstraContainer.getTemporaryLabels().get(edgeTo).getLast()) {
        dijkstraContainer.getTemporaryLabels().get(edgeTo).add(
            dijkstraContainer.getPermanentLabels()[dijkstraContainer.getCurrentVertex()]
                + edgeWeight);
        dijkstraContainer.getPathToVertex(edgeTo).clear();
        for (int b = 0;
            b < dijkstraContainer.getPathToVertex(dijkstraContainer.getCurrentVertex()).size();
            b++) {
          dijkstraContainer.getPathToVertex(edgeTo)
              .add(dijkstraContainer.getPathToVertex(dijkstraContainer.getCurrentVertex()).get(b));
        }
        dijkstraContainer.getPathToVertex(edgeTo).add(edgeTo);
      }
    }
    final int smallest = findSmallestNonPermanentTemporaryLabel(
        dijkstraContainer.getTemporaryLabels(), dijkstraContainer.getOrderLabels());
    if (smallest != -1) {
      dijkstraContainer.getPermanentLabels()[smallest] = dijkstraContainer.getTemporaryLabels()
          .get(smallest).getLast();
      dijkstraContainer.getOrderLabels()[smallest] =
          dijkstraContainer.getOrderLabels()[dijkstraContainer.getCurrentVertex()] + 1;
      dijkstraContainer.setCurrentVertex(smallest);
    }
  }

  public void dijkstraStep(final DijkstraContainer dijkstraContainer,
      final Text[][] dijkstraLabels) {
    dijkstraStep(dijkstraContainer);
    updateDijkstraLabels(dijkstraLabels, dijkstraContainer.getOrderLabels(),
        dijkstraContainer.getPermanentLabels(), dijkstraContainer.getTemporaryLabels());
  }

  public void jarnik(final ArrayList<int[]> minimumEdges, final int startVertex) {
    final ArrayList<Integer> includedVertices = new ArrayList<>();
    includedVertices.add(startVertex);
    while (includedVertices.size() < getNumberOfVertices()) {
      final int[] smallestEdge = new int[]{-1, -1, -1}; // {from,to,weight}
      for (int a = 0; a < includedVertices.size(); a++) {
        for (int b = 0; b < getNumberOfEdgesConnectedToVertex(includedVertices.get(a)); b++) {
          final int edgeTo = getVertex(includedVertices.get(a), b);
          final int edgeWeight = getEdgeWeight(includedVertices.get(a), b);
          if (!includedVertices.contains(edgeTo) && (smallestEdge[0] == -1
              || edgeWeight < smallestEdge[2])) {
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
      for (int a = 0; a < getNumberOfVertices(); a++) {
        for (int b = 0; b < getNumberOfEdgesConnectedToVertex(a); b++) {
          final int[] currentEdge = new int[]{a, getVertex(a, b), getEdgeWeight(a, b)};
          boolean found = false;
          for (int[] includedEdge : includedEdges) {
            if (includedEdge[0] == currentEdge[1] && includedEdge[1] == currentEdge[0]
                && includedEdge[2] == currentEdge[2]) {
              found = true;
              break;
            }
          }
          if (!found) {
            includedEdges.add(currentEdge);
          }
        }
      }
      includedEdges.sort(Comparator.comparingInt(ints -> ints[2]));
    }
    final int[] parents = new int[getNumberOfVertices()];
    Arrays.fill(parents, -1);
    for (int[] edge : includedEdges) {
      final int root0 = find(parents, edge[0]);
      final int root1 = find(parents, edge[1]);
      if (root0 != root1) {
        minimumEdges.add(edge);
        union(parents, root0, root1);
      }
      if (minimumEdges.size() == getNumberOfVertices() - 1) {
        break;
      }
    }
  }

  private int[] getOddVertices() {
    ArrayList<Integer> oddVertices = new ArrayList<>();
    for (int a = 0; a < getNumberOfVertices(); a++) {
      if (getNumberOfEdgesConnectedToVertex(a) % 2 == 1) {
        oddVertices.add(a);
      }
    }
    int[] toReturn = new int[oddVertices.size()];
    for (int a = 0; a < toReturn.length; a++) {
      toReturn[a] = oddVertices.get(a);
    }
    return toReturn;
  }

  private ArrayList<ArrayList<int[]>> getAllPairsOfList(int[] list) {
    ArrayList<ArrayList<int[]>> allPairs = new ArrayList<>();
    if (list.length >= 4) {
      for (int a = 1; a < list.length; a++) {
        int[] reducedList = new int[list.length - 2];
        int counter = 0;
        for (int b = 1; b < list.length; b++) {
          if (list[b] != list[a]) {
            reducedList[counter] = list[b];
            counter++;
          }
        }
        ArrayList<ArrayList<int[]>> reducedPairs = getAllPairsOfList(reducedList);
        for (ArrayList<int[]> reducedPair : reducedPairs) {
          reducedPair.add(new int[]{list[0], list[a]});
          allPairs.add(reducedPair);
        }
      }
    } else if (list.length == 2) {
      ArrayList<ArrayList<int[]>> toReturn = new ArrayList<>();
      ArrayList<int[]> one = new ArrayList<>();
      one.add(list);
      toReturn.add(one);
      return toReturn;
    }
    return allPairs;
  }

  public ArrayList<ArrayList<Integer>> routeInspection() {
    final int[] oddVertices = getOddVertices();
    if (oddVertices.length == 0) {
      return null;
    }
    final ArrayList<ArrayList<int[]>> allPairs = getAllPairsOfList(oddVertices);
    final ArrayList<ArrayList<ArrayList<Integer>>> allPaths = new ArrayList<>();
    final int[][] totalWeights = new int[allPairs.size()][2];
    for (int a = 0; a < allPairs.size(); a++) {
      totalWeights[a][0] = 0;
      totalWeights[a][1] = a;
      allPaths.add(new ArrayList<>());
      for (int b = 0; b < allPairs.get(a).size(); b++) {
        DijkstraContainer container = new DijkstraContainer(allPairs.get(a).get(b)[0],
            allPairs.get(a).get(b)[1], getNumberOfVertices());
        while (container.getOrderLabels()[container.getEndVertex()] == -1) {
          dijkstraStep(container);
        }
        allPaths.get(a).add(container.getPathToVertex(container.getEndVertex()));
        totalWeights[a][0] += container.getPermanentLabels()[container.getEndVertex()];
      }
    }
    java.util.Arrays.sort(totalWeights, Comparator.comparingInt(a -> a[0]));
    final ArrayList<ArrayList<Integer>> shortestPaths = allPaths.get(totalWeights[0][1]);
    final ArrayList<ArrayList<Integer>> repeatedEdges = new ArrayList<>();
    for (final ArrayList<Integer> shortestPath : shortestPaths) {
      for (int b = 0; b < shortestPath.size() - 1; b++) {
        repeatedEdges.add(
            new ArrayList<>(Arrays.asList(shortestPath.get(b), shortestPath.get(b + 1))));
      }
    }
    repeatedEdges.add(
        new ArrayList<>(Collections.singletonList(totalWeights[0][0] + getSumOfEdgeWeights())));
    return repeatedEdges;
  }

  public int getSumOfEdgeWeights() {
    int sum = 0;
    for (int a = 0; a < getNumberOfVertices(); a++) {
      for (int b = 0; b < getNumberOfEdgesConnectedToVertex(a); b++) {
        sum += getEdgeWeight(a, b);
      }
    }
    if (!isDigraph()) {
      sum /= 2;
    }
    return sum;
  }

  private boolean depthFirstSearch() {
    ArrayList<Integer> toVisit = new ArrayList<>();
    ArrayList<Integer> visited = new ArrayList<>();
    toVisit.add(0);
    while (!toVisit.isEmpty()) {
      int currentVertex = toVisit.removeLast();
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
      final Graph graph = new Graph(false);
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

  public void deleteVertex(final int vertex) {
    adjacencyList.remove(vertex);
    coordinates.remove(vertex);
    for (ArrayList<int[]> vertexEdges : adjacencyList) {
      for (int b = vertexEdges.size() - 1; b >= 0; b--) {
        if (vertexEdges.get(b)[0] == vertex) {
          vertexEdges.remove(b);
        } else if (vertexEdges.get(b)[0] > vertex) {
          vertexEdges.get(b)[0] -= 1;
        }
      }
    }
  }

  public Integer[] lowestBoundTSP() {
    final Graph[] graphs = new Graph[getNumberOfVertices()];
    final Integer[] lowerBounds = new Integer[graphs.length];
    for (int a = 0; a < graphs.length; a++) {
      lowerBounds[a] = 0;
      graphs[a] = new Graph(false);
      for (int b = 0; b < getNumberOfVertices(); b++) {
        graphs[a].addVertex(getXCoordinateOfVertex(b), getYCoordinateOfVertex(b));
      }
      for (int b = 0; b < getNumberOfVertices(); b++) {
        for (int c = 0; c < getNumberOfEdgesConnectedToVertex(b); c++) {
          final int toVertex = getVertex(b, c);
          graphs[a].addDirectedEdge(b, toVertex, getEdgeWeight(b, c));
        }
      }
    }
    for (int a = 0; a < graphs.length; a++) {
      if (graphs[a].getNumberOfEdgesConnectedToVertex(a) >= 2) {
        Integer smallestEdge = null;
        Integer secondSmallestEdge = null;
        for (int b = 0; b < graphs[a].getNumberOfEdgesConnectedToVertex(a); b++) {
          if (smallestEdge == null || graphs[a].getEdgeWeight(a, b) < smallestEdge) {
            if (smallestEdge != null) {
              secondSmallestEdge = smallestEdge;
            }
            smallestEdge = graphs[a].getEdgeWeight(a, b);
          } else if (secondSmallestEdge == null
              || graphs[a].getEdgeWeight(a, b) < secondSmallestEdge) {
            secondSmallestEdge = graphs[a].getEdgeWeight(a, b);
          }
        }
        lowerBounds[a] += smallestEdge + secondSmallestEdge;
        graphs[a].deleteVertex(a);
        final ArrayList<int[]> minimumEdges = new ArrayList<>();
        if (graphs[a].isConnected()) {
          graphs[a].kruskal(minimumEdges);
          lowerBounds[a] += getMinimumSpanningTreeSize(minimumEdges);
        } else {
          lowerBounds[a] = null;
        }
      } else {
        lowerBounds[a] = null;
      }
    }
    return lowerBounds;
  }

  public int getVertexFromInput(final String input) {
    int vertex = -1;
    if (input.length() == 1) {
      final char vertexChar = input.charAt(0);
      if (vertexChar >= 65 && vertexChar <= 90) {
        vertex = vertexChar - 65;
      } else if (vertexChar >= 97 && vertexChar <= 122) {
        vertex = vertexChar - 97;
      }
      if (vertex >= getNumberOfVertices()) {
        vertex = -1;
      }
    }
    return vertex;
  }
}