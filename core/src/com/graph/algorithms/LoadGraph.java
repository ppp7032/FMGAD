package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class LoadGraph implements Screen {
    private final Stage stage = new Stage();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final float scaleFactor = Graphics.findScaleFactor();
    private final Graph graph;
    private final ArrayList<EdgeWeight> edgeWeights = new ArrayList<>();
    private final Text[][] dijkstraLabels;
    private final ArrayList<VertexLabel> vertexLabels = new ArrayList<>();
    TextField startVertexInput;
    TextField endVertexInput;
    private boolean dijkstraPressed = false;
    private boolean dijkstraApplied = false;

    public LoadGraph(final Graph graph) { //To-do: make it not make two edgeWeights for every edge on an undirected graph.
        this.graph = graph;
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        graph.addToEdgeWeights(edgeWeights, scaleFactor, twenty);
        dijkstraLabels = new Text[graph.getAdjacencyListSize()][4];
        GeneralConstructor(twenty);
    }

    public LoadGraph(final Graph graph, final ArrayList<EdgeWeight> edgeWeights, final ArrayList<VertexLabel> vertexLabels) {
        this.graph = graph;
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        this.edgeWeights.addAll(edgeWeights);
        dijkstraLabels = new Text[graph.getAdjacencyListSize()][4];
        this.vertexLabels.addAll(vertexLabels);
        GeneralConstructor(twenty);
    }

    private void GeneralConstructor(final BitmapFont twenty) {
        final Skin skin = Graphics.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 15f * scaleFactor, 0));
        final TextButton dijkstraButton = new TextButton("Dijsktra's", skin, "default");
        final TextButton mainMenu = new TextButton("Main Menu", skin, "default");
        final TextButton edit = new TextButton("Edit", skin, "default");
        final Text dijkstraTitle = new Text("Dijkstra's Algorithm Options", Gdx.graphics.getWidth() / 2f, 545 * scaleFactor, Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 25f * scaleFactor, 0), new float[]{0, 0, 0, 1}, 0, 0);
        final float y1 = 491.5f;
        final Text dijkstraStartVertexLabel = new Text("Start Vertex", 430f * scaleFactor, y1 * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0);
        final Text dijkstraEndVertexLabel = new Text("End Vertex", 430f * scaleFactor, (y1 - 61f) * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0);
        startVertexInput = new TextField("0", skin);
        endVertexInput = new TextField("0", skin);
        final Skin buttonSkin = Graphics.generateSkin(twenty);
        final TextButton back = new TextButton("Back", buttonSkin, "default");
        final TextButton apply = new TextButton("Apply", buttonSkin, "default");
        for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
            final float width = 93f * scaleFactor;
            final float height = 64f * scaleFactor;
            final float x = graph.getXCoordinate(a) * scaleFactor - width / 2f;
            final float y = graph.getYCoordinate(a) * scaleFactor - height / 2f;
            BitmapFont small = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 10f * scaleFactor, 0);
            BitmapFont medium = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 15f * scaleFactor, 0);
            dijkstraLabels[a] = new Text[]{new Text(Character.toString((char) (a + 65)), x + width / 6f, y + height / 4f * 3f, medium, new float[]{0, 0, 0, 1}, 0, 0), new Text("", x + width / 2f, y + height / 4f * 3f, medium, new float[]{0, 0, 0, 1}, 0, 0), new Text("", x + width / 6f * 5f, y + height / 4f * 3f, medium, new float[]{0, 0, 0, 1}, 0, 0), new Text("", x + 10f * scaleFactor, y + height / 4f, small, new float[]{0, 0, 0, 1}, -1, 0)};
            for (int b = 0; b < 4; b++) {
                dijkstraLabels[a][b].setVisible(false);
            }
            vertexLabels.add(new VertexLabel(Character.toString((char) (a + 65)), graph.getXCoordinate(a) * scaleFactor, graph.getYCoordinate(a) * scaleFactor, twenty, new float[]{0, 0, 0, 1}, 0, 0, a, graph));
        }


        dijkstraButton.setWidth(127 * scaleFactor);
        dijkstraButton.setHeight(46 * scaleFactor);
        dijkstraButton.setPosition(80f * scaleFactor - dijkstraButton.getWidth() / 2f, 652f * scaleFactor);

        mainMenu.setWidth(127 * scaleFactor);
        mainMenu.setHeight(46 * scaleFactor);
        mainMenu.setPosition(80f * scaleFactor - mainMenu.getWidth() / 2f, 95 * scaleFactor);

        edit.setWidth(127 * scaleFactor);
        edit.setHeight(46 * scaleFactor);
        edit.setPosition(80f * scaleFactor - mainMenu.getWidth() / 2f, mainMenu.getY() - 71 * scaleFactor);

        dijkstraTitle.setVisible(false);

        dijkstraStartVertexLabel.setVisible(false);

        dijkstraEndVertexLabel.setVisible(false);

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


        dijkstraButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (dijkstraApplied) {
                    dijkstraApplied = false;
                    for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
                        for (int b = 0; b < 4; b++) {
                            dijkstraLabels[a][b].setVisible(false);
                        }
                    }
                    dijkstraButton.setTouchable(Touchable.enabled);
                    mainMenu.setTouchable(Touchable.enabled);
                    edit.setTouchable(Touchable.enabled);
                } else if (!dijkstraPressed) {
                    dijkstraPressed = true;
                    dijkstraTitle.setVisible(!dijkstraTitle.isVisible());
                    dijkstraStartVertexLabel.setVisible(!dijkstraStartVertexLabel.isVisible());
                    dijkstraEndVertexLabel.setVisible(!dijkstraEndVertexLabel.isVisible());
                    startVertexInput.setVisible(!startVertexInput.isVisible());
                    endVertexInput.setVisible(!endVertexInput.isVisible());
                    back.setVisible(!back.isVisible());
                    apply.setVisible(!apply.isVisible());
                    dijkstraButton.setTouchable(Touchable.disabled);
                    mainMenu.setTouchable(Touchable.disabled);
                    edit.setTouchable(Touchable.disabled);
                    startVertexInput.setText("A");
                    endVertexInput.setText("A");
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
                dijkstraPressed = false;
                dijkstraTitle.setVisible(!dijkstraTitle.isVisible());
                dijkstraStartVertexLabel.setVisible(!dijkstraStartVertexLabel.isVisible());
                dijkstraEndVertexLabel.setVisible(!dijkstraEndVertexLabel.isVisible());
                startVertexInput.setVisible(!startVertexInput.isVisible());
                endVertexInput.setVisible(!endVertexInput.isVisible());
                back.setVisible(!back.isVisible());
                apply.setVisible(!apply.isVisible());
                dijkstraButton.setTouchable(Touchable.enabled);
                mainMenu.setTouchable(Touchable.enabled);
                edit.setTouchable(Touchable.enabled);
            }
        });
        apply.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dijkstraPressed = false;
                dijkstraApplied = true;
                dijkstraTitle.setVisible(!dijkstraTitle.isVisible());
                dijkstraStartVertexLabel.setVisible(!dijkstraStartVertexLabel.isVisible());
                dijkstraEndVertexLabel.setVisible(!dijkstraEndVertexLabel.isVisible());
                startVertexInput.setVisible(!startVertexInput.isVisible());
                endVertexInput.setVisible(!endVertexInput.isVisible());
                back.setVisible(!back.isVisible());
                apply.setVisible(!apply.isVisible());
                for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
                    for (int b = 0; b < 4; b++) {
                        dijkstraLabels[a][b].setVisible(true);
                    }
                }
                dijkstraButton.setTouchable(Touchable.enabled);
                mainMenu.setTouchable(Touchable.enabled);
                edit.setTouchable(Touchable.enabled);
            }
        });


        stage.addActor(dijkstraButton);
        stage.addActor(mainMenu);
        stage.addActor(edit);
        stage.addActor(dijkstraTitle);
        stage.addActor(dijkstraStartVertexLabel);
        stage.addActor(dijkstraEndVertexLabel);
        stage.addActor(startVertexInput);
        stage.addActor(endVertexInput);
        stage.addActor(back);
        stage.addActor(apply);
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
            shapeRenderer.setColor(247f / 255f, 247f / 255f, 247f / 255f, 1);
            shapeRenderer.circle(graph.getXCoordinate(a) * scaleFactor, graph.getYCoordinate(a) * scaleFactor, 13 * scaleFactor);
            shapeRenderer.setColor(0, 0, 0, 1);
        }
        shapeRenderer.end();
        stage.getBatch().begin();
        for (EdgeWeight edgeWeight : edgeWeights) {
            edgeWeight.update(scaleFactor);
            edgeWeight.draw(stage.getBatch(), 0);
        }
        for (int a = 0; a < graph.getAdjacencyListSize(); a++) {
            vertexLabels.get(a).draw(stage.getBatch(), 0);
        }
        stage.getBatch().end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (dijkstraPressed) {
            Graphics.drawMenu(2, scaleFactor, shapeRenderer);
        }
        if (dijkstraApplied) {
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
                graph.dijkstra(startVertexInput.getText().charAt(0) - 65, endVertexInput.getText().charAt(0) - 65, dijkstraLabels);
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
