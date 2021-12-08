package com.graph.algorithms;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.text.DecimalFormat;

public class EdgeWeight extends Text {
    private final int vertex1;
    private final int vertex2;
    private final Graph graph;

    public EdgeWeight(final Graph graph, final int vertex1, final int vertex2, final String input, final BitmapFont font, final float[] colour, final int alignX, final int alignY, final float rotationDegree, final float scaleFactor) {
        super(input, 0, 0, font, colour, alignX, alignY, rotationDegree);
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.graph = graph;
        update(scaleFactor);
    }

    public void update(final float scaleFactor) {
        final float w = 30; // number of pixels above/below
        final float x1 = graph.getXCoordinate(vertex1);
        final float y1 = graph.getYCoordinate(vertex1);
        final float x2 = graph.getXCoordinate(vertex2);
        final float y2 = graph.getYCoordinate(vertex2);
        if (x2 != x1) {
            final DecimalFormat df = new DecimalFormat("0.00");
            final float m = Float.parseFloat(df.format((y2 - y1) / (x2 - x1)));
            if (m == 0f) {
                super.setTextPosition((x1 + x2) / 2f * scaleFactor, (y1 + w) * scaleFactor, 0, 0);
            } else {
                final float c = y1 - m * x1;
                final float a = (x1 + x2) / 2f;
                final float b = m * a + c;
                final float f = 1 + (float) Math.pow(m, -2);
                final float g = -2 * a * f;
                final float h = (g * a) / (-2) - w * w;
                final float x;
                final float root = (float) Math.sqrt(g * g - 4 * f * h);
                if (m < 0) {
                    x = (-g + root) / (2 * f) * scaleFactor;
                } else {
                    x = (-g - root) / (2 * f) * scaleFactor;
                }
                final float y = (-x / scaleFactor / m + b + a / m) * scaleFactor;
                super.setTextPosition(x, y, 0, 0);
            }
        } else {
            super.setTextPosition((x1 + w) * scaleFactor, (y1 + y2) / 2f * scaleFactor, 0, 0);
        }
        //super.rotationDegree=;
    }
}
