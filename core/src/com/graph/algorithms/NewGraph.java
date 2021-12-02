package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NewGraph implements Screen {
    public final Stage stage = new Stage();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final float scaleFactor = Gdx.graphics.getHeight() / 720f;
    private final Graph graph;
    private boolean newVertexClicked = false;
    private boolean newEdgeClicked = false;
    private int firstVertex = -1;
    private int secondVertex = -1;
    private int vertexBeingMoved = -1;

    public NewGraph(final Boolean digraphStatus) {
        graph = new Graph(digraphStatus);
        Skin skin = Text.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 15f * scaleFactor, 0));
        FileHandle file = Gdx.files.local("graphs/New Graph.graph2");
        int counter = 1;
        while (file.exists()) {
            file = Gdx.files.local("graphs/New Graph (" + counter + ").graph2");
            counter++;
        }
        final TextField name = new TextField(file.name().substring(0, file.name().lastIndexOf(".")), skin);
        name.setAlignment(1);
        final TextField edgeWeight = new TextField("0", skin);
        BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        edgeWeight.setAlignment(1);
        edgeWeight.setVisible(false);
        edgeWeight.setX(757 * scaleFactor);
        edgeWeight.setY(479 * scaleFactor);
        edgeWeight.setWidth(88 * scaleFactor);
        edgeWeight.setHeight(24 * scaleFactor);
        stage.addActor(edgeWeight);
        final Text edgeWeightTitle = new Text("Edge Properties", Gdx.graphics.getWidth() / 2f, 560 * scaleFactor, Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 25f * scaleFactor, 0), new float[]{0, 0, 0, 1});
        edgeWeightTitle.setVisible(false);
        final Text edgeWeightLabel = new Text("Edge Weight", 490 * scaleFactor, 505 * scaleFactor, twenty, new float[]{0, 0, 0, 1});
        edgeWeightLabel.setVisible(false);
        stage.addActor(edgeWeightLabel);
        stage.addActor(edgeWeightTitle);
        Skin buttonSkin = Text.generateSkin(twenty);
        final TextButton back = new TextButton("Back", buttonSkin, "default");
        final TextButton apply = new TextButton("Apply", buttonSkin, "default");

        back.setVisible(false);
        apply.setVisible(false);
        back.setWidth(127 * scaleFactor);
        back.setHeight(46 * scaleFactor);
        back.setY(162 * scaleFactor);
        back.setX(414f * scaleFactor);
        apply.setWidth(back.getWidth());
        apply.setHeight(back.getHeight());
        apply.setY(back.getY());
        apply.setX(back.getX() + 325 * scaleFactor);
        stage.addActor(apply);
        stage.addActor(back);


        final TextButton newVertex = new TextButton("New Vertex", skin, "default");
        final TextButton newEdge = new TextButton("New Edge", skin, "default");
        final TextButton save = new TextButton("Save", skin, "default");
        //TextButton saveAs = new TextButton("Save As", skin, "default");
        final TextButton finish = new TextButton("Finish", skin, "default");
        final TextButton mainMenu = new TextButton("Main Menu", skin, "default");
        final TextButton viewHotkeys = new TextButton("View Hotkeys", skin, "default");
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                edgeWeight.setVisible(false);
                edgeWeightTitle.setVisible(false);
                edgeWeightLabel.setVisible(false);
                back.setVisible(false);
                apply.setVisible(false);
                newVertex.setTouchable(Touchable.enabled);
                newEdge.setTouchable(Touchable.enabled);
                save.setTouchable(Touchable.enabled);
                finish.setTouchable(Touchable.enabled);
                mainMenu.setTouchable(Touchable.enabled);
                viewHotkeys.setTouchable(Touchable.enabled);
                name.setTouchable(Touchable.enabled);
                newEdgeClicked = false;
                firstVertex = -1;
                secondVertex = -1;
            }
        });
        apply.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (digraphStatus) {
                    graph.addDirectedEdge(firstVertex, secondVertex, Integer.parseInt(edgeWeight.getText()));
                } else {
                    graph.addUndirectedEdge(firstVertex, secondVertex, Integer.parseInt(edgeWeight.getText()));
                }
                edgeWeight.setVisible(false);
                edgeWeightTitle.setVisible(false);
                edgeWeightLabel.setVisible(false);
                back.setVisible(false);
                apply.setVisible(false);
                newVertex.setTouchable(Touchable.enabled);
                newEdge.setTouchable(Touchable.enabled);
                save.setTouchable(Touchable.enabled);
                finish.setTouchable(Touchable.enabled);
                mainMenu.setTouchable(Touchable.enabled);
                viewHotkeys.setTouchable(Touchable.enabled);
                name.setTouchable(Touchable.enabled);
                newEdgeClicked = false;
                firstVertex = -1;
                secondVertex = -1;
                edgeWeight.setText("0");
            }
        });
        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (mouseInBounds(scaleFactor)) {
                    if (newVertexClicked) {
                        graph.addVertex(x / scaleFactor, y / scaleFactor);
                        newVertexClicked = false;
                    } else if (newEdgeClicked) {
                        int clickedVertex = findVertexBeingClicked();
                        if (clickedVertex != -1) {
                            if (firstVertex == -1) {
                                firstVertex = clickedVertex;
                            } else {
                                if (firstVertex != clickedVertex && !graph.areVerticesConnected(firstVertex, clickedVertex)) {
                                    secondVertex = clickedVertex;
                                    edgeWeight.setVisible(true);
                                    edgeWeightTitle.setVisible(true);
                                    edgeWeightLabel.setVisible(true);
                                    back.setVisible(true);
                                    apply.setVisible(true);
                                    newVertex.setTouchable(Touchable.disabled);
                                    newEdge.setTouchable(Touchable.disabled);
                                    save.setTouchable(Touchable.disabled);
                                    finish.setTouchable(Touchable.disabled);
                                    mainMenu.setTouchable(Touchable.disabled);
                                    viewHotkeys.setTouchable(Touchable.disabled);
                                    name.setTouchable(Touchable.disabled);
                                }
                            }
                        }
                        if (firstVertex == -1) {
                            newEdgeClicked = false;
                        }
                    }
                } else if (y / scaleFactor < 350f && secondVertex == -1) {
                    newVertexClicked = false;
                    newEdgeClicked = false;
                    firstVertex = -1;
                }
            }
        });
        mainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });
        newVertex.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!newEdgeClicked) {
                    newVertexClicked = true;
                }
            }
        });
        newEdge.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!newVertexClicked) {
                    newEdgeClicked = true;
                }
            }
        });
        save.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                graph.saveGraph(name.getText());
            }
        });
        name.setWidth(124 * scaleFactor);
        name.setHeight(46 * scaleFactor);
        name.setPosition(80f * scaleFactor - name.getWidth() / 2f, 652f * scaleFactor);
        stage.addActor(name);
        newVertex.setWidth(127 * scaleFactor);
        newVertex.setHeight(46 * scaleFactor);
        newVertex.setPosition(80f * scaleFactor - newVertex.getWidth() / 2f, name.getY() - 71 * scaleFactor);
        stage.addActor(newVertex);
        newEdge.setWidth(127 * scaleFactor);
        newEdge.setHeight(46 * scaleFactor);
        newEdge.setPosition(80f * scaleFactor - newEdge.getWidth() / 2f, newVertex.getY() - 71 * scaleFactor);
        stage.addActor(newEdge);
        save.setWidth(127 * scaleFactor);
        save.setHeight(46 * scaleFactor);
        save.setPosition(80f * scaleFactor - save.getWidth() / 2f, newEdge.getY() - 71 * scaleFactor);
        stage.addActor(save);
        /*saveAs.setWidth(127 * scaleFactor);
        saveAs.setHeight(46 * scaleFactor);
        saveAs.setPosition(80f * scaleFactor - saveAs.getWidth() / 2f, save.getY() - 71 * scaleFactor);
        stage.addActor(saveAs);*/
        finish.setWidth(127 * scaleFactor);
        finish.setHeight(46 * scaleFactor);
        finish.setPosition(80f * scaleFactor - finish.getWidth() / 2f, save.getY() - 71 * scaleFactor);
        stage.addActor(finish);
        mainMenu.setWidth(127 * scaleFactor);
        mainMenu.setHeight(46 * scaleFactor);
        mainMenu.setPosition(80f * scaleFactor - mainMenu.getWidth() / 2f, 95 * scaleFactor);
        stage.addActor(mainMenu);
        viewHotkeys.setWidth(127 * scaleFactor);
        viewHotkeys.setHeight(46 * scaleFactor);
        viewHotkeys.setPosition(80f * scaleFactor - viewHotkeys.getWidth() / 2f, mainMenu.getY() - 71 * scaleFactor);
        stage.addActor(viewHotkeys);
    }

    public static float[] rotatePointAboutPoint(float[] point, float[] centre, float angle) {
        return new float[]{(float) (Math.cos(angle) * (point[0] - centre[0]) - Math.sin(angle) * (point[1] - centre[1]) + centre[0]), (float) (Math.sin(angle) * (point[0] - centre[0]) + Math.cos(angle) * (point[1] - centre[1]) + centre[1])};
    }

    public static float[][] arrowHeadGenerator(float[] point1, float[] point2, float scaleFactor) {
        float[] centreOfMass = new float[]{(point1[0] + point2[0]) * scaleFactor / 2, (point1[1] + point2[1]) * scaleFactor / 2};
        float sideLength = 23.551f * scaleFactor;
        float angle = (float) (Math.acos((point2[1] - point1[1]) / Math.sqrt(Math.pow(point2[0] - point1[0], 2) + Math.pow(point2[1] - point1[1], 2))));
        float y2 = (float) (centreOfMass[1] - sideLength * Math.sin(Math.toRadians(60)) / 3f);
        float[][] points = new float[][]{{centreOfMass[0], (float) (centreOfMass[1] + 2f * sideLength * Math.sin(Math.toRadians(60)) / 3f)}, {centreOfMass[0] - sideLength / 2, y2}, {centreOfMass[0] + sideLength / 2, y2}};
        if (point2[0] - point1[0] > 0) {
            angle *= -1;
        }
        for (int c = 0; c < points.length; c++) {
            points[c] = rotatePointAboutPoint(points[c], centreOfMass, angle);
        }
        return points;
    }

    public static boolean mouseInBounds(float scaleFactor) {
        return Gdx.input.getX() / scaleFactor - 15 > 160f && Gdx.input.getY() - 15 * scaleFactor > 0 && Gdx.input.getY() + 15 * scaleFactor < Gdx.graphics.getHeight() && Gdx.input.getX() + 15 * scaleFactor < Gdx.graphics.getWidth();
    }

    public int findVertexBeingClicked() {
        for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
            float mouseX = Gdx.input.getX() / scaleFactor;
            float mouseY = (Gdx.graphics.getHeight() - Gdx.input.getY()) / scaleFactor;
            if (Math.pow(mouseX - graph.getXCoordinate(a), 2) + Math.pow(mouseY - graph.getYCoordinate(a), 2) <= 15 * 15) {
                return a;
            }
        }
        return -1;
    }

    public void renderShapes() {
        shapeRenderer.end();
        Settings.drawRectangleWithBorder(shapeRenderer, scaleFactor, 0, 160f * scaleFactor, Gdx.graphics.getHeight() - scaleFactor, 2f * scaleFactor, new float[]{207f / 255f, 226f / 255f, 243f / 255f, 1});
        if (secondVertex != -1) {
            float width = 514 * scaleFactor;
            float height = 424 * scaleFactor;
            float x = (Gdx.graphics.getWidth() - width) / 2;
            float y = (Gdx.graphics.getHeight() - height) / 2;
            Settings.drawRectangleWithBorder(shapeRenderer, x, y, width, height, 2 * scaleFactor, new float[]{207f / 255f, 226f / 255f, 243f / 255f, 1});
            x = 414 * scaleFactor;
            y = 468 * scaleFactor;
            width = 452 * scaleFactor;
            height = 46 * scaleFactor;
            Settings.drawRectangleWithBorder(shapeRenderer, x, y, width, height, 2 * scaleFactor, new float[]{1f, 229f / 255f, 153f / 255f, 1});
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 1);
        for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
            for (int b = 0; b < graph.getNumberOfEdges(a); b++) {
                renderEdge(a, graph.getVertex(a, b));
            }
        }
        if (newEdgeClicked && firstVertex != -1 && secondVertex == -1) {
            shapeRenderer.rectLine(graph.getXCoordinate(firstVertex) * scaleFactor, graph.getYCoordinate(firstVertex) * scaleFactor, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 5 * scaleFactor);
            if (graph.isDigraph()) {
                float[][] points = NewGraph.arrowHeadGenerator(new float[]{graph.getXCoordinate(firstVertex), graph.getYCoordinate(firstVertex)}, new float[]{Gdx.input.getX() / scaleFactor, (Gdx.graphics.getHeight() - Gdx.input.getY()) / scaleFactor}, scaleFactor);
                shapeRenderer.triangle(points[0][0], points[0][1], points[1][0], points[1][1], points[2][0], points[2][1]);
            }
        } else if (secondVertex != -1) {
            renderEdge(firstVertex, secondVertex);
        }
        shapeRenderer.setColor(0, 0, 0, 1);
        for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
            shapeRenderer.circle(graph.getXCoordinate(a) * scaleFactor, graph.getYCoordinate(a) * scaleFactor, 15 * scaleFactor);
        }
        if (newVertexClicked && mouseInBounds(scaleFactor)) {
            shapeRenderer.circle(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 15 * scaleFactor);
        }
    }

    public void renderEdge(int graph1, int graph2) {
        shapeRenderer.rectLine(graph.getXCoordinate(graph1) * scaleFactor, graph.getYCoordinate(graph1) * scaleFactor, graph.getXCoordinate(graph2) * scaleFactor, graph.getYCoordinate(graph2) * scaleFactor, 5 * scaleFactor);
        if (graph.isDigraph()) {
            float[][] points = NewGraph.arrowHeadGenerator(new float[]{graph.getXCoordinate(graph1), graph.getYCoordinate(graph1)}, new float[]{graph.getXCoordinate(graph2), graph.getYCoordinate(graph2)}, scaleFactor);
            shapeRenderer.triangle(points[0][0], points[0][1], points[1][0], points[1][1], points[2][0], points[2][1]);
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
        if (Gdx.input.isButtonPressed(Input.Keys.LEFT)) {
            if (vertexBeingMoved == -1 && !newEdgeClicked && !newVertexClicked && mouseInBounds(scaleFactor)) {
                vertexBeingMoved = findVertexBeingClicked();
            }
        } else {
            vertexBeingMoved = -1;
        }
        if (vertexBeingMoved != -1 && mouseInBounds(scaleFactor)) {
            graph.setCoordinates(vertexBeingMoved, new float[]{Gdx.input.getX() / scaleFactor, (Gdx.graphics.getHeight() - Gdx.input.getY()) / scaleFactor});
        }
        renderShapes();
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
        shapeRenderer.dispose();
        stage.dispose();
    }
}
