package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SelectGraphType implements Screen {
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final Texture background = new Texture(Gdx.files.internal("backgrounds/4k.jpeg"));
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final Stage stage = new Stage();
    private final float scaleFactor = Graphics.findScaleFactor();

    public SelectGraphType() {
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        final Skin buttonSkin = Graphics.generateSkin(twenty);
        final SelectBox<String> graphType = new SelectBox<>(Graphics.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 12f * scaleFactor, 0)));
        final TextButton back = new TextButton("Back", buttonSkin, "default");
        final TextButton apply = new TextButton("Apply", buttonSkin, "default");


        graphType.setX(757 * scaleFactor);
        graphType.setY(479 * scaleFactor);
        graphType.setWidth(88 * scaleFactor);
        graphType.setHeight(24 * scaleFactor);
        graphType.setItems("Undirected", "Directed");

        back.setWidth(127 * scaleFactor);
        back.setHeight(46 * scaleFactor);
        back.setY(162 * scaleFactor);
        back.setX(414f * scaleFactor);

        apply.setWidth(back.getWidth());
        apply.setHeight(back.getHeight());
        apply.setY(back.getY());
        apply.setX(back.getX() + 325 * scaleFactor);


        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });

        apply.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (graphType.getSelectedIndex()) {
                    case 0:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new NewGraph(new Graph(false)));
                        break;
                    case 1:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new NewGraph(new Graph(true)));
                        break;
                }
            }
        });


        stage.addActor(graphType);
        stage.addActor(back);
        stage.addActor(apply);
        Graphics.addTextToMenu(stage, "New Graph Options", new String[]{"Graph Type"}, scaleFactor, Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 25f * scaleFactor, 0), twenty);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Graphics.drawMenu(1, scaleFactor, shapeRenderer);
        shapeRenderer.end();
        stage.act();
        stage.draw();

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
        dispose();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        background.dispose();
        spriteBatch.dispose();
        stage.dispose();
    }
}
