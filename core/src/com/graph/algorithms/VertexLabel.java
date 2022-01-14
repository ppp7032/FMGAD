package com.graph.algorithms;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class VertexLabel extends Text {
    private final int vertex;
    private final Graph graph;

    public VertexLabel(final String input, final float x, final float y, final BitmapFont font, final float[] colour, final int alignX, final int alignY, final int vertex, final Graph graph) {
        super(input, x, y, font, colour, alignX, alignY);
        this.vertex = vertex;
        this.graph = graph;
    }

    public void update(final float scaleFactor) {
        super.setTextPosition(graph.getXCoordinate(vertex) * scaleFactor, graph.getYCoordinate(vertex) * scaleFactor, 0, 0);
    }
}
