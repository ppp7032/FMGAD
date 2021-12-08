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

import java.util.ArrayList;

public class NewGraph implements Screen {
    public final Stage stage = new Stage();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final float scaleFactor = Graphics.findScaleFactor();
    private final Graph graph;
    private final ArrayList<EdgeWeight> edgeWeights = new ArrayList<>();
    private boolean newVertexClicked = false;
    private boolean newEdgeClicked = false;
    private int firstVertex = -1;
    private int secondVertex = -1;
    private int vertexBeingMoved = -1;

    public NewGraph(final Boolean digraphStatus) {
        graph = new Graph(digraphStatus);
        final Skin skin = Graphics.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 15f * scaleFactor, 0));
        FileHandle file = Gdx.files.local("graphs/New Graph.graph2");
        int counter = 1;
        while (file.exists()) {
            file = Gdx.files.local("graphs/New Graph (" + counter + ").graph2");
            counter++;
        }
        final TextField name = new TextField(file.name().substring(0, file.name().lastIndexOf(".")), skin);
        final TextField edgeWeight = new TextField("0", skin);
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        final Text edgeWeightTitle = new Text("Edge Properties", Gdx.graphics.getWidth() / 2f, 545 * scaleFactor, Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 25f * scaleFactor, 0), new float[]{0, 0, 0, 1}, 0, 0);
        final Text edgeWeightLabel = new Text("Edge Weight", 430f * scaleFactor, 491.5f * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0);
        final Skin buttonSkin = Graphics.generateSkin(twenty);
        final TextButton back = new TextButton("Back", buttonSkin, "default");
        final TextButton apply = new TextButton("Apply", buttonSkin, "default");
        final TextButton newVertex = new TextButton("New Vertex", skin, "default");
        final TextButton newEdge = new TextButton("New Edge", skin, "default");
        final TextButton save = new TextButton("Save", skin, "default");
        final TextButton finish = new TextButton("Finish", skin, "default");
        final TextButton mainMenu = new TextButton("Main Menu", skin, "default");
        final TextButton viewHotkeys = new TextButton("View Hotkeys", skin, "default");


        name.setAlignment(1);
        name.setWidth(124 * scaleFactor);
        name.setHeight(46 * scaleFactor);
        name.setPosition(80f * scaleFactor - name.getWidth() / 2f, 652f * scaleFactor);

        edgeWeight.setAlignment(1);
        edgeWeight.setVisible(false);
        edgeWeight.setX(757 * scaleFactor);
        edgeWeight.setY(479 * scaleFactor);
        edgeWeight.setWidth(88 * scaleFactor);
        edgeWeight.setHeight(24 * scaleFactor);

        edgeWeightTitle.setVisible(false);

        edgeWeightLabel.setVisible(false);

        back.setVisible(false);
        back.setWidth(127 * scaleFactor);
        back.setHeight(46 * scaleFactor);
        back.setY(162 * scaleFactor);
        back.setX(414f * scaleFactor);

        apply.setVisible(false);
        apply.setWidth(back.getWidth());
        apply.setHeight(back.getHeight());
        apply.setY(back.getY());
        apply.setX(back.getX() + 325 * scaleFactor);

        newVertex.setWidth(127 * scaleFactor);
        newVertex.setHeight(46 * scaleFactor);
        newVertex.setPosition(80f * scaleFactor - newVertex.getWidth() / 2f, name.getY() - 71 * scaleFactor);

        newEdge.setWidth(127 * scaleFactor);
        newEdge.setHeight(46 * scaleFactor);
        newEdge.setPosition(80f * scaleFactor - newEdge.getWidth() / 2f, newVertex.getY() - 71 * scaleFactor);

        save.setWidth(127 * scaleFactor);
        save.setHeight(46 * scaleFactor);
        save.setPosition(80f * scaleFactor - save.getWidth() / 2f, newEdge.getY() - 71 * scaleFactor);

        finish.setWidth(127 * scaleFactor);
        finish.setHeight(46 * scaleFactor);
        finish.setPosition(80f * scaleFactor - finish.getWidth() / 2f, save.getY() - 71 * scaleFactor);

        mainMenu.setWidth(127 * scaleFactor);
        mainMenu.setHeight(46 * scaleFactor);
        mainMenu.setPosition(80f * scaleFactor - mainMenu.getWidth() / 2f, 95 * scaleFactor);

        viewHotkeys.setWidth(127 * scaleFactor);
        viewHotkeys.setHeight(46 * scaleFactor);
        viewHotkeys.setPosition(80f * scaleFactor - viewHotkeys.getWidth() / 2f, mainMenu.getY() - 71 * scaleFactor);


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
                edgeWeights.add(new EdgeWeight(graph, firstVertex, secondVertex, edgeWeight.getText(), twenty, new float[]{0, 0, 0, 1}, 0, 0, scaleFactor));
                stage.addActor(edgeWeights.get(edgeWeights.size() - 1));
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
        mainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });


        stage.addActor(name);
        stage.addActor(edgeWeight);
        stage.addActor(edgeWeightTitle);
        stage.addActor(edgeWeightLabel);
        stage.addActor(back);
        stage.addActor(apply);
        stage.addActor(newVertex);
        stage.addActor(newEdge);
        stage.addActor(save);
        stage.addActor(finish);
        stage.addActor(mainMenu);
        stage.addActor(viewHotkeys);
    }

    private static boolean mouseInBounds(final float scaleFactor) {
        return Gdx.input.getX() / scaleFactor - 15 > 160f && Gdx.input.getY() - 15 * scaleFactor > 0 && Gdx.input.getY() + 15 * scaleFactor < Gdx.graphics.getHeight() && Gdx.input.getX() + 15 * scaleFactor < Gdx.graphics.getWidth();
    }

    private int findVertexBeingClicked() {
        for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
            final float mouseX = Gdx.input.getX() / scaleFactor;
            final float mouseY = (Gdx.graphics.getHeight() - Gdx.input.getY()) / scaleFactor;
            if (Math.pow(mouseX - graph.getXCoordinate(a), 2) + Math.pow(mouseY - graph.getYCoordinate(a), 2) <= 15 * 15) {
                return a;
            }
        }
        return -1;
    }

    private void renderShapes() {
        shapeRenderer.setColor(1, 0, 0, 1);
        for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
            for (int b = 0; b < graph.getNumberOfEdges(a); b++) {
                Graphics.renderEdge(graph.getXCoordinate(a), graph.getYCoordinate(a), graph.getXCoordinate(graph.getVertex(a, b)), graph.getYCoordinate(graph.getVertex(a, b)), shapeRenderer, graph.isDigraph(), scaleFactor);
            }
        }
        if (newEdgeClicked && firstVertex != -1 && secondVertex == -1) {
            Graphics.renderEdge(graph.getXCoordinate(firstVertex), graph.getYCoordinate(firstVertex), Gdx.input.getX() / scaleFactor, (Gdx.graphics.getHeight() - Gdx.input.getY()) / scaleFactor, shapeRenderer, graph.isDigraph(), scaleFactor);

        } else if (secondVertex != -1) {
            Graphics.renderEdge(graph.getXCoordinate(firstVertex), graph.getYCoordinate(firstVertex), graph.getXCoordinate(secondVertex), graph.getYCoordinate(secondVertex), shapeRenderer, graph.isDigraph(), scaleFactor);
        }
        shapeRenderer.setColor(0, 0, 0, 1);
        for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
            shapeRenderer.circle(graph.getXCoordinate(a) * scaleFactor, graph.getYCoordinate(a) * scaleFactor, 15 * scaleFactor);
        }
        if (newVertexClicked && mouseInBounds(scaleFactor)) {
            shapeRenderer.circle(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 15 * scaleFactor);
        }
        Graphics.drawRectangleWithBorder(shapeRenderer, scaleFactor, 0, 160f * scaleFactor, Gdx.graphics.getHeight() - scaleFactor, 2f * scaleFactor, new float[]{207f / 255f, 226f / 255f, 243f / 255f, 1});
        if (secondVertex != -1) {
            Graphics.drawMenu(1, scaleFactor, shapeRenderer);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(247f / 255f, 247f / 255f, 247f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (EdgeWeight edgeWeight : edgeWeights) {
            edgeWeight.update(scaleFactor);
        }
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
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderShapes();
        shapeRenderer.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        stage.dispose();
    }
}
