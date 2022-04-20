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
    private final Text alertMessage;
    private boolean newVertexClicked = false;
    private boolean newEdgeClicked = false;
    private int firstVertex = -1;
    private int secondVertex = -1;
    private int vertexBeingMoved = -1;
    private boolean alertShowing = false;
    private TextField edgeWeight;
    private boolean viewingHelp = false;

    public NewGraph(final Graph graph) {
        this.graph = graph;
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        alertMessage = new Text("", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, twenty, new float[]{0, 0, 0, 1}, 0, 0, -1);
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
        alertMessage = new Text("", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, twenty, new float[]{0, 0, 0, 1}, 0, 0, -1);
        this.edgeWeights.addAll(edgeWeights);
        this.vertexLabels.addAll(vertexLabels);
        temporaryVertexLabel = new Text(Character.toString((char) (graph.getNumberOfVertices() + 65)), 0, 0, twenty, new float[]{0, 0, 0, 1}, 0, 0, -1);
        GeneralConstructor(twenty, graph.getName());
    }

    private void GeneralConstructor(final BitmapFont twenty, final String graphName) {
        final Skin skin = Graphics.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 15f * scaleFactor, 0));
        edgeWeight = new TextField("0", skin);
        final TextField name = new TextField(graphName, skin);
        final Text menuTitle = new Text("", Gdx.graphics.getWidth() / 2f, 545 * scaleFactor, Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 25f * scaleFactor, 0), new float[]{0, 0, 0, 1}, 0, 0, -1);
        final float y1 = 491.5f;
        final Text[] attributes = new Text[]{new Text("", 16 * scaleFactor + (0.5f * (Gdx.graphics.getWidth() - 452 * scaleFactor)), y1 * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0, -1), new Text("Press 'E' to add a new edge", 16 * scaleFactor + (0.5f * (Gdx.graphics.getWidth() - 452 * scaleFactor)), (y1 - 61f) * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0, -1), new Text("Right click a vertex to delete it", 16 * scaleFactor + (0.5f * (Gdx.graphics.getWidth() - 452 * scaleFactor)), (y1 - 61f * 3) * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0, -1), new Text("Drag a vertex to move it", 16 * scaleFactor + (0.5f * (Gdx.graphics.getWidth() - 452 * scaleFactor)), (y1 - 61f * 2) * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0, -1), new Text("Only integer weights are supported", 16 * scaleFactor + (0.5f * (Gdx.graphics.getWidth() - 452 * scaleFactor)), (y1 - 61f * 4) * scaleFactor, twenty, new float[]{0, 0, 0, 1}, -1, 0, -1)};
        final Skin buttonSkin = Graphics.generateSkin(twenty);
        final TextButton back = new TextButton("Back", buttonSkin, "default");
        final TextButton apply = new TextButton("Apply", buttonSkin, "default");
        final TextButton newVertex = new TextButton("New Vertex", skin, "default");
        final TextButton newEdge = new TextButton("New Edge", skin, "default");
        final TextButton save = new TextButton("Save", skin, "default");
        final TextButton help = new TextButton("Help", skin, "default");
        final TextButton mainMenu = new TextButton("Main Menu", skin, "default");
        final TextButton finish = new TextButton("Finish", skin, "default");
        final TextButton[] sidePanelButtons = new TextButton[]{newVertex, newEdge, save, help, mainMenu, finish};
        temporaryVertexLabel.setVisible(false);


        alertMessage.setVisible(false);

        name.setAlignment(1);
        name.setWidth(124 * scaleFactor);
        name.setHeight(46 * scaleFactor);
        name.setPosition(80f * scaleFactor - name.getWidth() / 2f, 652f * scaleFactor);

        edgeWeight.setAlignment(1);
        edgeWeight.setVisible(false);
        edgeWeight.setX(327 * scaleFactor + attributes[0].getX());
        edgeWeight.setY(479 * scaleFactor);
        edgeWeight.setWidth(88 * scaleFactor);
        edgeWeight.setHeight(24 * scaleFactor);

        menuTitle.setVisible(false);

        for (final Text attribute : attributes) {
            attribute.setVisible(false);
        }

        Graphics.setupBackAndApplyButtons(back, apply, scaleFactor, false);

        newVertex.setWidth(127 * scaleFactor);
        newVertex.setHeight(46 * scaleFactor);
        newVertex.setPosition(80f * scaleFactor - newVertex.getWidth() / 2f, name.getY() - 71 * scaleFactor);

        Graphics.setupButtonBelow(newVertex, newEdge, scaleFactor);

        Graphics.setupButtonBelow(newEdge, save, scaleFactor);

        Graphics.setupBottomButton(finish, scaleFactor);

        Graphics.setupButtonAbove(finish, mainMenu, scaleFactor);

        Graphics.setupButtonAbove(mainMenu, help, scaleFactor);


        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Graphics.mouseInBounds(scaleFactor)) {
                    alertShowing = false;
                    alertMessage.setVisible(false);
                    if (newVertexClicked) {
                        graph.addVertex(x / scaleFactor, y / scaleFactor);
                        newVertexClicked = false;
                        vertexLabels.add(new Text(Character.toString((char) (graph.getNumberOfVertices() + 64)), x, y, twenty, new float[]{0, 0, 0, 1}, 0, 0, -1));
                        temporaryVertexLabel.setVisible(false);
                    } else if (newEdgeClicked) {
                        int clickedVertex = findVertexBeingClicked();
                        if (clickedVertex != -1) {
                            if (firstVertex == -1) {
                                firstVertex = clickedVertex;
                            } else if (firstVertex != clickedVertex && !graph.areTwoVerticesConnected(firstVertex, clickedVertex)) {
                                menuTitle.updateText("Edge Properties");
                                attributes[0].updateText("Edge Weight");
                                secondVertex = clickedVertex;
                                edgeWeight.setVisible(true);
                                menuTitle.setVisible(true);
                                attributes[0].setVisible(true);
                                back.setVisible(true);
                                apply.setVisible(true);
                                for (TextButton sidePanelButton : sidePanelButtons) {
                                    sidePanelButton.setTouchable(Touchable.disabled);
                                }
                                name.setTouchable(Touchable.disabled);
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
                if (newEdgeClicked) {
                    closeEnterEdgeWeightMenu(edgeWeight, menuTitle, attributes[0], back, apply, sidePanelButtons, name);
                } else if (viewingHelp) {
                    toggleHelpMenu(false, sidePanelButtons, back, menuTitle, attributes);
                }
            }
        });
        apply.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int weight = 0;
                boolean isInteger = true;
                try {
                    weight = Integer.parseInt(edgeWeight.getText());
                } catch (NumberFormatException e) {
                    isInteger = false;
                }
                if (isInteger) {
                    if (graph.isDigraph()) {
                        graph.addDirectedEdge(firstVertex, secondVertex, weight);
                    } else {
                        graph.addUndirectedEdge(firstVertex, secondVertex, weight);
                    }
                    edgeWeights.add(new EdgeWeight(graph, firstVertex, secondVertex, Integer.toString(weight), twenty, new float[]{0, 0, 1, 1}, 0, 0, scaleFactor));
                    closeEnterEdgeWeightMenu(edgeWeight, menuTitle, attributes[0], back, apply, sidePanelButtons, name);
                }
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
                if (finaliseGraph()) {
                    graph.saveGraph(name.getText());
                }
            }
        });
        help.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleHelpMenu(true, sidePanelButtons, back, menuTitle, attributes);
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
                if (finaliseGraph()) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadGraph(graph, edgeWeights, vertexLabels));
                }
            }
        });


        stage.addActor(alertMessage);
        stage.addActor(name);
        stage.addActor(edgeWeight);
        stage.addActor(menuTitle);
        for (final Text attribute : attributes) {
            stage.addActor(attribute);
        }
        stage.addActor(back);
        stage.addActor(apply);
        for (final TextButton sidePanelButton : sidePanelButtons) {
            stage.addActor(sidePanelButton);
        }
    }

    private void toggleHelpMenu(final boolean newStatus, final TextButton[] sidePanelButtons, final TextButton back, final Text menuTitle, final Text[] attributes) {
        viewingHelp = newStatus;
        back.setVisible(newStatus);
        menuTitle.setVisible(newStatus);
        for (final Text attribute : attributes) {
            attribute.setVisible(newStatus);
        }
        attributes[0].updateText("Press 'V' to add a new vertex");
        if (newStatus) {
            for (final TextButton sidePanelButton : sidePanelButtons) {
                sidePanelButton.setTouchable(Touchable.disabled);
            }
            menuTitle.updateText("Help");
        } else {
            for (final TextButton sidePanelButton : sidePanelButtons) {
                sidePanelButton.setTouchable(Touchable.enabled);
            }
        }
    }

    private boolean finaliseGraph() {
        if (graph.getNumberOfVertices() > 1) {
            if (graph.isConnected()) {
                return true;
            } else {
                alertShowing = true;
                alertMessage.setVisible(true);
                alertMessage.updateText("Please ensure the\ngraph is connected!");
            }
        } else {
            alertShowing = true;
            alertMessage.setVisible(true);
            alertMessage.updateText("Please ensure the\ngraph contains at\nleast 2 vertices!");
        }
        return false;
    }

    private void clickNewVertex() {
        if (!newEdgeClicked) {
            newVertexClicked = true;
            temporaryVertexLabel.setVisible(true);
            temporaryVertexLabel.updateText(Character.toString((char) (graph.getNumberOfVertices() + 65)));
        }
    }

    private void clickNewEdge() {
        edgeWeight.setText("0");
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

    private void closeEnterEdgeWeightMenu(final TextField edgeWeight, final Text edgeWeightTitle, final Text edgeWeightLabel, final TextButton back, final TextButton apply, final TextButton[] sidePanelButtons, final TextField name) {
        edgeWeight.setVisible(false);
        edgeWeightTitle.setVisible(false);
        edgeWeightLabel.setVisible(false);
        back.setVisible(false);
        apply.setVisible(false);
        for (final TextButton sidePanelButton : sidePanelButtons) {
            sidePanelButton.setTouchable(Touchable.enabled);
        }
        name.setTouchable(Touchable.enabled);
        newEdgeClicked = false;
        firstVertex = -1;
        secondVertex = -1;
    }

    private void deleteVertex(final int vertex) {
        for (int a = edgeWeights.size() - 1; a >= 0; a--) {
            if (edgeWeights.get(a).getVertex1() == vertex || edgeWeights.get(a).getVertex2() == vertex) {
                edgeWeights.remove(a);
            }
        }
        for (EdgeWeight weight : edgeWeights) {
            if (weight.getVertex1() > vertex) {
                weight.decrementVertex1();
            }
            if (weight.getVertex2() > vertex) {
                weight.decrementVertex2();
            }
        }
        graph.deleteVertex(vertex);
        vertexLabels.remove(vertex);
        for (Text vertexLabel : vertexLabels) {
            if (vertexLabel.getText().charAt(0) > vertex + 65) {
                vertexLabel.updateText(Character.toString((char) (vertexLabel.getText().charAt(0) - 1)));
            }
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
        } else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && Graphics.mouseInBounds(scaleFactor) && !newVertexClicked && !newEdgeClicked && !alertShowing) {
            final int vertexBeingClicked = findVertexBeingClicked();
            if (vertexBeingClicked != -1) {
                deleteVertex(vertexBeingClicked);
            }
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
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
            Graphics.renderVertex(shapeRenderer, stage.getBatch(), Gdx.input.getX() / scaleFactor, (Gdx.graphics.getHeight() - Gdx.input.getY()) / scaleFactor, temporaryVertexLabel, scaleFactor);
            shapeRenderer.end();
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
        if (alertShowing) {
            newVertexClicked = false;
            newEdgeClicked = false;
            firstVertex = -1;
            secondVertex = -1;
            vertexBeingMoved = -1;
            Graphics.renderAlert(shapeRenderer, alertMessage, scaleFactor);
        } else if (viewingHelp) {
            newVertexClicked = false;
            newEdgeClicked = false;
            firstVertex = -1;
            secondVertex = -1;
            vertexBeingMoved = -1;
            Graphics.drawMenu(5, scaleFactor, shapeRenderer);
        }
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
