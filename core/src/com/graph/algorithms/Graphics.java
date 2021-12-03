package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class Graphics {
    public static void drawRectangleWithBorder(ShapeRenderer renderer, float x, float y, float width, float height, float borderWidth, float[] colour) {
        renderer.setColor(colour[0], colour[1], colour[2], colour[3]);
        renderer.rect(x, y, width, height);
        renderer.setColor(0, 0, 0, 1);
        renderer.rectLine(x, y, x + width, y, borderWidth);
        renderer.rectLine(x, y, x, y + height, borderWidth);
        renderer.rectLine(x, y + height, x + width, y + height, borderWidth);
        renderer.rectLine(x + width, y, x + width, y + height, borderWidth);
    }

    public static void drawMenu(int numberOfRecords, float scaleFactor, ShapeRenderer shapeRenderer) {
        float width = 514 * scaleFactor;
        float height = 424 * scaleFactor;
        float x = (Gdx.graphics.getWidth() - width) / 2;
        float y = (Gdx.graphics.getHeight() - height) / 2;
        Graphics.drawRectangleWithBorder(shapeRenderer, x, y, width, height, 2 * scaleFactor, new float[]{207f / 255f, 226f / 255f, 243f / 255f, 1});
        if (numberOfRecords > 0) {
            x = 414 * scaleFactor;
            y = 468 * scaleFactor;
            width = 452 * scaleFactor;
            height = 46 * scaleFactor;
            Graphics.drawRectangleWithBorder(shapeRenderer, x, y, width, height, 2 * scaleFactor, new float[]{1f, 229f / 255f, 153f / 255f, 1});
            for (int a = 1; a < numberOfRecords; a++) {
                y -= 61 * scaleFactor;
                Graphics.drawRectangleWithBorder(shapeRenderer, x, y, width, height, 2 * scaleFactor, new float[]{1f, 229f / 255f, 153f / 255f, 1});
            }
        }
    }

    public static Skin generateSkin(BitmapFont font) {
        Skin skin = new Skin();
        skin.add("font", font, BitmapFont.class);
        skin.addRegions(new TextureAtlas("skins/cloud-form/skin/cloud-form-ui.atlas"));
        skin.load(Gdx.files.internal("skins/cloud-form/skin/cloud-form-ui.json"));
        return skin;
    }

    private static float[] rotatePointAboutPoint(float[] point, float[] centre, float angle) {
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

    public static float findScaleFactor() {
        return Gdx.graphics.getHeight() / 720f;
    }

    public static void renderEdge(int graph1, int graph2, ShapeRenderer shapeRenderer, Graph graph, float scaleFactor) {
        shapeRenderer.rectLine(graph.getXCoordinate(graph1) * scaleFactor, graph.getYCoordinate(graph1) * scaleFactor, graph.getXCoordinate(graph2) * scaleFactor, graph.getYCoordinate(graph2) * scaleFactor, 5 * scaleFactor);
        if (graph.isDigraph()) {
            float[][] points = Graphics.arrowHeadGenerator(new float[]{graph.getXCoordinate(graph1), graph.getYCoordinate(graph1)}, new float[]{graph.getXCoordinate(graph2), graph.getYCoordinate(graph2)}, scaleFactor);
            shapeRenderer.triangle(points[0][0], points[0][1], points[1][0], points[1][1], points[2][0], points[2][1]);
        }
    }
}