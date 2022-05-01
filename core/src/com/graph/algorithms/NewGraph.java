package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class NewGraph implements Screen {
    private final TextField name;
    private final Stage stage = new Stage();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final Graph graph;
    private final Text temporaryVertexLabel;
    private final Text alertMessage;
    private final TextField edgeWeight;
    private final ArrayList<EdgeWeight> edgeWeights;
    private final ArrayList<Text> vertexLabels;
    private boolean newVertexClicked = false;
    private boolean newEdgeClicked = false;
    private int firstVertex = -1;
    private int secondVertex = -1;
    private int vertexBeingMoved = -1;
    private boolean viewingHelp = false;

    public NewGraph(final Graph graph, final ArrayList<EdgeWeight> edgeWeights, final ArrayList<Text> vertexLabels) {
        this.graph = graph;
        alertMessage = new Text("", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Graphics.fonts[3], new float[]{0, 0, 0, 1}, 0, 0, -1);
        this.edgeWeights = edgeWeights;
        this.vertexLabels = vertexLabels;
        temporaryVertexLabel = new Text(Character.toString((char) (graph.getNumberOfVertices() + 65)), 0, 0, Graphics.fonts[3], new float[]{0, 0, 0, 1}, 0, 0, -1);
        edgeWeight = new TextField("0", Graphics.skins[1]);
        name = new TextField(graph.getName(), Graphics.skins[1]);
        final Text menuTitle = new Text("", Gdx.graphics.getWidth() / 2f, 545 * Graphics.scaleFactor, Graphics.fonts[4], new float[]{0, 0, 0, 1}, 0, 0, -1);
        final float y1 = 491.5f;
        final Text[] attributes = new Text[]{
                new Text("", 16 * Graphics.scaleFactor + (0.5f * (Gdx.graphics.getWidth() - 452 * Graphics.scaleFactor)), y1 * Graphics.scaleFactor, Graphics.fonts[3], new float[]{0, 0, 0, 1}, -1, 0, -1),
                new Text("Press 'E' to add a new edge", 16 * Graphics.scaleFactor + (0.5f * (Gdx.graphics.getWidth() - 452 * Graphics.scaleFactor)), (y1 - 61f) * Graphics.scaleFactor, Graphics.fonts[3], new float[]{0, 0, 0, 1}, -1, 0, -1),
                new Text("Right click a vertex to delete it", 16 * Graphics.scaleFactor + (0.5f * (Gdx.graphics.getWidth() - 452 * Graphics.scaleFactor)), (y1 - 61f * 3) * Graphics.scaleFactor, Graphics.fonts[3], new float[]{0, 0, 0, 1}, -1, 0, -1),
                new Text("Drag a vertex to move it", 16 * Graphics.scaleFactor + (0.5f * (Gdx.graphics.getWidth() - 452 * Graphics.scaleFactor)), (y1 - 61f * 2) * Graphics.scaleFactor, Graphics.fonts[3], new float[]{0, 0, 0, 1}, -1, 0, -1),
                new Text("Only integer weights are supported", 16 * Graphics.scaleFactor + (0.5f * (Gdx.graphics.getWidth() - 452 * Graphics.scaleFactor)), (y1 - 61f * 4) * Graphics.scaleFactor, Graphics.fonts[3], new float[]{0, 0, 0, 1}, -1, 0, -1)
        };
        final TextButton back = new TextButton("Back", Graphics.skins[2], "default");
        final TextButton apply = new TextButton("Apply", Graphics.skins[2], "default");
        final TextButton newVertex = new TextButton("New Vertex", Graphics.skins[1], "default");
        final TextButton newEdge = new TextButton("New Edge", Graphics.skins[1], "default");
        final TextButton save = new TextButton("Save", Graphics.skins[1], "default");
        final TextButton help = new TextButton("Help", Graphics.skins[1], "default");
        final TextButton mainMenu = new TextButton("Main Menu", Graphics.skins[1], "default");
        final TextButton finish = new TextButton("Finish", Graphics.skins[1], "default");
        final TextButton[] sidePanelButtons = new TextButton[]{newVertex, newEdge, save, help, mainMenu, finish};


        alertMessage.setVisible(false);

        name.setAlignment(1);
        name.setWidth(124 * Graphics.scaleFactor);
        name.setHeight(46 * Graphics.scaleFactor);
        name.setPosition(80f * Graphics.scaleFactor - name.getWidth() / 2f, 652f * Graphics.scaleFactor);

        Graphics.setUpStartVertexInput(edgeWeight, attributes[0].getX());

        for (final Text attribute : attributes) {
            attribute.setVisible(false);
        }

        Graphics.setupBackAndApplyButtons(back, apply, false);

        newVertex.setWidth(127 * Graphics.scaleFactor);
        newVertex.setHeight(46 * Graphics.scaleFactor);
        newVertex.setPosition(80f * Graphics.scaleFactor - newVertex.getWidth() / 2f, name.getY() - 71 * Graphics.scaleFactor);

        Graphics.setupButtonBelow(newVertex, newEdge);

        Graphics.setupButtonBelow(newEdge, save);

        Graphics.setupBottomButton(finish);

        Graphics.setupButtonAbove(finish, mainMenu);

        Graphics.setupButtonAbove(mainMenu, help);


        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (mouseInBoundsForVertex()) {
                    if (newVertexClicked) {
                        graph.addVertex(x / Graphics.scaleFactor, y / Graphics.scaleFactor);
                        newVertexClicked = false;
                        vertexLabels.add(new Text(Character.toString((char) (graph.getNumberOfVertices() + 64)), x, y, Graphics.fonts[3], new float[]{0, 0, 0, 1}, 0, 0, -1));
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
                } else if (y / Graphics.scaleFactor < 350f && secondVertex == -1) {
                    newVertexClicked = false;
                    newEdgeClicked = false;
                    firstVertex = -1;
                }
                if (alertMessage.isVisible() && Graphics.mouseInBounds()) {
                    alertMessage.setVisible(false);
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
                    edgeWeights.add(new EdgeWeight(graph, firstVertex, secondVertex, Integer.toString(weight), Graphics.fonts[3], new float[]{0, 0, 1, 1}, 0, 0));
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
                ((Main) Gdx.app.getApplicationListener()).setScreen(Main.ScreenKey.MainMenu);
            }
        });
        finish.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (finaliseGraph()) {
                    ((Main) Gdx.app.getApplicationListener()).setScreen(Main.ScreenKey.LoadGraph);
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

    public void clear(final boolean newStatus) {
        graph.clear(newStatus);
        vertexLabels.clear();
        edgeWeights.clear();
        temporaryVertexLabel.updateText(Character.toString((char) 65));
        name.setText(graph.getName());
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
                alertMessage.setVisible(true);
                alertMessage.updateText("Please ensure the\ngraph is connected!");
            }
        } else {
            alertMessage.setVisible(true);
            alertMessage.updateText("Please ensure the\ngraph contains at\nleast 2 vertices!");
        }
        return false;
    }

    private void clickNewVertex() {
        if (!newEdgeClicked) {
            newVertexClicked = !newVertexClicked;
            if (newVertexClicked) {
                temporaryVertexLabel.updateText(Character.toString((char) (graph.getNumberOfVertices() + 65)));
            }
        }
    }

    private void clickNewEdge() {
        if (!newVertexClicked) {
            if (!newEdgeClicked) {
                newEdgeClicked = true;
                edgeWeight.setText("0");
            } else if (secondVertex == -1) {
                newEdgeClicked = false;
                firstVertex = -1;
            }
        }
    }

    private boolean mouseInBoundsForVertex() {
        return Gdx.input.getX() / Graphics.scaleFactor - 15 > 160f && Gdx.input.getY() - 15 * Graphics.scaleFactor > 0 && Gdx.input.getY() + 15 * Graphics.scaleFactor < Gdx.graphics.getHeight() && Gdx.input.getX() + 15 * Graphics.scaleFactor < Gdx.graphics.getWidth();
    }

    private int findVertexBeingClicked() {
        for (int a = 0; a < graph.getNumberOfVertices(); a++) {
            final float mouseX = Gdx.input.getX() / Graphics.scaleFactor;
            final float mouseY = (Gdx.graphics.getHeight() - Gdx.input.getY()) / Graphics.scaleFactor;
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
        name.setText(graph.getName());
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(247f / 255f, 247f / 255f, 247f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isButtonPressed(Input.Keys.LEFT)) {
            if (vertexBeingMoved == -1 && !newEdgeClicked && !newVertexClicked && Graphics.mouseInBounds()) {
                vertexBeingMoved = findVertexBeingClicked();
            }
        } else {
            vertexBeingMoved = -1;
        }
        if (vertexBeingMoved != -1 && mouseInBoundsForVertex()) {
            graph.setCoordinates(vertexBeingMoved, new float[]{Gdx.input.getX() / Graphics.scaleFactor, (Gdx.graphics.getHeight() - Gdx.input.getY()) / Graphics.scaleFactor});
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            clickNewVertex();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            clickNewEdge();
        } else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && Graphics.mouseInBounds() && !newVertexClicked && !newEdgeClicked && !alertMessage.isVisible()) {
            final int vertexBeingClicked = findVertexBeingClicked();
            if (vertexBeingClicked != -1) {
                deleteVertex(vertexBeingClicked);
            }
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Graphics.renderGraphEdges(shapeRenderer, graph);
        if (newEdgeClicked && firstVertex != -1 && secondVertex == -1) {
            Graphics.renderEdge(graph.getXCoordinateOfVertex(firstVertex), graph.getYCoordinateOfVertex(firstVertex), Gdx.input.getX() / Graphics.scaleFactor, (Gdx.graphics.getHeight() - Gdx.input.getY()) / Graphics.scaleFactor, shapeRenderer, graph.isDigraph());

        } else if (secondVertex != -1) {
            Graphics.renderEdge(graph.getXCoordinateOfVertex(firstVertex), graph.getYCoordinateOfVertex(firstVertex), graph.getXCoordinateOfVertex(secondVertex), graph.getYCoordinateOfVertex(secondVertex), shapeRenderer, graph.isDigraph());
        }
        Graphics.renderGraphVertices(shapeRenderer, graph, vertexLabels, stage.getBatch(), vertexBeingMoved);
        shapeRenderer.end();
        if (newVertexClicked && mouseInBoundsForVertex()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            Graphics.renderVertex(shapeRenderer, stage.getBatch(), Gdx.input.getX() / Graphics.scaleFactor, (Gdx.graphics.getHeight() - Gdx.input.getY()) / Graphics.scaleFactor, temporaryVertexLabel);
            shapeRenderer.end();
        }
        stage.getBatch().begin();
        for (EdgeWeight edgeWeight : edgeWeights) {
            edgeWeight.update();
            edgeWeight.draw(stage.getBatch(), 0);
        }
        stage.getBatch().end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Graphics.drawRectangleWithBorder(shapeRenderer, Graphics.scaleFactor, 0, 160f * Graphics.scaleFactor, Gdx.graphics.getHeight() - Graphics.scaleFactor, 2f * Graphics.scaleFactor, new float[]{207f / 255f, 226f / 255f, 243f / 255f, 1});
        if (secondVertex != -1) {
            Graphics.drawMenu(1, shapeRenderer);
        }
        if (alertMessage.isVisible()) {
            newVertexClicked = false;
            newEdgeClicked = false;
            firstVertex = -1;
            secondVertex = -1;
            vertexBeingMoved = -1;
            Graphics.renderAlert(shapeRenderer, alertMessage);
        } else if (viewingHelp) {
            newVertexClicked = false;
            newEdgeClicked = false;
            firstVertex = -1;
            secondVertex = -1;
            vertexBeingMoved = -1;
            Graphics.drawMenu(5, shapeRenderer);
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
    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }
}
