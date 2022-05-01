package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SelectGraphType implements Screen {
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final Texture background = new Texture(Gdx.files.internal("backgrounds/4k.jpeg"));
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final Stage stage = new Stage();

    public SelectGraphType() {
        final SelectBox<String> graphType = new SelectBox<>(Graphics.skins[0]);
        final TextButton back = new TextButton("Back", Graphics.skins[2], "default");
        final TextButton apply = new TextButton("Apply", Graphics.skins[2], "default");


        graphType.setX(343 * Graphics.scaleFactor + (0.5f * (Gdx.graphics.getWidth() - 452 * Graphics.scaleFactor)));
        graphType.setY(479 * Graphics.scaleFactor);
        graphType.setWidth(88 * Graphics.scaleFactor);
        graphType.setHeight(24 * Graphics.scaleFactor);
        graphType.setItems("Undirected", "Directed");

        Graphics.setupBackAndApplyButtons(back, apply, true);


        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) Gdx.app.getApplicationListener()).setScreen(Main.ScreenKey.MainMenu);
            }
        });

        apply.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (graphType.getSelectedIndex()) {
                    case 0:
                        ((Main) Gdx.app.getApplicationListener()).clearNewGraph(false);
                        break;
                    case 1:
                        ((Main) Gdx.app.getApplicationListener()).clearNewGraph(true);
                        break;
                }
                ((Main) Gdx.app.getApplicationListener()).setScreen(Main.ScreenKey.NewGraph);
            }
        });


        stage.addActor(graphType);
        stage.addActor(back);
        stage.addActor(apply);
        Graphics.addTextToMenu(stage, "New Graph Options", new String[]{"Graph Type"}, Graphics.fonts[4], Graphics.fonts[3]);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(final float delta) {
        Graphics.drawSelectionMenu(spriteBatch, background, shapeRenderer, stage, 1);
    }

    @Override
    public void resize(final int width, final int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        background.dispose();
        spriteBatch.dispose();
        stage.dispose();
    }
}
