package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Text extends Actor {
    final String toPrint;
    final BitmapFont font;
    final float width;
    final float height;
    final float x;
    final float y;

    public Text(String input, float x, float y, String fontName, float fontSize) {
        font = new BitmapFont(Gdx.files.internal(fontName));
        font.getData().setScale(fontSize / 19f);
        toPrint = input;
        GlyphLayout glyphLayout = new GlyphLayout(font, toPrint);
        width = glyphLayout.width;
        height = glyphLayout.height;
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.draw(batch, toPrint, x - width / 2, y - height / 2);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return null;
    }

}