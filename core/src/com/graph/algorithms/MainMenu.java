package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu implements Screen {
    private final Stage stage = new Stage();

    public MainMenu() {
        float scaleFactor = Graphics.findScaleFactor();
        final Skin skin = Graphics.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 26f * scaleFactor, 0));
        final Image background = new Image(new Texture(Gdx.files.internal("backgrounds/4k.jpeg")));
        final TextButton newGraph = new TextButton("New Graph", skin, "default");
        final TextButton loadGraph = new TextButton("Load Graph", skin, "default");
        final TextButton settings = new TextButton("Settings", skin, "default");
        final TextButton exit = new TextButton("Quit", skin, "default");


        background.setHeight(Gdx.graphics.getHeight());
        background.setWidth(Gdx.graphics.getWidth());

        newGraph.setWidth(382 * scaleFactor);
        newGraph.setHeight(65 * scaleFactor);
        newGraph.setPosition(0.5f * (Gdx.graphics.getWidth() - newGraph.getWidth()), 516f * scaleFactor);

        loadGraph.setWidth(382 * scaleFactor);
        loadGraph.setHeight(65 * scaleFactor);
        loadGraph.setPosition(0.5f * (Gdx.graphics.getWidth() - loadGraph.getWidth()), newGraph.getY() - 126 * scaleFactor);

        settings.setWidth(382 * scaleFactor);
        settings.setHeight(65 * scaleFactor);
        settings.setPosition(0.5f * (Gdx.graphics.getWidth() - loadGraph.getWidth()), loadGraph.getY() - 126 * scaleFactor);

        exit.setWidth(382 * scaleFactor);
        exit.setHeight(65 * scaleFactor);
        exit.setPosition(0.5f * (Gdx.graphics.getWidth() - exit.getWidth()), settings.getY() - 126 * scaleFactor);


        newGraph.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SelectGraphType());
            }
        });
        loadGraph.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SelectGraph());
            }
        });
        settings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Settings());
            }
        });
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });


        stage.addActor(background);
        stage.addActor(newGraph);
        stage.addActor(loadGraph);
        stage.addActor(settings);
        stage.addActor(exit);
        stage.addActor(new Text("Further Maths Graph Algorithm Demonstrator", Gdx.graphics.getWidth() / 2f, 665 * scaleFactor, Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 40f * scaleFactor, (int) scaleFactor), new float[]{1, 1, 1, 1}, 0, 0));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        stage.dispose();
    }

}