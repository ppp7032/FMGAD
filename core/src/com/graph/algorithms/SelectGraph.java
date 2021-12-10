package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SelectGraph implements Screen {
    private final Stage stage = new Stage();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final float scaleFactor = Graphics.findScaleFactor();
    private final Texture background = new Texture(Gdx.files.internal("backgrounds/4k.jpeg"));
    private final SpriteBatch spriteBatch = new SpriteBatch();

    public SelectGraph() {
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        final Skin skin = Graphics.generateSkin(twenty);
        final List<String> graphSelector = new List<>(skin);
        final ScrollPane scrollBar = new ScrollPane(graphSelector, skin, "default");
        final TextButton back = new TextButton("Back", skin, "default");
        final TextButton apply = new TextButton("Apply", skin, "default");
        final FileHandle graphsDirectory = Gdx.files.internal("graphs");


        final FileHandle[] graphs = graphsDirectory.list("graph2");
        final String[] graphNames = new String[graphs.length];
        for (int a = 0; a < graphs.length; a++) {
            graphNames[a] = graphs[a].file().getName();
            graphNames[a] = graphNames[a].substring(0, graphNames[a].lastIndexOf("."));
        }
        graphSelector.setItems(graphNames);

        scrollBar.setX(414f * scaleFactor);
        scrollBar.setY(224f * scaleFactor);
        scrollBar.setHeight(290f * scaleFactor);
        scrollBar.setWidth(452f * scaleFactor);

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
                //load the graf init
            }
        });


        stage.addActor(scrollBar);
        stage.addActor(back);
        stage.addActor(apply);
        Graphics.addTextToMenu(stage, "Graph Selection", new String[]{}, scaleFactor, Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 25f * scaleFactor, 0), twenty);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Graphics.drawMenu(0, scaleFactor, shapeRenderer);
        shapeRenderer.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
        shapeRenderer.dispose();
        background.dispose();
        spriteBatch.dispose();
    }
}
