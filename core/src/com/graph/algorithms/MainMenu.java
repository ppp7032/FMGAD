package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class MainMenu implements Screen {
    final Stage stage;

    public MainMenu() {
        stage = new Stage();
        stage.addActor(new Image(new Texture(Gdx.files.internal("backgrounds/background2Transparent.png"))));
        stage.addActor(
                new Text("Further Maths Graph Algorithm Demonstrator", Gdx.graphics.getWidth() / 2f, 550, "Segoe UI.fnt"));

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton newGraph = new TextButton("New Graph", skin, "default");
        TextButton loadGraph = new TextButton("Load Graph", skin, "default");
        TextButton exit = new TextButton("Exit", skin, "default");
        newGraph.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to screen where you can make a graph!!
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

        Table table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);
        table.setPosition(0, Gdx.graphics.getHeight());
        table.padTop(250);
        table.add(newGraph).padBottom(20);
        table.row();
        table.add(loadGraph).padBottom(20);
        table.row();
        table.add(exit);
        stage.addActor(table);
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