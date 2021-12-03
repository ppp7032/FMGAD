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
    private final float width;
    private final float height;
    private final float x;
    private final float y;
    private final float[] colour;

    public Text(final String input, final float x, final float y, final BitmapFont font, final float[] colour) {
        this.font = font;
        toPrint = input;
        final GlyphLayout glyphLayout = new GlyphLayout(font, toPrint);
        width = glyphLayout.width;
        height = glyphLayout.height;
        this.x = x;
        this.y = y;
        this.colour = colour;
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
    public void draw(Batch batch, float parentAlpha) {
        font.setColor(colour[0], colour[1], colour[2], colour[3]);
        font.draw(batch, toPrint, x - width / 2, y - height / 2);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return null;
    }

}