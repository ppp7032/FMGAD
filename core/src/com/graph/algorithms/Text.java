package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Text extends Actor {
    private final String toPrint;
    private final BitmapFont font;
    private final float[] colour;
    private float x;
    private float y;

    public Text(final String input, final float x, final float y, final BitmapFont font, final float[] colour, final int alignX, final int alignY) { //align -1 means left, 0 means centre, 1 means right
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
        font.setColor(colour[0], colour[1], colour[2], colour[3]);
        font.draw(batch, toPrint, x, y);
    }

    @Override
    public Actor hit(final float x, final float y, final boolean touchable) {
        return null;
    }

}