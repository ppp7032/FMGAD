package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NewGraph implements Screen {
    private final Stage stage = new Stage();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final float scaleFactor = Gdx.graphics.getHeight() / 720f;
    private final Graph graph = new Graph();
    private boolean newVertexClicked = false;
    private boolean newEdgeClicked = false;
    private int firstVertex = -1;
    private int vertexBeingMoved = -1;

    public NewGraph(final float scaleFactor) { //Todo- Before you can start making a graph, select whether or not it should be a digraph
        Skin skin = new Skin(Gdx.files.internal("skins/flat-earth/skin/flat-earth-ui.json"));
        TextButton newVertex = new TextButton("New Node", skin, "default");
        TextButton newEdge = new TextButton("New Arc", skin, "default");
        TextButton save = new TextButton("Save", skin, "default");
        TextButton saveAs = new TextButton("Save As", skin, "default");
        TextButton finish = new TextButton("Finish", skin, "default");
        TextButton mainMenu = new TextButton("Main Menu", skin, "default");
        TextButton viewHotkeys = new TextButton("View Hotkeys", skin, "default");
        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (mouseInBounds()) {
                    if (newVertexClicked) {
                        graph.addVertex(x/scaleFactor,y/scaleFactor);
                        newVertexClicked = false;
                    } else if (newEdgeClicked) {
                        int clickedVertex = findVertexBeingClicked();
                        if (clickedVertex != -1) {
                            if (firstVertex == -1) {
                                firstVertex = clickedVertex;
                            } else {//Todo- make sure edge being added doesn't connect a vertex to itself, or vertices that are already connected, and add a dialogue with a textfield to allow inputting edge weight.
                                graph.addUndirectedEdge(firstVertex,clickedVertex,0);
                                firstVertex = -1;
                            }
                        }
                        if (firstVertex == -1) {
                            newEdgeClicked = false;
                        }
                    }
                } else if (y / scaleFactor < 350f) {
                    newVertexClicked = false;
                    newEdgeClicked = false;
                    firstVertex = -1;
                }
            }
        });
        mainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(scaleFactor));
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
        newVertex.getLabel().setFontScale(0.75f * scaleFactor);
        newVertex.setWidth(127 * scaleFactor);
        newVertex.setHeight(46 * scaleFactor);
        newVertex.setPosition(80f * scaleFactor - newVertex.getWidth() / 2f, 652f * scaleFactor);
        stage.addActor(newVertex);
        newEdge.getLabel().setFontScale(0.75f * scaleFactor);
        newEdge.setWidth(127 * scaleFactor);
        newEdge.setHeight(46 * scaleFactor);
        newEdge.setPosition(80f * scaleFactor - newEdge.getWidth() / 2f, newVertex.getY() - 71 * scaleFactor);
        stage.addActor(newEdge);
        save.getLabel().setFontScale(0.75f * scaleFactor);
        save.setWidth(127 * scaleFactor);
        save.setHeight(46 * scaleFactor);
        save.setPosition(80f * scaleFactor - save.getWidth() / 2f, newEdge.getY() - 71 * scaleFactor);
        stage.addActor(save);
        saveAs.getLabel().setFontScale(0.75f * scaleFactor);
        saveAs.setWidth(127 * scaleFactor);
        saveAs.setHeight(46 * scaleFactor);
        saveAs.setPosition(80f * scaleFactor - saveAs.getWidth() / 2f, save.getY() - 71 * scaleFactor);
        stage.addActor(saveAs);
        finish.getLabel().setFontScale(0.75f * scaleFactor);
        finish.setWidth(127 * scaleFactor);
        finish.setHeight(46 * scaleFactor);
        finish.setPosition(80f * scaleFactor - finish.getWidth() / 2f, saveAs.getY() - 71 * scaleFactor);
        stage.addActor(finish);
        mainMenu.getLabel().setFontScale(0.75f * scaleFactor);
        mainMenu.setWidth(127 * scaleFactor);
        mainMenu.setHeight(46 * scaleFactor);
        mainMenu.setPosition(80f * scaleFactor - mainMenu.getWidth() / 2f, 95 * scaleFactor);
        stage.addActor(mainMenu);
        viewHotkeys.getLabel().setFontScale(0.75f * scaleFactor);
        viewHotkeys.setWidth(127 * scaleFactor);
        viewHotkeys.setHeight(46 * scaleFactor);
        viewHotkeys.setPosition(80f * scaleFactor - viewHotkeys.getWidth() / 2f, mainMenu.getY() - 71 * scaleFactor);
        stage.addActor(viewHotkeys);
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

    public boolean mouseInBounds() {
        return Gdx.input.getX() / scaleFactor - 15 > 160f && Gdx.input.getY() - 15 * scaleFactor > 0 && Gdx.input.getY() + 15 * scaleFactor < Gdx.graphics.getHeight() && Gdx.input.getX() + 15 * scaleFactor < Gdx.graphics.getWidth();
    }

    public void renderShapes() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(207f / 255f, 226f / 255f, 243f / 255f, 1);
        shapeRenderer.rect(0, 0, 160f * scaleFactor, Gdx.graphics.getHeight());
        shapeRenderer.setColor(95f / 256f, 96f / 256f, 97f / 256f, 1);
        shapeRenderer.rectLine(0, 0, 0, Gdx.graphics.getHeight(), 3 * scaleFactor);
        shapeRenderer.rectLine(0, Gdx.graphics.getHeight(), 160 * scaleFactor, Gdx.graphics.getHeight(), 4 * scaleFactor);
        shapeRenderer.rectLine(160 * scaleFactor, Gdx.graphics.getHeight(), 160 * scaleFactor, 0, 2 * scaleFactor);
        shapeRenderer.rectLine(0, 0, 160 * scaleFactor, 0, 2 * scaleFactor);
        shapeRenderer.setColor(1, 0, 0, 1);
        for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
            for (int b = 0; b < graph.getNumberOfEdges(a); b++) {
                int toVertex = graph.getVertex(a,b);
                shapeRenderer.rectLine(graph.getXCoordinate(a) * scaleFactor, graph.getYCoordinate(a) * scaleFactor, graph.getXCoordinate(toVertex) * scaleFactor, graph.getYCoordinate(toVertex) * scaleFactor, 5 * scaleFactor);
            }
        }
        if (newEdgeClicked && firstVertex != -1) {
            shapeRenderer.rectLine(graph.getXCoordinate(firstVertex) * scaleFactor, graph.getYCoordinate(firstVertex) * scaleFactor, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 5 * scaleFactor);
        }
        shapeRenderer.setColor(0, 0, 0, 1);
        for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
            shapeRenderer.circle(graph.getXCoordinate(a) * scaleFactor, graph.getYCoordinate(a) * scaleFactor, 15 * scaleFactor);
        }
        if (newVertexClicked && mouseInBounds()) {
            shapeRenderer.circle(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 15 * scaleFactor);
        }
        shapeRenderer.end();
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
            if (vertexBeingMoved == -1 && !newEdgeClicked && !newVertexClicked && mouseInBounds()) {
                vertexBeingMoved = findVertexBeingClicked();
            }
        } else {
            vertexBeingMoved = -1;
        }
        if (vertexBeingMoved != -1 && mouseInBounds()) {
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
