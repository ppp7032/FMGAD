package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class LoadGraph implements Screen {
    private final Stage stage = new Stage();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final float scaleFactor = Graphics.findScaleFactor();
    private final Graph graph;
    private final ArrayList<EdgeWeight> edgeWeights = new ArrayList<>();
    private Text[][] dijkstraLabels;
    private boolean dijkstraPressed = false;

    public LoadGraph(final Graph graph) { //To-do: make it not make two edgeWeights for every edge on an undirected graph.
        this.graph = graph;
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        graph.addToEdgeWeights(edgeWeights, scaleFactor, twenty);
        dijkstraLabels = new Text[graph.getAdjacencyListSize()][4];
        GeneralConstructor(twenty);
    }

    public LoadGraph(final Graph graph, final ArrayList<EdgeWeight> edgeWeights) {
        this.graph = graph;
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        this.edgeWeights.addAll(edgeWeights);
        dijkstraLabels = new Text[graph.getAdjacencyListSize()][4];
        GeneralConstructor(twenty);
    }

    private void GeneralConstructor(final BitmapFont twenty) {
        final Skin skin = Graphics.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 15f * scaleFactor, 0));
        final TextButton dijsktraButton = new TextButton("Dijsktra's", skin, "default");
        final TextButton mainMenu = new TextButton("Main Menu", skin, "default");
        final TextButton edit = new TextButton("Edit", skin, "default");
        for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
            final float width = 93f * scaleFactor;
            final float height = 64f * scaleFactor;
            final float x = graph.getXCoordinate(a) * scaleFactor - width / 2f;
            final float y = graph.getYCoordinate(a) * scaleFactor - height / 2f;
            BitmapFont small = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 15f * scaleFactor, 0);
            dijkstraLabels[a] = new Text[]{new Text("1", x + width / 6f, y + height / 4f * 3f, small, new float[]{0, 0, 0, 1}, 0, 0), new Text("2", x + width / 2f, y + height / 4f * 3f, small, new float[]{0, 0, 0, 1}, 0, 0), new Text("3", x + width / 6f * 5f, y + height / 4f * 3f, small, new float[]{0, 0, 0, 1}, 0, 0), new Text("4", x + 10f * scaleFactor, y + height / 4f, small, new float[]{0, 0, 0, 1}, -1, 0)};
            for (int b = 0; b < 4; b++) {
                dijkstraLabels[a][b].setVisible(false);
            }
        }


        dijsktraButton.setWidth(127 * scaleFactor);
        dijsktraButton.setHeight(46 * scaleFactor);
        dijsktraButton.setPosition(80f * scaleFactor - dijsktraButton.getWidth() / 2f, 652f * scaleFactor);

        mainMenu.setWidth(127 * scaleFactor);
        mainMenu.setHeight(46 * scaleFactor);
        mainMenu.setPosition(80f * scaleFactor - mainMenu.getWidth() / 2f, 95 * scaleFactor);

        edit.setWidth(127 * scaleFactor);
        edit.setHeight(46 * scaleFactor);
        edit.setPosition(80f * scaleFactor - mainMenu.getWidth() / 2f, mainMenu.getY() - 71 * scaleFactor);


        dijsktraButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dijkstraPressed = !dijkstraPressed;
                for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
                    for (int b = 0; b < 4; b++) {
                        dijkstraLabels[a][b].setVisible(!dijkstraLabels[a][b].isVisible());
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
                ((Game) Gdx.app.getApplicationListener()).setScreen(new NewGraph(graph, edgeWeights));
            }
        });


        stage.addActor(dijsktraButton);
        stage.addActor(mainMenu);
        stage.addActor(edit);
        for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
            for (int b = 0; b < 4; b++) {
                stage.addActor(dijkstraLabels[a][b]);
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
        shapeRenderer.setColor(1, 0, 0, 1);
        for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
            for (int b = 0; b < graph.getNumberOfEdges(a); b++) {
                Graphics.renderEdge(graph.getXCoordinate(a), graph.getYCoordinate(a), graph.getXCoordinate(graph.getVertex(a, b)), graph.getYCoordinate(graph.getVertex(a, b)), shapeRenderer, graph.isDigraph(), scaleFactor);
            }
        }
        shapeRenderer.setColor(0, 0, 0, 1);
        for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
            shapeRenderer.circle(graph.getXCoordinate(a) * scaleFactor, graph.getYCoordinate(a) * scaleFactor, 15 * scaleFactor);
        }
        shapeRenderer.end();
        stage.getBatch().begin();
        for (EdgeWeight edgeWeight : edgeWeights) {
            edgeWeight.update(scaleFactor);
            edgeWeight.draw(stage.getBatch(), 0);
        }
        stage.getBatch().end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (dijkstraPressed) {
            for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
                final float width = 93f * scaleFactor;
                final float height = 64f * scaleFactor;
                final float x = graph.getXCoordinate(a) * scaleFactor - width / 2f;
                final float y = graph.getYCoordinate(a) * scaleFactor - height / 2f;
                shapeRenderer.setColor(1, 1, 1, 1);
                shapeRenderer.rect(x, y, width, height);
                shapeRenderer.setColor(0, 0, 0, 1);
                Graphics.drawRectangleWithBorder(shapeRenderer, x, y, width, height, 2 * scaleFactor, new float[]{1, 1, 1, 1});
                shapeRenderer.rectLine(x, y + height / 2f, x + width, y + height / 2f, 2 * scaleFactor);
                shapeRenderer.rectLine(x + width / 3f, y + height, x + width / 3f, y + height / 2f, 2f * scaleFactor);
                shapeRenderer.rectLine(x + width / 3f * 2f, y + height / 2f, x + width / 3f * 2f, y + height, 2 * scaleFactor);
            }
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
