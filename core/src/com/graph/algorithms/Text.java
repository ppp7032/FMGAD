package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Text extends Actor {
    private final BitmapFont font;
    private final float[] colour;
    private final int alignX;
    private final int alignY;
    private final float originalX;
    private final float originalY;
    private final float maxWidth;
    private String toPrint;
    private String fullText;

    public Text(final String input, final float x, final float y, final BitmapFont font, final float[] colour, final int alignX, final int alignY, final float maxWidth) { //align -1 means left, 0 means centre, 1 means right
        this.font = font;
        fullText = input;
        this.colour = colour;
        this.alignX = alignX;
        this.alignY = alignY;
        this.originalX = x;
        this.originalY = y;
        this.maxWidth = maxWidth;
        shrinkText();
        setTextPosition(x, y, alignX, alignY);
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

    private void shrinkText() {
        final GlyphLayout glyphLayout = new GlyphLayout(font, fullText);
        int counter = 1;
        toPrint = fullText;
        while (maxWidth != -1 && glyphLayout.width > maxWidth) {
            toPrint = "..." + fullText.substring(counter);
            glyphLayout.setText(font, toPrint);
            counter++;
        }
        super.setWidth(glyphLayout.width);
        super.setHeight(glyphLayout.height);
    }

    public void updateText(String newText) {
        fullText = newText;
        shrinkText();
        super.setPosition(originalX, originalY);
        setTextPosition(super.getX(), super.getY(), alignX, alignY);
    }

    public void setTextPosition(final float x, final float y, final int alignX, final int alignY) {
        switch (alignX) {
            case -1:
                super.setX(x);
                break;
            case 0:
                super.setX(x - super.getWidth() / 2);
                break;
            case 1:
                super.setX(x - super.getWidth());
                break;
        }
        switch (alignY) {
            case -1:
                super.setY(y + super.getHeight());
                break;
            case 0:
                super.setY(y + super.getHeight() / 2);
                break;
            case 1:
                super.setY(y);
                break;
        }
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        font.setColor(colour[0], colour[1], colour[2], colour[3]);
        font.draw(batch, toPrint, super.getX(), super.getY());
    }

    @Override
    public Actor hit(final float x, final float y, final boolean touchable) {
        return null;
    }

}