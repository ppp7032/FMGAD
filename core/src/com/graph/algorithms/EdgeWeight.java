package com.graph.algorithms;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class EdgeWeight extends Text {
    private final int vertex1;
    private final int vertex2;
    private final Graph graph;

    public EdgeWeight(final Graph graph, final int vertex1, final int vertex2, final String input, final float x, final float y, final BitmapFont font, final float[] colour, final int alignX, final int alignY, final float rotationDegree) {
        super(input, x, y, font, colour, alignX, alignY, rotationDegree);
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.graph = graph;
    }

    public void update(final float scaleFactor) {
        super.setTextPosition((graph.getXCoordinate(vertex1) + graph.getXCoordinate(vertex2)) / 2f * scaleFactor, (graph.getYCoordinate(vertex1) + graph.getYCoordinate(vertex2)) / 2f * scaleFactor, 0, 0);
        //super.rotationDegree=;
    }
}
