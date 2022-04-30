package com.graph.algorithms;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class EdgeWeight extends Text {
    private final Graph graph;
    private int vertex1;
    private int vertex2;

    public EdgeWeight(final Graph graph, final int vertex1, final int vertex2, final String input, final BitmapFont font, final float[] colour, final int alignX, final int alignY) {
        super(input, 0, 0, font, colour, alignX, alignY, -1);
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.graph = graph;
        update();
    }

    public void update() {
        final float offset = 20; // number of pixels (not counting scaleFactor) above/below
        final float x1 = graph.getXCoordinateOfVertex(vertex1);
        final float y1 = graph.getYCoordinateOfVertex(vertex1);
        final float x2 = graph.getXCoordinateOfVertex(vertex2);
        final float y2 = graph.getYCoordinateOfVertex(vertex2);
        if (x1 == x2) {
            super.setTextPosition((x1 + offset) * Graphics.scaleFactor, (y1 + y2) / 2f * Graphics.scaleFactor, 0, 0);
        } else {
            final double angle = Math.atan((y1 - y2) / (x1 - x2));
            super.setTextPosition(((x1 + x2) / 2f - offset * (float) Math.sin(angle)) * Graphics.scaleFactor, ((y1 + y2) / 2f + offset * (float) Math.cos(angle)) * Graphics.scaleFactor, 0, 0);
        }
    }

    public int getVertex1() {
        return vertex1;
    }

    public int getVertex2() {
        return vertex2;
    }

    public void decrementVertex1() {
        vertex1 -= 1;
    }

    public void decrementVertex2() {
        vertex2 -= 1;
    }
}
