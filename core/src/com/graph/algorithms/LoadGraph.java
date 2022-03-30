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
    private boolean dijkstraPressed = false;
    private boolean dijkstraApplied = false;
    private DijkstraContainer dijkstraContainer = new DijkstraContainer();
    private boolean jarnikPressed = false;
    private boolean jarnikApplied = false;
    private int minimumSpanningTreeCounter = 1;
    private boolean kruskalApplied = false;
    private boolean displayingRouteInspection = false;
    private boolean everRanRouteInspection = false;
    private boolean travellingSalesmanPressed = false;

    public LoadGraph(final Graph graph) { //To-do: make it not make two edgeWeights for every edge on an undirected graph.
        this.graph = graph;
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
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
        final Text menuTitle = new Text("Dijkstra's Algorithm Options", Gdx.graphics.getWidth() / 2f, 545 * scaleFactor, Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 25f * scaleFactor, 0), new float[]{0, 0, 0, 1}, 0, 0, -1);
        final float y1 = 491.5f;
        final Text startVertexLabel = new Text("Start Vertex", 430f * scaleFactor, y1 * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0, -1);
        final Text endVertexLabel = new Text("End Vertex", 430f * scaleFactor, (y1 - 61f) * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0, -1);
        final Skin buttonSkin = Graphics.generateSkin(twenty);
        final TextButton back = new TextButton("Back", buttonSkin, "default");
        final TextButton apply = new TextButton("Apply", buttonSkin, "default");
        final BitmapFont small = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 10f * scaleFactor, 0);
        final BitmapFont medium = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 15f * scaleFactor, 0);
        final List<String> repeatedEdgesList = new List<>(skin);
        final ScrollPane scrollBar = new ScrollPane(repeatedEdgesList, skin, "default");
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

        jarnikButton.setWidth(127 * scaleFactor);
        jarnikButton.setHeight(46 * scaleFactor);
        jarnikButton.setPosition(dijkstraButton.getX(), dijkstraButton.getY() - 71 * scaleFactor);

        kruskalButton.setWidth(127 * scaleFactor);
        kruskalButton.setHeight(46 * scaleFactor);
        kruskalButton.setPosition(jarnikButton.getX(), jarnikButton.getY() - 71 * scaleFactor);

        routeInspectionButton.setWidth(127 * scaleFactor);
        routeInspectionButton.setHeight(46 * scaleFactor);
        routeInspectionButton.setPosition(kruskalButton.getX(), kruskalButton.getY() - 71 * scaleFactor);

        travellingSalesmanButton.setWidth(127 * scaleFactor);
        travellingSalesmanButton.setHeight(46 * scaleFactor);
        travellingSalesmanButton.setPosition(routeInspectionButton.getX(), routeInspectionButton.getY() - 71 * scaleFactor);

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


        dijkstraButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!jarnikApplied && !kruskalApplied && !displayingRouteInspection && !travellingSalesmanPressed) {
                    if (dijkstraApplied) {
                        dijkstraApplied = false;
                        dijkstraContainer.setup = false;
                        for (int a = 0; a < graph.getNumberOfVertices(); a++) {
                            for (int b = 0; b < 4; b++) {
                                dijkstraLabels[a][b].setVisible(false);
                            }
                        }
                        dijkstraButton.setTouchable(Touchable.enabled);
                        jarnikButton.setTouchable(Touchable.enabled);
                        kruskalButton.setTouchable(Touchable.enabled);
                        routeInspectionButton.setTouchable(Touchable.enabled);
                        travellingSalesmanButton.setTouchable(Touchable.enabled);
                        mainMenu.setTouchable(Touchable.enabled);
                        edit.setTouchable(Touchable.enabled);
                    } else if (!dijkstraPressed) {
                        dijkstraPressed = true;
                        changeVisibility(menuTitle, startVertexLabel, endVertexLabel, back, apply);
                        menuTitle.updateText("Dijkstra's Algorithm Options");
                        startVertexLabel.updateText("Start Vertex");
                        endVertexLabel.updateText("End Vertex");
                        dijkstraButton.setTouchable(Touchable.disabled);
                        jarnikButton.setTouchable(Touchable.disabled);
                        kruskalButton.setTouchable(Touchable.disabled);
                        routeInspectionButton.setTouchable(Touchable.disabled);
                        travellingSalesmanButton.setTouchable(Touchable.disabled);
                        mainMenu.setTouchable(Touchable.disabled);
                        edit.setTouchable(Touchable.disabled);
                        startVertexInput.setText("A");
                        endVertexInput.setText("A");
                    }
                }
            }
        });
        jarnikButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!graph.isDigraph() && !dijkstraApplied && !kruskalApplied && !displayingRouteInspection && !travellingSalesmanPressed) {
                    if (jarnikApplied) {
                        jarnikApplied = false;
                    } else {
                        jarnikPressed = !jarnikPressed;
                        if (jarnikPressed) {
                            changeVisibility(menuTitle, startVertexLabel, endVertexLabel, back, apply);
                            endVertexInput.setVisible(false);
                            menuTitle.updateText("Jarník's Algorithm Options");
                            startVertexLabel.updateText("Start Vertex");
                            endVertexLabel.updateText("");
                            dijkstraButton.setTouchable(Touchable.disabled);
                            jarnikButton.setTouchable(Touchable.disabled);
                            kruskalButton.setTouchable(Touchable.disabled);
                            routeInspectionButton.setTouchable(Touchable.disabled);
                            travellingSalesmanButton.setTouchable(Touchable.disabled);
                            mainMenu.setTouchable(Touchable.disabled);
                            edit.setTouchable(Touchable.disabled);
                            startVertexInput.setText("A");
                        }
                    }
                }
            }
        });
        kruskalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!graph.isDigraph() && !dijkstraApplied && !jarnikApplied && !displayingRouteInspection && !travellingSalesmanPressed) {
                    kruskalApplied = !kruskalApplied;
                    if (kruskalApplied) {
                        minimumEdges.clear();
                        minimumSpanningTreeCounter = 1;
                        graph.kruskal(minimumEdges);
                    }
                }
            }
        });
        routeInspectionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!graph.isDigraph() && !dijkstraApplied && !jarnikApplied && !kruskalApplied && !travellingSalesmanPressed) {
                    displayingRouteInspection = true;
                    changeVisibility(menuTitle, startVertexLabel, endVertexLabel, back, apply);
                    menuTitle.updateText("Chinese Postman Repeated Edges:");
                    scrollBar.setVisible(true);
                    apply.setVisible(false);
                    dijkstraButton.setTouchable(Touchable.disabled);
                    jarnikButton.setTouchable(Touchable.disabled);
                    kruskalButton.setTouchable(Touchable.disabled);
                    routeInspectionButton.setTouchable(Touchable.disabled);
                    travellingSalesmanButton.setTouchable(Touchable.disabled);
                    mainMenu.setTouchable(Touchable.disabled);
                    edit.setTouchable(Touchable.disabled);
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
                            repeatedEdgesList.setItems(items);
                        } else {
                            repeatedEdgesList.setItems("Total Weight: " + graph.getSumOfEdgeWeights());
                        }
                        everRanRouteInspection = true;
                    }
                }
            }
        });
        travellingSalesmanButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!graph.isDigraph() && !dijkstraApplied && !jarnikApplied && !kruskalApplied && !displayingRouteInspection) {
                    travellingSalesmanPressed = true;
                    changeVisibility(menuTitle, startVertexLabel, endVertexLabel, back, apply);
                    menuTitle.updateText("Travelling Salesman Problem:");
                    startVertexLabel.updateText("Algorithm: ");
                    endVertexLabel.setVisible(false);
                    startVertexInput.setVisible(false);
                    endVertexInput.setVisible(false);
                    selectTSPAlgorithm.setVisible(true);
                    selectTSPAlgorithm.setSelectedIndex(0);
                    dijkstraButton.setTouchable(Touchable.disabled);
                    jarnikButton.setTouchable(Touchable.disabled);
                    kruskalButton.setTouchable(Touchable.disabled);
                    routeInspectionButton.setTouchable(Touchable.disabled);
                    travellingSalesmanButton.setTouchable(Touchable.disabled);
                    mainMenu.setTouchable(Touchable.disabled);
                    edit.setTouchable(Touchable.disabled);
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
                changeVisibility(menuTitle, startVertexLabel, endVertexLabel, back, apply);
                dijkstraButton.setTouchable(Touchable.enabled);
                jarnikButton.setTouchable(Touchable.enabled);
                kruskalButton.setTouchable(Touchable.enabled);
                routeInspectionButton.setTouchable(Touchable.enabled);
                travellingSalesmanButton.setTouchable(Touchable.enabled);
                mainMenu.setTouchable(Touchable.enabled);
                edit.setTouchable(Touchable.enabled);
                if (dijkstraPressed) {
                    dijkstraPressed = false;
                } else if (jarnikPressed) {
                    jarnikPressed = false;
                    endVertexInput.setVisible(false);
                } else if (displayingRouteInspection) {
                    scrollBar.setVisible(false);
                    apply.setVisible(false);
                    displayingRouteInspection = false;
                } else if (travellingSalesmanPressed) {
                    endVertexLabel.setVisible(false);
                    startVertexInput.setVisible(false);
                    endVertexInput.setVisible(false);
                    selectTSPAlgorithm.setVisible(false);
                    travellingSalesmanPressed = false;
                }
            }
        });
        apply.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dijkstraButton.setTouchable(Touchable.enabled);
                jarnikButton.setTouchable(Touchable.enabled);
                kruskalButton.setTouchable(Touchable.enabled);
                routeInspectionButton.setTouchable(Touchable.enabled);
                travellingSalesmanButton.setTouchable(Touchable.enabled);
                mainMenu.setTouchable(Touchable.enabled);
                edit.setTouchable(Touchable.enabled);
                changeVisibility(menuTitle, startVertexLabel, endVertexLabel, back, apply);
                if (dijkstraPressed) {
                    dijkstraPressed = false;
                    dijkstraApplied = true;
                    for (int a = 0; a < graph.getNumberOfVertices(); a++) {
                        for (int b = 0; b < 4; b++) {
                            dijkstraLabels[a][b].setVisible(true);
                        }
                    }
                } else if (jarnikPressed) {
                    jarnikPressed = false;
                    jarnikApplied = true;
                    minimumEdges.clear();
                    minimumSpanningTreeCounter = 1;
                    graph.jarnik(minimumEdges, startVertexInput.getText().charAt(0) - 65);
                    endVertexInput.setVisible(false);
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
    }

    private boolean drawMST(boolean condition) {
        if (condition) {
            shapeRenderer.setColor(0, 1, 0, 1);
            for (int a = 0; a < minimumSpanningTreeCounter; a++) {
                Graphics.renderEdge(graph.getXCoordinateOfVertex(minimumEdges.get(a)[0]), graph.getYCoordinateOfVertex(minimumEdges.get(a)[0]), graph.getXCoordinateOfVertex(minimumEdges.get(a)[1]), graph.getYCoordinateOfVertex(minimumEdges.get(a)[1]), shapeRenderer, false, scaleFactor);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                minimumSpanningTreeCounter++;
            }
            if (minimumSpanningTreeCounter == graph.getNumberOfVertices()) {
                condition = false;
            }
        }
        return condition;
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
        }
        jarnikApplied = drawMST(jarnikApplied);
        kruskalApplied = drawMST(kruskalApplied);
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
        } else if (displayingRouteInspection) {
            Graphics.drawMenu(0, scaleFactor, shapeRenderer);
        } else if (travellingSalesmanPressed) {
            Graphics.drawMenu(1, scaleFactor, shapeRenderer);
        }
        Graphics.drawRectangleWithBorder(shapeRenderer, scaleFactor, 0, 160f * scaleFactor, Gdx.graphics.getHeight() - scaleFactor, 2f * scaleFactor, new float[]{207f / 255f, 226f / 255f, 243f / 255f, 1});
        shapeRenderer.end();
        stage.act();
        stage.draw();
    }

    private void changeVisibility(final Text title, final Text startVertexLabel, final Text endVertexLabel, final TextButton back, final TextButton apply) {
        title.setVisible(!title.isVisible());
        startVertexLabel.setVisible(!startVertexLabel.isVisible());
        endVertexLabel.setVisible(!endVertexLabel.isVisible());
        startVertexInput.setVisible(!startVertexInput.isVisible());
        endVertexInput.setVisible(!endVertexInput.isVisible());
        back.setVisible(!back.isVisible());
        apply.setVisible(!apply.isVisible());
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
