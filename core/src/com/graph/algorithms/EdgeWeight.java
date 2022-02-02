package com.graph.algorithms;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class EdgeWeight extends Text {
    private final int vertex1;
    private final int vertex2;
    private final Graph graph;

    public EdgeWeight(final Graph graph, final int vertex1, final int vertex2, final String input, final BitmapFont font, final float[] colour, final int alignX, final int alignY, final float scaleFactor) {
        super(input, 0, 0, font, colour, alignX, alignY, -1);
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.graph = graph;
        update(scaleFactor);
    }

    public void update(final float scaleFactor) {
        final float offset = 30; // number of pixels (not counting scaleFactor) above/below
        final float x1 = graph.getXCoordinateOfVertex(vertex1);
        final float y1 = graph.getYCoordinateOfVertex(vertex1);
        final float x2 = graph.getXCoordinateOfVertex(vertex2);
        final float y2 = graph.getYCoordinateOfVertex(vertex2);
        if (x1 == x2) {
            super.setTextPosition((x1 + offset) * scaleFactor, (y1 + y2) / 2f * scaleFactor, 0, 0);
        } else {
            final double angle = Math.atan((y1 - y2) / (x1 - x2));
            super.setTextPosition(((x1 + x2) / 2f - offset * (float) Math.sin(angle)) * scaleFactor, ((y1 + y2) / 2f + offset * (float) Math.cos(angle)) * scaleFactor, 0, 0);
        }
    }
}
