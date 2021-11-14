package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Text extends Actor {
    private final String toPrint;
    private final BitmapFont font;
    private final float width;
    private final float height;
    private final float x;
    private final float y;
    private final Color color;

    public Text(String input, float x, float y, BitmapFont font, Color color) {
        this.font = font;
        toPrint = input;
        GlyphLayout glyphLayout = new GlyphLayout(font, toPrint);
        width = glyphLayout.width;
        height = glyphLayout.height;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public static BitmapFont generateFont(String fontName, float size, int borderWidth) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) size;
        parameter.borderWidth = borderWidth;
        parameter.color = Color.WHITE;
        /*parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        parameter.shadowColor = new Color(0, 0.5f, 0, 0.75f);*/
        BitmapFont font = generator.generateFont(parameter); // font size 24 pixels
        generator.dispose();
        return font;
    }

    public static Skin generateSkin(BitmapFont font) {
        Skin skin = new Skin();
        skin.add("font", font, BitmapFont.class);
        skin.addRegions(new TextureAtlas("skins/cloud-form/skin/cloud-form-ui.atlas"));
        skin.load(Gdx.files.internal("skins/cloud-form/skin/cloud-form-ui.json"));
        return skin;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.setColor(color);
        font.draw(batch, toPrint, x - width / 2, y - height / 2);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return null;
    }

}