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
    final Stage stage = new Stage();

    public MainMenu() {
        stage.addActor(new Image(new Texture(Gdx.files.internal("backgrounds/background2Transparent.png"))));
        stage.addActor(
                new Text("Further Maths Graph Algorithm Demonstrator", Gdx.graphics.getWidth() / 2f, 685, "Segoe UI.fnt"));

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton newGraph = new TextButton("New Graph", skin, "default");
        TextButton loadGraph = new TextButton("Load Graph", skin, "default");
        TextButton exit = new TextButton("Quit", skin, "default");
        newGraph.setWidth(382);
        newGraph.setHeight(65);
        newGraph.setPosition(0.5f * (Gdx.graphics.getWidth() - newGraph.getWidth()), 516f);
        loadGraph.setWidth(382);
        loadGraph.setHeight(65);
        loadGraph.setPosition(0.5f * (Gdx.graphics.getWidth() - newGraph.getWidth()), newGraph.getY() - 171);
        exit.setWidth(382);
        exit.setHeight(65);
        exit.setPosition(0.5f * (Gdx.graphics.getWidth() - newGraph.getWidth()), loadGraph.getY() - 171);
        stage.addActor(newGraph);
        stage.addActor(loadGraph);
        stage.addActor(exit);
        newGraph.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new NewGraph());
            }
        });
        loadGraph.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to screen where you can load a graph!!
            }
        });
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        stage.dispose();

    }

}