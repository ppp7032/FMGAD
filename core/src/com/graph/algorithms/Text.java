package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Text extends Actor {
    private final String toPrint;
    private final BitmapFont font;
    private final float[] colour;
    private final Matrix4 translationToOrigin;
    private final Matrix4 translationFromOrigin;
    private float x;
    private float y;
    private float rotationDegree;

    public Text(final String input, final float x, final float y, final BitmapFont font, final float[] colour, final int alignX, final int alignY, final float rotationDegree) { //align -1 means left, 0 means centre, 1 means right
        this.font = font;
        toPrint = input;
        final GlyphLayout glyphLayout = new GlyphLayout(font, toPrint);
        float width = glyphLayout.width;
        float height = glyphLayout.height;
        this.colour = colour;
        switch (alignX) {
            case -1:
                this.x = x;
                break;
            case 0:
                this.x = x - width / 2;
                break;
            case 1:
                this.x = x - width;
                break;
        }
        switch (alignY) {
            case -1:
                this.y = y + height;
                break;
            case 0:
                this.y = y + height / 2;
                break;
            case 1:
                this.y = y;
                break;
        }
        this.rotationDegree = rotationDegree;
        translationToOrigin = (new Matrix4()).setToTranslation(-this.x - width / 2f, -this.y + height / 2f, 0);
        translationFromOrigin = (new Matrix4()).setToTranslation(this.x + width / 2f, this.y - height / 2f, 0);
    }

    public static BitmapFont generateFont(final String fontName, final float size, final int borderWidth) {
        final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) size;
        parameter.borderWidth = borderWidth;
        /*parameter.color = Color.WHITE;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        parameter.shadowColor = new Color(0, 0.5f, 0, 0.75f);*/
        final BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        final Matrix4 oldMatrix = batch.getTransformMatrix().cpy();
        final Matrix4 rotationMatrix = (new Matrix4()).setToRotation(0, 0, 1, rotationDegree);
        rotationMatrix.mulLeft(translationFromOrigin);
        rotationMatrix.mul(translationToOrigin);
        batch.setTransformMatrix(rotationMatrix);
        font.setColor(colour[0], colour[1], colour[2], colour[3]);
        font.draw(batch, toPrint, x, y);
        batch.setTransformMatrix(oldMatrix);
        /*if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) rotationDegree += 1;
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) rotationDegree -= 1;
        }*/
    }

    @Override
    public Actor hit(final float x, final float y, final boolean touchable) {
        return null;
    }

}