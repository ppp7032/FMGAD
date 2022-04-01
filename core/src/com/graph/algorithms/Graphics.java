package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.awt.*;
import java.util.ArrayList;

public abstract class Graphics {
    public static void drawRectangleWithBorder(final ShapeRenderer renderer, final float x, final float y, final float width, final float height, final float borderWidth, final float[] colour) {
        final Color previousColor = new Color(renderer.getColor());
        renderer.setColor(colour[0], colour[1], colour[2], colour[3]);
        renderer.rect(x, y, width, height);
        renderer.setColor(0, 0, 0, 1);
        renderer.rectLine(x, y, x + width, y, borderWidth);
        renderer.rectLine(x, y, x, y + height, borderWidth);
        renderer.rectLine(x, y + height, x + width, y + height, borderWidth);
        renderer.rectLine(x + width, y, x + width, y + height, borderWidth);
        renderer.setColor(previousColor);
    }

    public static float[] setupDijkstraBoxes(final float scaleFactor, final Graph graph, final int vertex) {
        final float width = 93f * scaleFactor;
        final float height = 64f * scaleFactor;
        final float x = graph.getXCoordinateOfVertex(vertex) * scaleFactor - width / 2f;
        final float y = graph.getYCoordinateOfVertex(vertex) * scaleFactor - height / 2f;
        return new float[]{x, y, width, height};
    }

