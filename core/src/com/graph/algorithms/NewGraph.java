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
    private final Stage stage = new Stage();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final float scaleFactor = Graphics.findScaleFactor();
    private final Graph graph;
    private final ArrayList<EdgeWeight> edgeWeights = new ArrayList<>();
    private final ArrayList<Text> vertexLabels = new ArrayList<>();
    private final Text temporaryVertexLabel;
    private boolean newVertexClicked = false;
    private boolean newEdgeClicked = false;
    private int firstVertex = -1;
    private int secondVertex = -1;
    private int vertexBeingMoved = -1;

    public NewGraph(final Graph graph) {
        this.graph = graph;
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        graph.addToEdgeWeights(edgeWeights, scaleFactor, twenty);
        FileHandle file = Gdx.files.local("graphs/New Graph.graph");
        int counter = 1;
        while (file.exists()) {
            file = Gdx.files.local("graphs/New Graph (" + counter + ").graph");
            counter++;
        }
        graph.changeName(file.name().substring(0, file.name().lastIndexOf(".")));
        temporaryVertexLabel = new Text(Character.toString((char) (graph.getNumberOfVertices() + 65)), 0, 0, twenty, new float[]{0, 0, 0, 1}, 0, 0, -1);
        GeneralConstructor(twenty, graph.getName());
    }

    public NewGraph(final Graph graph, final ArrayList<EdgeWeight> edgeWeights, final ArrayList<Text> vertexLabels) {
        this.graph = graph;
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        this.edgeWeights.addAll(edgeWeights);
        this.vertexLabels.addAll(vertexLabels);
        temporaryVertexLabel = new Text(Character.toString((char) (graph.getNumberOfVertices() + 65)), 0, 0, twenty, new float[]{0, 0, 0, 1}, 0, 0, -1);
        GeneralConstructor(twenty, graph.getName());
    }

    private void GeneralConstructor(final BitmapFont twenty, final String graphName) {
        final Skin skin = Graphics.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 15f * scaleFactor, 0));
        final TextField name = new TextField(graphName, skin);
        final TextField edgeWeight = new TextField("0", skin);
        final Text edgeWeightTitle = new Text("Edge Properties", Gdx.graphics.getWidth() / 2f, 545 * scaleFactor, Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 25f * scaleFactor, 0), new float[]{0, 0, 0, 1}, 0, 0, -1);
        final Text edgeWeightLabel = new Text("Edge Weight", 430f * scaleFactor, 491.5f * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0, -1);
        final Skin buttonSkin = Graphics.generateSkin(twenty);
        final TextButton back = new TextButton("Back", buttonSkin, "default");
        final TextButton apply = new TextButton("Apply", buttonSkin, "default");
        final TextButton newVertex = new TextButton("New Vertex", skin, "default");
        final TextButton newEdge = new TextButton("New Edge", skin, "default");
        final TextButton save = new TextButton("Save", skin, "default");
        final TextButton mainMenu = new TextButton("Main Menu", skin, "default");
        final TextButton finish = new TextButton("Finish", skin, "default");
        temporaryVertexLabel.setVisible(false);


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

        Graphics.setupBackAndApplyButtons(back, apply, scaleFactor, false);

        newVertex.setWidth(127 * scaleFactor);
        newVertex.setHeight(46 * scaleFactor);
        newVertex.setPosition(80f * scaleFactor - newVertex.getWidth() / 2f, name.getY() - 71 * scaleFactor);

        newEdge.setWidth(127 * scaleFactor);
        newEdge.setHeight(46 * scaleFactor);
        newEdge.setPosition(80f * scaleFactor - newEdge.getWidth() / 2f, newVertex.getY() - 71 * scaleFactor);

        save.setWidth(127 * scaleFactor);
        save.setHeight(46 * scaleFactor);
        save.setPosition(80f * scaleFactor - save.getWidth() / 2f, newEdge.getY() - 71 * scaleFactor);

        Graphics.setupBottomTwoButtons(mainMenu, finish, scaleFactor);


        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Graphics.mouseInBounds(scaleFactor)) {
                    if (newVertexClicked) {
                        graph.addVertex(x / scaleFactor, y / scaleFactor);
                        newVertexClicked = false;
                        vertexLabels.add(new Text(Character.toString((char) (graph.getNumberOfVertices() + 64)), graph.getXCoordinateOfVertex(graph.getNumberOfVertices() - 1) * scaleFactor, graph.getYCoordinateOfVertex(graph.getNumberOfVertices() - 1) * scaleFactor, twenty, new float[]{0, 0, 0, 1}, 0, 0, -1));
                        temporaryVertexLabel.setVisible(false);
                    } else if (newEdgeClicked) {
                        int clickedVertex = findVertexBeingClicked();
                        if (clickedVertex != -1) {
                            if (firstVertex == -1) {
                                firstVertex = clickedVertex;
                            } else {
                                if (firstVertex != clickedVertex && !graph.areTwoVerticesConnected(firstVertex, clickedVertex)) {
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
                                    finish.setTouchable(Touchable.disabled);
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
                closeEnterEdgeWeightMenu(edgeWeight, edgeWeightTitle, edgeWeightLabel, back, apply, newVertex, newEdge, save, finish, mainMenu, name);
            }
        });
        apply.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final int weight = Integer.parseInt(edgeWeight.getText());
                if (graph.isDigraph()) {
                    graph.addDirectedEdge(firstVertex, secondVertex, weight);
                } else {
                    graph.addUndirectedEdge(firstVertex, secondVertex, weight);
                }
                edgeWeights.add(new EdgeWeight(graph, firstVertex, secondVertex, Integer.toString(weight), twenty, new float[]{0, 0, 0, 1}, 0, 0, scaleFactor));
                closeEnterEdgeWeightMenu(edgeWeight, edgeWeightTitle, edgeWeightLabel, back, apply, newVertex, newEdge, save, finish, mainMenu, name);
                edgeWeight.setText("0");
            }
        });
        newVertex.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickNewVertex();
            }
        });
        newEdge.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickNewEdge();
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
        finish.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadGraph(graph, edgeWeights, vertexLabels));
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
        stage.addActor(mainMenu);
        stage.addActor(finish);
    }

    private void clickNewVertex() {
        if (!newEdgeClicked) {
            newVertexClicked = true;
            temporaryVertexLabel.setVisible(true);
            temporaryVertexLabel.updateText(Character.toString((char) (graph.getNumberOfVertices() + 65)));
        }
    }

    private void clickNewEdge() {
        if (!newVertexClicked) {
            newEdgeClicked = true;
        }
    }

    private int findVertexBeingClicked() {
        for (int a = 0; a < graph.getNumberOfVertices(); a++) {
            final float mouseX = Gdx.input.getX() / scaleFactor;
            final float mouseY = (Gdx.graphics.getHeight() - Gdx.input.getY()) / scaleFactor;
            if (Math.pow(mouseX - graph.getXCoordinateOfVertex(a), 2) + Math.pow(mouseY - graph.getYCoordinateOfVertex(a), 2) <= 15 * 15) {
                return a;
            }
        }
        return -1;
    }

    private void renderShapes() {
        Graphics.renderGraphEdges(shapeRenderer, graph, scaleFactor);
        if (newEdgeClicked && firstVertex != -1 && secondVertex == -1) {
            Graphics.renderEdge(graph.getXCoordinateOfVertex(firstVertex), graph.getYCoordinateOfVertex(firstVertex), Gdx.input.getX() / scaleFactor, (Gdx.graphics.getHeight() - Gdx.input.getY()) / scaleFactor, shapeRenderer, graph.isDigraph(), scaleFactor);

        } else if (secondVertex != -1) {
            Graphics.renderEdge(graph.getXCoordinateOfVertex(firstVertex), graph.getYCoordinateOfVertex(firstVertex), graph.getXCoordinateOfVertex(secondVertex), graph.getYCoordinateOfVertex(secondVertex), shapeRenderer, graph.isDigraph(), scaleFactor);
        }
        Graphics.renderGraphVertices(shapeRenderer, graph, scaleFactor, vertexLabels, stage.getBatch(), vertexBeingMoved);
        shapeRenderer.end();
        if (newVertexClicked && Graphics.mouseInBounds(scaleFactor)) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            temporaryVertexLabel.setTextPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 0, 0);
            shapeRenderer.circle(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 15 * scaleFactor);
            shapeRenderer.setColor(247f / 255f, 247f / 255f, 247f / 255f, 1);
            shapeRenderer.circle(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 13 * scaleFactor);
            shapeRenderer.setColor(0, 0, 0, 1);
            shapeRenderer.end();
            stage.getBatch().begin();
            temporaryVertexLabel.draw(stage.getBatch(), 0);
            stage.getBatch().end();
        }
        stage.getBatch().begin();
        for (EdgeWeight edgeWeight : edgeWeights) {
            edgeWeight.update(scaleFactor);
            edgeWeight.draw(stage.getBatch(), 0);
        }
        stage.getBatch().end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Graphics.drawRectangleWithBorder(shapeRenderer, scaleFactor, 0, 160f * scaleFactor, Gdx.graphics.getHeight() - scaleFactor, 2f * scaleFactor, new float[]{207f / 255f, 226f / 255f, 243f / 255f, 1});
        if (secondVertex != -1) {
            Graphics.drawMenu(1, scaleFactor, shapeRenderer);
        }
    }

    private void closeEnterEdgeWeightMenu(final TextField edgeWeight, final Text edgeWeightTitle, final Text edgeWeightLabel, final TextButton back, final TextButton apply, final TextButton newVertex, final TextButton newEdge, final TextButton save, final TextButton finish, final TextButton mainMenu, final TextField name) {
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
        name.setTouchable(Touchable.enabled);
        newEdgeClicked = false;
        firstVertex = -1;
        secondVertex = -1;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(247f / 255f, 247f / 255f, 247f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isButtonPressed(Input.Keys.LEFT)) {
            if (vertexBeingMoved == -1 && !newEdgeClicked && !newVertexClicked && Graphics.mouseInBounds(scaleFactor)) {
                vertexBeingMoved = findVertexBeingClicked();
            }
        } else {
            vertexBeingMoved = -1;
        }
        if (vertexBeingMoved != -1 && Graphics.mouseInBounds(scaleFactor)) {
            graph.setCoordinates(vertexBeingMoved, new float[]{Gdx.input.getX() / scaleFactor, (Gdx.graphics.getHeight() - Gdx.input.getY()) / scaleFactor});
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            clickNewVertex();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            clickNewEdge();
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
        stage.dispose();
        shapeRenderer.dispose();
    }
}
