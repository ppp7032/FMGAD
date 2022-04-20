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
    private boolean nearestNeighbourPressed = false;
    private boolean nearestNeighbourApplied = false;
    private boolean alertShowing = false;
    private boolean viewingHelp = false;

    public LoadGraph(final Graph graph) {
        this.graph = graph;
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        alertMessage = new Text("", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, twenty, new float[]{0, 0, 0, 1}, 0, 0, -1);
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
        alertMessage = new Text("", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, twenty, new float[]{0, 0, 0, 1}, 0, 0, -1);
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
        final TextButton help = new TextButton("Help", skin, "default");
        final TextButton mainMenu = new TextButton("Main Menu", skin, "default");
        final TextButton edit = new TextButton("Edit", skin, "default");
        final TextButton[] sidePanelButtons = new TextButton[]{mainMenu, edit, dijkstraButton, jarnikButton, kruskalButton, routeInspectionButton, travellingSalesmanButton, help};
        final Text menuTitle = new Text("", Gdx.graphics.getWidth() / 2f, 545 * scaleFactor, Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 25f * scaleFactor, 0), new float[]{0, 0, 0, 1}, 0, 0, -1);
        final float y1 = 491.5f;
        final Text[] attributes = new Text[]{new Text("", 16f * scaleFactor + (0.5f * (Gdx.graphics.getWidth() - 452 * scaleFactor)), y1 * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0, -1), new Text("", 16f * scaleFactor + (0.5f * (Gdx.graphics.getWidth() - 452 * scaleFactor)), (y1 - 61f) * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0, -1)};
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

        selectTSPAlgorithm.setX(277 * scaleFactor + attributes[0].getX());
        selectTSPAlgorithm.setY(479 * scaleFactor);
        selectTSPAlgorithm.setWidth(138 * scaleFactor);
        selectTSPAlgorithm.setHeight(24 * scaleFactor);
        selectTSPAlgorithm.setItems("Nearest Neighbour", "Lower Bound");
        selectTSPAlgorithm.setVisible(false);

        Graphics.setupBottomButton(edit, scaleFactor);

        Graphics.setupButtonAbove(edit, mainMenu, scaleFactor);

        Graphics.setupButtonAbove(mainMenu, help, scaleFactor);

        menuTitle.setVisible(false);

        for (final Text attribute : attributes) {
            attribute.setVisible(false);
        }

        startVertexInput.setVisible(false);
        startVertexInput.setAlignment(1);
        startVertexInput.setX(327 * scaleFactor + attributes[0].getX());
        startVertexInput.setY(479 * scaleFactor);
        startVertexInput.setWidth(88 * scaleFactor);
        startVertexInput.setHeight(24 * scaleFactor);

        endVertexInput.setVisible(false);
        endVertexInput.setAlignment(1);
        endVertexInput.setX(327 * scaleFactor + attributes[1].getX());
        endVertexInput.setY(startVertexInput.getY() - 61 * scaleFactor);
        endVertexInput.setWidth(88 * scaleFactor);
        endVertexInput.setHeight(24 * scaleFactor);

        scrollBar.setX((0.5f * (Gdx.graphics.getWidth() - 452 * scaleFactor)));
        scrollBar.setY(224f * scaleFactor);
        scrollBar.setHeight(290f * scaleFactor);
        scrollBar.setWidth(452f * scaleFactor);
        scrollBar.setVisible(false);

        Graphics.setupBackAndApplyButtons(back, apply, scaleFactor, false);

        alertMessage.setVisible(false);


        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (alertShowing && Graphics.mouseInBounds(scaleFactor)) {
                    alertShowing = false;
                    alertMessage.setVisible(false);
                }
            }
        });
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
                } else if (!dijkstraPressed) {
                    dijkstraPressed = true;
                    toggleDijkstraPressed(menuTitle, attributes[0], attributes[1], back, apply);
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
                        toggleJarnikPressed(menuTitle, attributes[0], back, apply);
                        toggleButtonTouchableStatus(sidePanelButtons, Touchable.disabled);
                    }
                } else {
                    enableDigraphAlert();
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
                } else {
                    enableDigraphAlert();
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
                } else {
                    enableDigraphAlert();
                }
            }
        });
        travellingSalesmanButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!graph.isDigraph()) {
                    if (!nearestNeighbourApplied) {
                        travellingSalesmanPressed = true;
                        toggleTravellingSalesman(menuTitle, attributes[0], selectTSPAlgorithm, back, apply);
                        toggleButtonTouchableStatus(sidePanelButtons, Touchable.disabled);
                    } else {
                        travellingSalesmanButton.setText("T. Salesman");
                        nearestNeighbourApplied = false;
                        alertShowing = false;
                        alertMessage.setVisible(false);
                        toggleButtonTouchableStatus(sidePanelButtons, Touchable.enabled);
                    }
                } else {
                    enableDigraphAlert();
                }
            }
        });
        help.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleButtonTouchableStatus(sidePanelButtons, Touchable.disabled);
                toggleHelpMenu(menuTitle, attributes[0], back);
                viewingHelp = true;
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
                    toggleDijkstraPressed(menuTitle, attributes[0], attributes[1], back, apply);
                    toggleButtonTouchableStatus(sidePanelButtons, Touchable.enabled);
                } else if (jarnikPressed) {
                    jarnikPressed = false;
                    toggleJarnikPressed(menuTitle, attributes[0], back, apply);
                    toggleButtonTouchableStatus(sidePanelButtons, Touchable.enabled);
                } else if (chinesePostmanPressed) {
                    chinesePostmanPressed = false;
                    toggleChinesePostman(menuTitle, scrollBar, back);
                    toggleButtonTouchableStatus(sidePanelButtons, Touchable.enabled);
                } else if (displayingLowerBounds) {
                    displayingLowerBounds = false;
                    travellingSalesmanPressed = true;
                    toggleLowerBounds(menuTitle, scrollBar, back);
                    toggleTravellingSalesman(menuTitle, attributes[0], selectTSPAlgorithm, back, apply);
                } else if (nearestNeighbourPressed) {
                    toggleNearestNeighbourPressed(menuTitle, attributes[0], back, apply);
                    toggleTravellingSalesman(menuTitle, attributes[0], selectTSPAlgorithm, back, apply);
                    nearestNeighbourPressed = false;
                    travellingSalesmanPressed = true;
                } else if (travellingSalesmanPressed) {
                    travellingSalesmanPressed = false;
                    toggleTravellingSalesman(menuTitle, attributes[0], selectTSPAlgorithm, back, apply);
                    toggleButtonTouchableStatus(sidePanelButtons, Touchable.enabled);
                } else if (viewingHelp) {
                    viewingHelp = false;
                    toggleButtonTouchableStatus(sidePanelButtons, Touchable.enabled);
                    toggleHelpMenu(menuTitle, attributes[0], back);
                }
            }
        });
        apply.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (nearestNeighbourPressed) {
                    final int vertex = graph.getVertexFromInput(startVertexInput.getText());
                    if (vertex != -1) {
                        toggleNearestNeighbourPressed(menuTitle, attributes[0], back, apply);
                        travellingSalesmanButton.setTouchable(Touchable.enabled);
                        mainMenu.setTouchable(Touchable.enabled);
                        edit.setTouchable(Touchable.enabled);
                        travellingSalesmanButton.setText("Back");
                        nearestNeighbourPressed = false;
                        nearestNeighbourApplied = true;
                        graph.nearestNeighbour(vertex, nearestNeighbourPath);
                        counter = 1;
                    }
                } else if (travellingSalesmanPressed) {
                    travellingSalesmanPressed = false;
                    if (selectTSPAlgorithm.getSelectedIndex() == 1) {
                        displayingLowerBounds = true;
                        toggleTravellingSalesman(menuTitle, attributes[0], selectTSPAlgorithm, back, apply);
                        toggleLowerBounds(menuTitle, scrollBar, back);
                        if (!everRanLowerBounds) {
                            final int[] lowestBounds = graph.lowestBoundTSP();
                            final String[] items = new String[lowestBounds.length];
                            for (int a = 0; a < lowestBounds.length; a++) {
                                final String value;
                                if (lowestBounds[a] == -1) {
                                    value = "Algorithm Stalled";
                                } else {
                                    value = String.valueOf(lowestBounds[a]);
                                }
                                items[a] = (char) (a + 65) + ": " + value;
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
                        toggleTravellingSalesman(menuTitle, attributes[0], selectTSPAlgorithm, back, apply);
                        toggleNearestNeighbourPressed(menuTitle, attributes[0], back, apply);
                    }
                } else if (dijkstraPressed) {
                    final int vertex1 = graph.getVertexFromInput(startVertexInput.getText());
                    final int vertex2 = graph.getVertexFromInput(endVertexInput.getText());
                    if (vertex1 != -1 && vertex2 != -1) {
                        dijkstraPressed = false;
                        dijkstraApplied = true;
                        for (int a = 0; a < graph.getNumberOfVertices(); a++) {
                            for (int b = 0; b < 4; b++) {
                                dijkstraLabels[a][b].setVisible(true);
                            }
                        }
                        toggleDijkstraPressed(menuTitle, attributes[0], attributes[1], back, apply);
                        dijkstraButton.setTouchable(Touchable.enabled);
                        mainMenu.setTouchable(Touchable.enabled);
                        edit.setTouchable(Touchable.enabled);
                        dijkstraButton.setText("Back");
                    }
                } else if (jarnikPressed) {
                    final int vertex = graph.getVertexFromInput(startVertexInput.getText());
                    if (vertex != -1) {
                        jarnikPressed = false;
                        jarnikApplied = true;
                        minimumEdges.clear();
                        counter = 1;
                        graph.jarnik(minimumEdges, vertex);
                        toggleJarnikPressed(menuTitle, attributes[0], back, apply);
                        jarnikButton.setTouchable(Touchable.enabled);
                        mainMenu.setTouchable(Touchable.enabled);
                        edit.setTouchable(Touchable.enabled);
                        jarnikButton.setText("Back");
                    }
                }
            }
        });


        for (final TextButton sidePanelButton : sidePanelButtons) {
            stage.addActor(sidePanelButton);
        }
        stage.addActor(selectTSPAlgorithm);
        for (final Text attribute : attributes) {
            stage.addActor(attribute);
        }
        stage.addActor(menuTitle);
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

    private void toggleHelpMenu(final Text menuTitle, final Text attribute, final TextButton back) {
        final boolean newStatus = !menuTitle.isVisible();
        if (newStatus) {
            menuTitle.updateText("Help");
            attribute.updateText("Press space to do an iteration");
        }
        attribute.setVisible(newStatus);
        menuTitle.setVisible(newStatus);
        back.setVisible(newStatus);
    }

    private void enableDigraphAlert() {
        alertShowing = true;
        alertMessage.updateText("Only Dijkstra's algorithm\nis supported on digraphs!");
        alertMessage.setVisible(true);
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

    private void toggleTravellingSalesman(final Text menuTitle, final Text algorithmLabel, final SelectBox<String> selector, final TextButton back, final TextButton apply) {
        final boolean newStatus = !menuTitle.isVisible();
        if (newStatus) {
            menuTitle.updateText("Travelling Salesman Problem");
            algorithmLabel.updateText("Algorithm");
            selector.setSelectedIndex(0);
        }
        menuTitle.setVisible(newStatus);
        algorithmLabel.setVisible(newStatus);
        selector.setVisible(newStatus);
        back.setVisible(newStatus);
        apply.setVisible(newStatus);
    }

    private void toggleLowerBounds(final Text menuTitle, final ScrollPane scrollBar, final TextButton back) {
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
            if (counter == nearestNeighbourPath.size() - 2 && nearestNeighbourPath.get(nearestNeighbourPath.size() - 1) == -1) {
                alertShowing = true;
                alertMessage.updateText("Nearest Neighbour\nhas stalled!");
                alertMessage.setVisible(true);
            } else if (counter < nearestNeighbourPath.size() - 2 && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                counter++;
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
            }
            if (!dijkstraContainer.setup) {
                dijkstraContainer = graph.setupDijkstraContainer(graph.getVertexFromInput(startVertexInput.getText()), graph.getVertexFromInput(endVertexInput.getText()));
                graph.updateDijkstraLabels(dijkstraLabels, dijkstraContainer.orderLabels, dijkstraContainer.permanentLabels, dijkstraContainer.temporaryLabels);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && dijkstraContainer.permanentLabels[dijkstraContainer.endVertex] == -1) {
                graph.dijkstraStep(dijkstraContainer, dijkstraLabels);
            }
        } else if (chinesePostmanPressed || displayingLowerBounds) {
            Graphics.drawMenu(0, scaleFactor, shapeRenderer);
        } else if (travellingSalesmanPressed || nearestNeighbourPressed || jarnikPressed || viewingHelp) {
            Graphics.drawMenu(1, scaleFactor, shapeRenderer);
        } else if (alertShowing) {
            Graphics.renderAlert(shapeRenderer, alertMessage, scaleFactor);
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