    public static void drawSelectionMenu(final SpriteBatch spriteBatch, final Texture background, final ShapeRenderer shapeRenderer, final Stage stage, final float scaleFactor, final int numberOfAttributes) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Graphics.drawMenu(numberOfAttributes, scaleFactor, shapeRenderer);
        shapeRenderer.end();
        stage.act();
        stage.draw();
    }

    public static void setupBottomTwoButtons(final TextButton button1, final TextButton button2, final float scaleFactor) {
        button1.setWidth(127 * scaleFactor);
        button1.setHeight(46 * scaleFactor);
        button1.setPosition(80f * scaleFactor - button1.getWidth() / 2f, 95 * scaleFactor);
        Graphics.setupButtonBelow(button1, button2, scaleFactor);
    }

    public static void renderGraphEdges(final ShapeRenderer shapeRenderer, final Graph graph, final float scaleFactor) {
        shapeRenderer.setColor(1, 0, 0, 1);
        for (int a = 0; a < graph.getNumberOfVertices(); a++) {
            for (int b = 0; b < graph.getNumberOfEdgesConnectedToVertex(a); b++) {
                Graphics.renderEdge(graph.getXCoordinateOfVertex(a), graph.getYCoordinateOfVertex(a), graph.getXCoordinateOfVertex(graph.getVertex(a, b)), graph.getYCoordinateOfVertex(graph.getVertex(a, b)), shapeRenderer, graph.isDigraph(), scaleFactor);
            }
        }
    }

    private static void renderVertex(final ShapeRenderer shapeRenderer, final Batch batch, final Graph graph, final int vertex, final Text vertexLabel, final float scaleFactor) {
        renderVertex(shapeRenderer, batch, graph.getXCoordinateOfVertex(vertex), graph.getYCoordinateOfVertex(vertex), vertexLabel, scaleFactor);
    }

    public static void renderVertex(final ShapeRenderer shapeRenderer, final Batch batch, final float x, final float y, final Text vertexLabel, final float scaleFactor) {
        shapeRenderer.circle(x * scaleFactor, y * scaleFactor, 15 * scaleFactor);
        shapeRenderer.setColor(247f / 255f, 247f / 255f, 247f / 255f, 1);
        shapeRenderer.circle(x * scaleFactor, y * scaleFactor, 13 * scaleFactor);
        shapeRenderer.end();
        batch.begin();
        vertexLabel.setTextPosition(x * scaleFactor, y * scaleFactor, 0, 0);
        vertexLabel.draw(batch, 0);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
    }

    public static void renderGraphVertices(final ShapeRenderer shapeRenderer, final Graph graph, final float scaleFactor, final ArrayList<Text> vertexLabels, Batch batch, final int vertexBeingMoved) {
        shapeRenderer.setColor(0, 0, 0, 1);
        for (int a = graph.getNumberOfVertices() - 1; a >= 0; a--) {
            if (vertexBeingMoved != a) {
                renderVertex(shapeRenderer, batch, graph, a, vertexLabels.get(a), scaleFactor);
            }
        }
        if (vertexBeingMoved != -1) {
            renderVertex(shapeRenderer, batch, graph, vertexBeingMoved, vertexLabels.get(vertexBeingMoved), scaleFactor);
        }
    }

    public static void renderGraphVertices(final ShapeRenderer shapeRenderer, final Graph graph, final float scaleFactor, final ArrayList<Text> vertexLabels, Batch batch) {
        renderGraphVertices(shapeRenderer, graph, scaleFactor, vertexLabels, batch, -1);
    }

    public static void setupBackAndApplyButtons(final TextButton back, final TextButton apply, final float scaleFactor, final boolean visible) {
        back.setVisible(visible);
        back.setWidth(127 * scaleFactor);
        back.setHeight(46 * scaleFactor);
        back.setY(162 * scaleFactor);
        back.setX(414f * scaleFactor);
        apply.setVisible(visible);
        apply.setWidth(back.getWidth());
        apply.setHeight(back.getHeight());
        apply.setY(back.getY());
        apply.setX(back.getX() + 325 * scaleFactor);
    }

    public static void drawMenu(final int numberOfAttributes, final float scaleFactor, final ShapeRenderer shapeRenderer) {
        float width = 514 * scaleFactor;
        float height = 424 * scaleFactor;
        float x = (Gdx.graphics.getWidth() - width) / 2;
        float y = (Gdx.graphics.getHeight() - height) / 2;
        Graphics.drawRectangleWithBorder(shapeRenderer, x, y, width, height, 2 * scaleFactor, new float[]{207f / 255f, 226f / 255f, 243f / 255f, 1});
        if (numberOfAttributes > 0) {
            x = 414 * scaleFactor;
            y = 468 * scaleFactor;
            width = 452 * scaleFactor;
            height = 46 * scaleFactor;
            Graphics.drawRectangleWithBorder(shapeRenderer, x, y, width, height, 2 * scaleFactor, new float[]{1f, 229f / 255f, 153f / 255f, 1});
            for (int a = 1; a < numberOfAttributes; a++) {
                y -= 61 * scaleFactor;
                Graphics.drawRectangleWithBorder(shapeRenderer, x, y, width, height, 2 * scaleFactor, new float[]{1f, 229f / 255f, 153f / 255f, 1});
            }
        }
    }

    public static Skin generateSkin(final BitmapFont font) {
        final Skin skin = new Skin();
        skin.add("font", font, BitmapFont.class);
        skin.addRegions(new TextureAtlas("skins/cloud-form/skin/cloud-form-ui.atlas"));
        skin.load(Gdx.files.internal("skins/cloud-form/skin/cloud-form-ui.json"));
        return skin;
    }

    private static float[] rotatePointAboutPoint(final float[] point, final float[] centre, final float angle) {
        return new float[]{(float) (Math.cos(angle) * (point[0] - centre[0]) - Math.sin(angle) * (point[1] - centre[1]) + centre[0]), (float) (Math.sin(angle) * (point[0] - centre[0]) + Math.cos(angle) * (point[1] - centre[1]) + centre[1])};
    }

    private static float[][] arrowHeadGenerator(final float[] point1, final float[] point2, final float scaleFactor) {
        final float[] centreOfMass = new float[]{(point1[0] + point2[0]) * scaleFactor / 2, (point1[1] + point2[1]) * scaleFactor / 2};
        final float sideLength = 23.551f * scaleFactor;
        float angle = (float) (Math.acos((point2[1] - point1[1]) / Math.sqrt(Math.pow(point2[0] - point1[0], 2) + Math.pow(point2[1] - point1[1], 2))));
        final float y2 = (float) (centreOfMass[1] - sideLength * Math.sin(Math.toRadians(60)) / 3f);
        final float[][] points = new float[][]{{centreOfMass[0], (float) (centreOfMass[1] + 2f * sideLength * Math.sin(Math.toRadians(60)) / 3f)}, {centreOfMass[0] - sideLength / 2, y2}, {centreOfMass[0] + sideLength / 2, y2}};
        if (point2[0] - point1[0] > 0) {
            angle *= -1;
        }
        for (int c = 0; c < points.length; c++) {
            points[c] = rotatePointAboutPoint(points[c], centreOfMass, angle);
        }
        return points;
    }

    public static float findScaleFactor() {
        return Gdx.graphics.getHeight() / 720f;
    }

    public static void renderEdge(final float x1, final float y1, final float x2, final float y2, final ShapeRenderer shapeRenderer, final Boolean digraphStatus, final float scaleFactor) {
        shapeRenderer.rectLine(x1 * scaleFactor, y1 * scaleFactor, x2 * scaleFactor, y2 * scaleFactor, 5 * scaleFactor);
        if (digraphStatus) {
            final float[][] points = Graphics.arrowHeadGenerator(new float[]{x1, y1}, new float[]{x2, y2}, scaleFactor);
            shapeRenderer.triangle(points[0][0], points[0][1], points[1][0], points[1][1], points[2][0], points[2][1]);
        }
    }

    public static void setDisplayMode(final String fullscreenMode, final String resolution) {
        final int height = Integer.parseInt(resolution.substring(0, resolution.length() - 1));
        final int width = height / 9 * 16;
        final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        if (dimension.width * 9 == dimension.height * 16) {
            if (fullscreenMode.equals("Fullscreen")) {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            } else {
                Gdx.graphics.setWindowedMode(width, height);
            }
        } else {
            Gdx.graphics.setWindowedMode(width, height); //Todo- make the app tell the user fullscreen is not supported for aspect ratios other than 16:9.
        }
    }

    public static void addTextToMenu(final Stage stage, final String title, final String[] attributes, final float scaleFactor, final BitmapFont titleFont, final BitmapFont attributeFont) {
        stage.addActor(new Text(title, Gdx.graphics.getWidth() / 2f, 545 * scaleFactor, titleFont, new float[]{0, 0, 0, 1}, 0, 0, -1));
        float y = 491.5f;
        for (String attribute : attributes) {
            stage.addActor(new Text(attribute, 430 * scaleFactor, y * scaleFactor, attributeFont, new float[]{0, 0, 0, 1}, -1, 0, -1));
            y -= 61f;
        }
    }

    public static boolean mouseInBounds(final float scaleFactor) {
        return Gdx.input.getX() / scaleFactor - 15 > 160f && Gdx.input.getY() - 15 * scaleFactor > 0 && Gdx.input.getY() + 15 * scaleFactor < Gdx.graphics.getHeight() && Gdx.input.getX() + 15 * scaleFactor < Gdx.graphics.getWidth();
    }

    public static ArrayList<String> getItems(List<String> graphSelector) {
        final Object[] currentItems = graphSelector.getItems().items;
        final ArrayList<String> newItems = new ArrayList<>();
        for (final Object o : currentItems) {
            if (o != null) {
                newItems.add((String) o);
            }
        }
        return newItems;
    }

    public static void setupButtonBelow(final TextButton above, final TextButton toSetUp, final float scaleFactor) {
        toSetUp.setWidth(127 * scaleFactor);
        toSetUp.setHeight(46 * scaleFactor);
        toSetUp.setPosition(above.getX(), above.getY() - 71 * scaleFactor);
    }
}