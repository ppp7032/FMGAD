package com.graph.algorithms;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class EdgeWeight extends Text {
    private final int vertex1;
    private final int vertex2;
    private final Graph graph;

    public EdgeWeight(final Graph graph, final int vertex1, final int vertex2, final String input, final BitmapFont font, final float[] colour, final int alignX, final int alignY, final float scaleFactor) {
        super(input, 0, 0, font, colour, alignX, alignY);
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.graph = graph;
        update(scaleFactor);
    }

    public void update(final float scaleFactor) {
        final float offset = 30; // number of pixels (not counting scaleFactor) above/below
        final float x1 = graph.getXCoordinate(vertex1);
        final float y1 = graph.getYCoordinate(vertex1);
        final float x2 = graph.getXCoordinate(vertex2);
        final float y2 = graph.getYCoordinate(vertex2);
        if (x1 == x2) {
            super.setTextPosition((x1 + offset) * scaleFactor, (y1 + y2) / 2f * scaleFactor, 0, 0);
        } else {
            final float m = (y2 - y1) / (x2 - x1);
            final float midpointX = (x1 + x2) / 2f;
            final float midpointY = (y1 + y2) / 2f;
            super.setTextPosition((midpointX - offset * (float) Math.sin(Math.atan(m))) * scaleFactor, (midpointY + offset * (float) Math.cos(Math.atan(m))) * scaleFactor, 0, 0);
        }
    }
}
