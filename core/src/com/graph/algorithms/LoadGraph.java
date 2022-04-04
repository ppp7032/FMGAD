package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class LoadGraph implements Screen {
    private final Stage stage = new Stage();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final float scaleFactor = Graphics.findScaleFactor();
    private final Graph graph;
    private final ArrayList<EdgeWeight> edgeWeights = new ArrayList<>();
    private final Text[][] dijkstraLabels;
    private final ArrayList<Text> vertexLabels = new ArrayList<>();
    private final TextField startVertexInput;
    private final TextField endVertexInput;
    private final ArrayList<int[]> minimumEdges = new ArrayList<>();
    private final ArrayList<Integer> nearestNeighbourPath = new ArrayList<>();
    private final Text alertMessage;
    private boolean dijkstraPressed = false;
    private boolean dijkstraApplied = false;
    private DijkstraContainer dijkstraContainer = new DijkstraContainer();
    private boolean jarnikPressed = false;
    private boolean jarnikApplied = false;
    private int counter = 1;
    private boolean kruskalApplied = false;
    private boolean chinesePostmanPressed = false;
    private boolean everRanRouteInspection = false;
    private boolean travellingSalesmanPressed = false;
    private boolean displayingLowerBounds = false;
    private boolean everRanLowerBounds = false;
    private boolean nearestNeighbourPressed = false; //todo: make it not generate the same font every 2 seconds.
    private boolean nearestNeighbourApplied = false;
    private boolean alertShowing;

    public LoadGraph(final Graph graph) { //To-do: make it not make two edgeWeights for every edge on an undirected graph.
        this.graph = graph;
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        alertMessage = new Text("Nearest Neighbour\nhas stalled!", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, twenty, new float[]{0, 0, 0, 1}, 0, 0, -1);
        graph.addToEdgeWeights(edgeWeights, scaleFactor, twenty);
        dijkstraLabels = new Text[graph.getNumberOfVertices()][4];
        final Skin skin = Graphics.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 15f * scaleFactor, 0));
        startVertexInput = new TextField("0", skin);
        endVertexInput = new TextField("0", skin);
        GeneralConstructor(twenty, skin);
    }

    public LoadGraph(final Graph graph, final ArrayList<EdgeWeight> edgeWeights, final ArrayList<Text> vertexLabels) {
        this.graph = graph;
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        alertMessage = new Text("Nearest Neighbour\nhas stalled!", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, twenty, new float[]{0, 0, 0, 1}, 0, 0, -1);
        this.edgeWeights.addAll(edgeWeights);
        dijkstraLabels = new Text[graph.getNumberOfVertices()][4];
        this.vertexLabels.addAll(vertexLabels);
        final Skin skin = Graphics.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 15f * scaleFactor, 0));
        startVertexInput = new TextField("0", skin);
        endVertexInput = new TextField("0", skin);
        GeneralConstructor(twenty, skin);
    }

    private void GeneralConstructor(final BitmapFont twenty, Skin skin) {
        final TextButton dijkstraButton = new TextButton("Dijsktra's", skin, "default");
        final TextButton jarnikButton = new TextButton("Jarník's", skin, "default");
        final TextButton kruskalButton = new TextButton("Kruskal's", skin, "default");
        final TextButton routeInspectionButton = new TextButton("C. Postman", skin, "default");
        final TextButton travellingSalesmanButton = new TextButton("T. Salesman", skin, "default");
        final SelectBox<String> selectTSPAlgorithm = new SelectBox<>(Graphics.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 12f * scaleFactor, 0)), "default");
        final TextButton mainMenu = new TextButton("Main Menu", skin, "default");
        final TextButton edit = new TextButton("Edit", skin, "default");
        final TextButton[] sidePanelButtons = new TextButton[]{mainMenu, edit, dijkstraButton, jarnikButton, kruskalButton, routeInspectionButton, travellingSalesmanButton};
        final Text menuTitle = new Text("Dijkstra's Algorithm Options", Gdx.graphics.getWidth() / 2f, 545 * scaleFactor, Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 25f * scaleFactor, 0), new float[]{0, 0, 0, 1}, 0, 0, -1);
        final float y1 = 491.5f;
        final Text startVertexLabel = new Text("Start Vertex", 430f * scaleFactor, y1 * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0, -1);
        final Text endVertexLabel = new Text("End Vertex", 430f * scaleFactor, (y1 - 61f) * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0, -1);
        final Skin buttonSkin = Graphics.generateSkin(twenty);
        final TextButton back = new TextButton("Back", buttonSkin, "default");
        final TextButton apply = new TextButton("Apply", buttonSkin, "default");
        final BitmapFont small = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 10f * scaleFactor, 0);
        final BitmapFont medium = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 15f * scaleFactor, 0);
        final List<String> list = new List<>(skin);
        final ArrayList<ArrayList<String>> routeInspectionItems = new ArrayList<>();
        routeInspectionItems.add(new ArrayList<String>());
        final ArrayList<ArrayList<String>> lowerBoundTSPItems = new ArrayList<>();
        lowerBoundTSPItems.add(new ArrayList<String>());
        final ScrollPane scrollBar = new ScrollPane(list, skin, "default");
        for (int a = 0; a < graph.getNumberOfVertices(); a++) {
            final float[] dimensions = Graphics.setupDijkstraBoxes(scaleFactor, graph, a);
            dijkstraLabels[a] = new Text[]{new Text(Character.toString((char) (a + 65)), dimensions[0] + dimensions[2] / 6f, dimensions[1] + dimensions[3] / 4f * 3f, medium, new float[]{0, 0, 0, 1}, 0, 0, 31 * scaleFactor), new Text("", dimensions[0] + dimensions[2] / 2f, dimensions[1] + dimensions[3] / 4f * 3f, medium, new float[]{0, 0, 0, 1}, 0, 0, 31 * scaleFactor), new Text("", dimensions[0] + dimensions[2] / 6f * 5f, dimensions[1] + dimensions[3] / 4f * 3f, medium, new float[]{0, 0, 0, 1}, 0, 0, 31 * scaleFactor), new Text("", dimensions[0] + 5f * scaleFactor, dimensions[1] + dimensions[3] / 4f, small, new float[]{0, 0, 0, 1}, -1, 0, dimensions[2] - 10f * scaleFactor)};
            for (int b = 0; b < 4; b++) {
                dijkstraLabels[a][b].setVisible(false);
            }
            vertexLabels.add(new Text(Character.toString((char) (a + 65)), graph.getXCoordinateOfVertex(a) * scaleFactor, graph.getYCoordinateOfVertex(a) * scaleFactor, twenty, new float[]{0, 0, 0, 1}, 0, 0, -1));
        }


        dijkstraButton.setWidth(127 * scaleFactor);
        dijkstraButton.setHeight(46 * scaleFactor);
        dijkstraButton.setPosition(80f * scaleFactor - dijkstraButton.getWidth() / 2f, 652f * scaleFactor);

        Graphics.setupButtonBelow(dijkstraButton, jarnikButton, scaleFactor);

        Graphics.setupButtonBelow(jarnikButton, kruskalButton, scaleFactor);

        Graphics.setupButtonBelow(kruskalButton, routeInspectionButton, scaleFactor);

        Graphics.setupButtonBelow(routeInspectionButton, travellingSalesmanButton, scaleFactor);

        selectTSPAlgorithm.setX((757 - 50) * scaleFactor);
        selectTSPAlgorithm.setY(479 * scaleFactor);
        selectTSPAlgorithm.setWidth((88 + 50) * scaleFactor);
        selectTSPAlgorithm.setHeight(24 * scaleFactor);
        selectTSPAlgorithm.setItems("Nearest Neighbour", "Lower Bound");
        selectTSPAlgorithm.setVisible(false);

        Graphics.setupBottomTwoButtons(mainMenu, edit, scaleFactor);

        menuTitle.setVisible(false);

        startVertexLabel.setVisible(false);

        endVertexLabel.setVisible(false);

        startVertexInput.setVisible(false);
        startVertexInput.setAlignment(1);
        startVertexInput.setX(757 * scaleFactor);
        startVertexInput.setY(479 * scaleFactor);
        startVertexInput.setWidth(88 * scaleFactor);
        startVertexInput.setHeight(24 * scaleFactor);

        endVertexInput.setVisible(false);
        endVertexInput.setAlignment(1);
        endVertexInput.setX(757 * scaleFactor);
        endVertexInput.setY(startVertexInput.getY() - 61 * scaleFactor);
        endVertexInput.setWidth(88 * scaleFactor);
        endVertexInput.setHeight(24 * scaleFactor);

        scrollBar.setX(414f * scaleFactor);
        scrollBar.setY(224f * scaleFactor);
        scrollBar.setHeight(290f * scaleFactor);
        scrollBar.setWidth(452f * scaleFactor);
        scrollBar.setVisible(false);

        Graphics.setupBackAndApplyButtons(back, apply, scaleFactor, false);

        alertMessage.setVisible(false);


        dijkstraButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (dijkstraApplied) {
                    dijkstraApplied = false;
                    dijkstraContainer.setup = false;
                    for (int a = 0; a < graph.getNumberOfVertices(); a++) {
                        for (int b = 0; b < 4; b++) {
                            dijkstraLabels[a][b].setVisible(false);
                        }
                    }
                    toggleButtonTouchableStatus(sidePanelButtons, Touchable.enabled);
                    dijkstraButton.setText("Dijkstra's");
                    //toggleButtonTouchability(sidePanelButtons);
                } else if (!dijkstraPressed) {
                    dijkstraPressed = true;
                    toggleDijkstraPressed(menuTitle, startVertexLabel, endVertexLabel, back, apply);
                    toggleButtonTouchableStatus(sidePanelButtons, Touchable.disabled);
                }
            }
        });
        jarnikButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!graph.isDigraph()) {
                    if (jarnikApplied) {
                        jarnikApplied = false;
                        toggleButtonTouchableStatus(sidePanelButtons, Touchable.enabled);
                        jarnikButton.setText("Jarník's");
                    } else if (!jarnikPressed) {
                        jarnikPressed = true;
                        toggleJarnikPressed(menuTitle, startVertexLabel, back, apply);
                        toggleButtonTouchableStatus(sidePanelButtons, Touchable.disabled);
                    }
                }
            }
        });
        kruskalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!graph.isDigraph()) {
                    kruskalApplied = !kruskalApplied;
                    if (kruskalApplied) {
                        minimumEdges.clear();
                        counter = 1;
                        graph.kruskal(minimumEdges);
                        toggleButtonTouchableStatus(sidePanelButtons, Touchable.disabled);
                        mainMenu.setTouchable(Touchable.enabled);
                        edit.setTouchable(Touchable.enabled);
                        kruskalButton.setTouchable(Touchable.enabled);
                        kruskalButton.setText("Back");
                    } else {
                        toggleButtonTouchableStatus(sidePanelButtons, Touchable.enabled);
                        kruskalButton.setText("Kruskal's");
                    }
                }
            }
        });
        routeInspectionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!graph.isDigraph()) {
                    chinesePostmanPressed = true;
                    toggleChinesePostman(menuTitle, scrollBar, back);
                    toggleButtonTouchableStatus(sidePanelButtons, Touchable.disabled);
                    if (!everRanRouteInspection) {
                        final ArrayList<ArrayList<Integer>> repeatedEdges = graph.routeInspection();
                        if (repeatedEdges != null) {
                            final String[] items = new String[repeatedEdges.size()];
                            items[0] = "Total Weight: " + repeatedEdges.get(repeatedEdges.size() - 1).get(0);
                            for (int a = 1; a < items.length; a++) {
                                final ArrayList<Integer> numbers = repeatedEdges.get(a - 1);
                                final char[] numbersChar = {(char) (numbers.get(0) + 65), (char) (numbers.get(1) + 65)};
                                items[a] = new String(numbersChar);
                            }
                            list.setItems(items);
                        } else {
                            list.setItems("Total Weight: " + graph.getSumOfEdgeWeights());
                        }
                        everRanRouteInspection = true;
                        routeInspectionItems.set(0, Graphics.getItems(list));
                    } else {
                        list.setItems(routeInspectionItems.get(0).toArray(new String[0]));
                    }
                }
            }
        });
        travellingSalesmanButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!graph.isDigraph()) {
                    if (!nearestNeighbourApplied) {
                        travellingSalesmanPressed = true;
                        toggleTravellingSalesman(menuTitle, startVertexLabel, selectTSPAlgorithm, back, apply);
                        toggleButtonTouchableStatus(sidePanelButtons, Touchable.disabled);
                    } else {
                        travellingSalesmanButton.setText("T. Salesman");
                        nearestNeighbourApplied = false;
                        alertShowing = false;
                        alertMessage.setVisible(false);
                        toggleButtonTouchableStatus(sidePanelButtons, Touchable.enabled);
                    }
                }
            }
        });
        mainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });
        edit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new NewGraph(graph, edgeWeights, vertexLabels));
            }
        });
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (dijkstraPressed) {
                    dijkstraPressed = false;
                    toggleDijkstraPressed(menuTitle, startVertexLabel, endVertexLabel, back, apply);
                    toggleButtonTouchableStatus(sidePanelButtons, Touchable.enabled);
                } else if (jarnikPressed) {
                    jarnikPressed = false;
                    toggleJarnikPressed(menuTitle, startVertexLabel, back, apply);
                    toggleButtonTouchableStatus(sidePanelButtons, Touchable.enabled);
                } else if (chinesePostmanPressed) {
                    chinesePostmanPressed = false;
                    toggleChinesePostman(menuTitle, scrollBar, back);
                    toggleButtonTouchableStatus(sidePanelButtons, Touchable.enabled);
                } else if (displayingLowerBounds) {
                    displayingLowerBounds = false;
                    travellingSalesmanPressed = true;
                    toggleLowerBounds(menuTitle, scrollBar, back, apply);
                    toggleTravellingSalesman(menuTitle, startVertexLabel, selectTSPAlgorithm, back, apply);
                } else if (nearestNeighbourPressed) {
                    toggleNearestNeighbourPressed(menuTitle, startVertexLabel, back, apply);
                    toggleTravellingSalesman(menuTitle, startVertexLabel, selectTSPAlgorithm, back, apply);
                    nearestNeighbourPressed = false;
                    travellingSalesmanPressed = true;
                } else if (travellingSalesmanPressed) {
                    travellingSalesmanPressed = false;
                    toggleTravellingSalesman(menuTitle, startVertexLabel, selectTSPAlgorithm, back, apply);
                    toggleButtonTouchableStatus(sidePanelButtons, Touchable.enabled);
                }
            }
        });
        apply.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (nearestNeighbourPressed) {
                    toggleNearestNeighbourPressed(menuTitle, startVertexLabel, back, apply);
                    travellingSalesmanButton.setTouchable(Touchable.enabled);
                    mainMenu.setTouchable(Touchable.enabled);
                    edit.setTouchable(Touchable.enabled);
                    travellingSalesmanButton.setText("Back");
                    nearestNeighbourPressed = false;
                    nearestNeighbourApplied = true;
                    graph.nearestNeighbour(startVertexInput.getText().charAt(0) - 65, nearestNeighbourPath);
                    counter = 1;
                } else if (travellingSalesmanPressed) {
                    travellingSalesmanPressed = false;
                    if (selectTSPAlgorithm.getSelectedIndex() == 1) {
                        displayingLowerBounds = true;
                        toggleTravellingSalesman(menuTitle, startVertexLabel, selectTSPAlgorithm, back, apply);
                        toggleLowerBounds(menuTitle, scrollBar, back, apply);
                        if (!everRanLowerBounds) {
                            final int[] lowestBounds = graph.lowestBoundTSP();
                            final String[] items = new String[lowestBounds.length];
                            for (int a = 0; a < lowestBounds.length; a++) {
                                items[a] = String.valueOf(lowestBounds[a]);
                            }
                            list.setItems(items);
                            lowerBoundTSPItems.set(0, Graphics.getItems(list));
                            everRanLowerBounds = true;
                        } else {
                            list.setItems(lowerBoundTSPItems.get(0).toArray(new String[0]));
                        }
                    } else {
                        nearestNeighbourPressed = true;
                        travellingSalesmanPressed = false;
                        toggleTravellingSalesman(menuTitle, startVertexLabel, selectTSPAlgorithm, back, apply);
                        toggleNearestNeighbourPressed(menuTitle, startVertexLabel, back, apply);
                    }
                } else if (dijkstraPressed) {
                    dijkstraPressed = false;
                    dijkstraApplied = true;
                    for (int a = 0; a < graph.getNumberOfVertices(); a++) {
                        for (int b = 0; b < 4; b++) {
                            dijkstraLabels[a][b].setVisible(true);
                        }
                    }
                    toggleDijkstraPressed(menuTitle, startVertexLabel, endVertexLabel, back, apply);
                    dijkstraButton.setTouchable(Touchable.enabled);
                    mainMenu.setTouchable(Touchable.enabled);
                    edit.setTouchable(Touchable.enabled);
                    dijkstraButton.setText("Back");
                } else if (jarnikPressed) {
                    jarnikPressed = false;
                    jarnikApplied = true;
                    minimumEdges.clear();
                    counter = 1;
                    graph.jarnik(minimumEdges, startVertexInput.getText().charAt(0) - 65);
                    toggleJarnikPressed(menuTitle, startVertexLabel, back, apply);
                    jarnikButton.setTouchable(Touchable.enabled);
                    mainMenu.setTouchable(Touchable.enabled);
                    edit.setTouchable(Touchable.enabled);
                    jarnikButton.setText("Back");
                }
            }
        });


        stage.addActor(dijkstraButton);
        stage.addActor(jarnikButton);
        stage.addActor(kruskalButton);
        stage.addActor(routeInspectionButton);
        stage.addActor(travellingSalesmanButton);
        stage.addActor(selectTSPAlgorithm);
        stage.addActor(mainMenu);
        stage.addActor(edit);
        stage.addActor(menuTitle);
        stage.addActor(startVertexLabel);
        stage.addActor(endVertexLabel);
        stage.addActor(startVertexInput);
        stage.addActor(endVertexInput);
        stage.addActor(back);
        stage.addActor(apply);
        stage.addActor(scrollBar);
        for (int a = 0; a < graph.getNumberOfVertices(); a++) {
            for (int b = 0; b < 4; b++) {
                stage.addActor(dijkstraLabels[a][b]);
            }
        }
        stage.addActor(alertMessage);
    }

    private void toggleButtonTouchableStatus(final TextButton[] buttons, final Touchable status) {
        for (TextButton button : buttons) {
            button.setTouchable(status);
        }
    }

    private void toggleDijkstraPressed(final Text menuTitle, final Text startVertexLabel, final Text endVertexLabel, final TextButton back, final TextButton apply) {
        final boolean newStatus = !menuTitle.isVisible();
        if (newStatus) {
            menuTitle.updateText("Dijkstra's Algorithm Options");
            startVertexLabel.updateText("Start Vertex");
            endVertexLabel.updateText("End Vertex");
            startVertexInput.setText("A");
            endVertexInput.setText("A");
        }
        menuTitle.setVisible(newStatus);
        startVertexInput.setVisible(newStatus);
        startVertexLabel.setVisible(newStatus);
        endVertexInput.setVisible(newStatus);
        endVertexLabel.setVisible(newStatus);
        back.setVisible(newStatus);
        apply.setVisible(newStatus);
    }

    private void toggleJarnikPressed(final Text menuTitle, final Text startVertexLabel, final TextButton back, final TextButton apply) {
        final boolean newStatus = !menuTitle.isVisible();
        if (newStatus) {
            menuTitle.updateText("Jarník's Algorithm Options");
            startVertexLabel.updateText("Start Vertex");
            startVertexInput.setText("A");
        }
        menuTitle.setVisible(newStatus);
        startVertexInput.setVisible(newStatus);
        startVertexLabel.setVisible(newStatus);
        back.setVisible(newStatus);
        apply.setVisible(newStatus);
    }

    private void toggleChinesePostman(final Text menuTitle, final ScrollPane scrollBar, final TextButton back) {
        final boolean newStatus = !menuTitle.isVisible();
        if (newStatus) {
            menuTitle.updateText("Chinese Postman Repeated Edges:");
        }
        menuTitle.setVisible(newStatus);
        scrollBar.setVisible(newStatus);
        back.setVisible(newStatus);
    }

    private void toggleTravellingSalesman(final Text menuTitle, final Text startVertexLabel, final SelectBox<String> selector, final TextButton back, final TextButton apply) {
        final boolean newStatus = !menuTitle.isVisible();
        if (newStatus) {
            menuTitle.updateText("Travelling Salesman Problem");
            startVertexLabel.updateText("Algorithm");
            selector.setSelectedIndex(0);
        }
        menuTitle.setVisible(newStatus);
        startVertexLabel.setVisible(newStatus);
        selector.setVisible(newStatus);
        back.setVisible(newStatus);
        apply.setVisible(newStatus);
    }

    private void toggleLowerBounds(final Text menuTitle, final ScrollPane scrollBar, final TextButton back, final TextButton apply) {
        final boolean newStatus = !menuTitle.isVisible();
        if (newStatus) {
            menuTitle.updateText("Lower Bounds:");
        }
        menuTitle.setVisible(newStatus);
        scrollBar.setVisible(newStatus);
        back.setVisible(newStatus);
    }

    private void toggleNearestNeighbourPressed(final Text menuTitle, final Text startVertexLabel, final TextButton back, final TextButton apply) {
        final boolean newStatus = !menuTitle.isVisible();
        if (newStatus) {
            menuTitle.updateText("Nearest Neighbour Options");
            startVertexLabel.updateText("Start Vertex");
            startVertexInput.setText("A");
        }
        menuTitle.setVisible(newStatus);
        startVertexInput.setVisible(newStatus);
        startVertexLabel.setVisible(newStatus);
        back.setVisible(newStatus);
        apply.setVisible(newStatus);
    }

    private void drawMST(boolean condition) {
        if (condition) {
            for (int a = 0; a < counter; a++) {
                Graphics.renderEdge(graph.getXCoordinateOfVertex(minimumEdges.get(a)[0]), graph.getYCoordinateOfVertex(minimumEdges.get(a)[0]), graph.getXCoordinateOfVertex(minimumEdges.get(a)[1]), graph.getYCoordinateOfVertex(minimumEdges.get(a)[1]), shapeRenderer, false, scaleFactor);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && counter < graph.getNumberOfVertices() - 1) {
                counter++;
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(247f / 255f, 247f / 255f, 247f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Graphics.renderGraphEdges(shapeRenderer, graph, scaleFactor);
        if (dijkstraApplied && dijkstraContainer.setup && dijkstraContainer.permanentLabels[dijkstraContainer.endVertex] != -1) {
            ArrayList<Integer> path = dijkstraContainer.pathsToEachVertex.get(dijkstraContainer.endVertex);
            shapeRenderer.setColor(0, 1, 0, 1);
            for (int a = 1; a < path.size(); a++) {
                final int vertex1 = path.get(a - 1);
                final int vertex2 = path.get(a);
                Graphics.renderEdge(graph.getXCoordinateOfVertex(vertex1), graph.getYCoordinateOfVertex(vertex1), graph.getXCoordinateOfVertex(vertex2), graph.getYCoordinateOfVertex(vertex2), shapeRenderer, graph.isDigraph(), scaleFactor);
            }
        } else if (nearestNeighbourApplied) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && counter < nearestNeighbourPath.size() - 2) {
                counter++;
            }
            if (!alertShowing && counter == nearestNeighbourPath.size() - 2 && nearestNeighbourPath.get(nearestNeighbourPath.size() - 1) == -1) {
                alertShowing = true;
                alertMessage.setVisible(true);
            }
            shapeRenderer.setColor(0, 1, 0, 1);
            for (int a = 1; a <= counter; a++) {
                final int vertex1 = nearestNeighbourPath.get(a - 1);
                final int vertex2 = nearestNeighbourPath.get(a);
                Graphics.renderEdge(graph.getXCoordinateOfVertex(vertex1), graph.getYCoordinateOfVertex(vertex1), graph.getXCoordinateOfVertex(vertex2), graph.getYCoordinateOfVertex(vertex2), shapeRenderer, graph.isDigraph(), scaleFactor);
            }
        }
        shapeRenderer.setColor(0, 1, 0, 1);
        drawMST(jarnikApplied);
        drawMST(kruskalApplied);
        Graphics.renderGraphVertices(shapeRenderer, graph, scaleFactor, vertexLabels, stage.getBatch());
        shapeRenderer.end();
        stage.getBatch().begin();
        for (EdgeWeight edgeWeight : edgeWeights) {
            edgeWeight.update(scaleFactor);
            edgeWeight.draw(stage.getBatch(), 0);
        }
        stage.getBatch().end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (dijkstraPressed) {
            Graphics.drawMenu(2, scaleFactor, shapeRenderer);
        } else if (jarnikPressed) {
            Graphics.drawMenu(1, scaleFactor, shapeRenderer);
        } else if (dijkstraApplied) {
            for (int a = 0; a < graph.getNumberOfVertices(); a++) {
                final float[] dimensions = Graphics.setupDijkstraBoxes(scaleFactor, graph, a);
                shapeRenderer.setColor(1, 1, 1, 1);
                shapeRenderer.rect(dimensions[0], dimensions[1], dimensions[2], dimensions[3]);
                shapeRenderer.setColor(0, 0, 0, 1);
                Graphics.drawRectangleWithBorder(shapeRenderer, dimensions[0], dimensions[1], dimensions[2], dimensions[3], 2 * scaleFactor, new float[]{1, 1, 1, 1});
                shapeRenderer.rectLine(dimensions[0], dimensions[1] + dimensions[3] / 2f, dimensions[0] + dimensions[2], dimensions[1] + dimensions[3] / 2f, 2 * scaleFactor);
                shapeRenderer.rectLine(dimensions[0] + dimensions[2] / 3f, dimensions[1] + dimensions[3], dimensions[0] + dimensions[2] / 3f, dimensions[1] + dimensions[3] / 2f, 2f * scaleFactor);
                shapeRenderer.rectLine(dimensions[0] + dimensions[2] / 3f * 2f, dimensions[1] + dimensions[3] / 2f, dimensions[0] + dimensions[2] / 3f * 2f, dimensions[1] + dimensions[3], 2 * scaleFactor);
                //System.out.println((dimensions[0] + dimensions[2] / 3f)-(dimensions[0] + dimensions[2] / 3f * 2f));
            }
            if (!dijkstraContainer.setup) {
                dijkstraContainer = graph.setupDijkstraContainer(startVertexInput.getText().charAt(0) - 65, endVertexInput.getText().charAt(0) - 65);
                graph.updateDijkstraLabels(dijkstraLabels, dijkstraContainer.orderLabels, dijkstraContainer.permanentLabels, dijkstraContainer.temporaryLabels);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && dijkstraContainer.permanentLabels[dijkstraContainer.endVertex] == -1) {
                graph.dijkstraStep(dijkstraContainer, dijkstraLabels);
            }
        } else if (chinesePostmanPressed || displayingLowerBounds) {
            Graphics.drawMenu(0, scaleFactor, shapeRenderer);
        } else if (travellingSalesmanPressed || nearestNeighbourPressed) {
            Graphics.drawMenu(1, scaleFactor, shapeRenderer);
        } else if (alertShowing) {
            final float width = 250f * scaleFactor;
            final float height = 75f * scaleFactor;
            Graphics.drawRectangleWithBorder(shapeRenderer, (Gdx.graphics.getWidth() - width) / 2f, (Gdx.graphics.getHeight() - height) / 2f, width, height, 2f, new float[]{207f / 255f, 226f / 255f, 243f / 255f, 1});
        }
        Graphics.drawRectangleWithBorder(shapeRenderer, scaleFactor, 0, 160f * scaleFactor, Gdx.graphics.getHeight() - scaleFactor, 2f * scaleFactor, new float[]{207f / 255f, 226f / 255f, 243f / 255f, 1});
        shapeRenderer.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }
}
