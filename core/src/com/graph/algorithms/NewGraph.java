package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class NewGraph implements Screen {
    final Stage stage = new Stage();
    final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public NewGraph() {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton newVertex = new TextButton("New Vertex", skin, "default");
        TextButton newEdge = new TextButton(" New Edge ", skin, "default");
        newVertex.setTransform(true);
        newVertex.setScale(0.75f);
        newEdge.setTransform(true);
        newEdge.setScale(0.75f);
        TextButton save = new TextButton(" Save Graph", skin, "default");
        save.setTransform(true);
        save.setScale(0.75f);
        TextButton saveAs = new TextButton("   Save As   ", skin, "default");
        saveAs.setTransform(true);
        saveAs.setScale(0.75f);
        TextButton finish = new TextButton("    Finish    ", skin, "default");
        finish.setTransform(true);
        finish.setScale(0.75f);
        TextButton mainMenu = new TextButton(" Main Menu ", skin, "default");
        mainMenu.setTransform(true);
        mainMenu.setScale(0.75f);
        TextButton viewHotkeys = new TextButton("View Hotkeys", skin, "default");
        viewHotkeys.setTransform(true);
        viewHotkeys.setScale(0.75f);
        mainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });


        Table table = new Table();
        table.align(Align.left | Align.top);
        table.setPosition(0, Gdx.graphics.getHeight());
        table.padLeft(5);
        table.padTop(20);
        table.add(newVertex).padBottom(30);
        table.row();

        table.add(newEdge).padBottom(30);
        table.row();
        table.add(save).padBottom(30);
        table.row();
        table.add(saveAs).padBottom(30);

        table.row();
        table.add(finish).padBottom(230);
        table.row();
        table.add(mainMenu).padBottom(30);
        table.row();
        table.add(viewHotkeys);
        stage.addActor(table);
    }

    public void renderShapes() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(207f / 255f, 226f / 255f, 243f / 255f, 1);
        shapeRenderer.rect(0, 0, 160, 720f);
        shapeRenderer.setColor(95f / 256f, 96f / 256f, 97f / 256f, 1);
        shapeRenderer.rectLine(0, 0, 0, 720, 3);
        shapeRenderer.rectLine(0, 720, 160, 720, 4);
        shapeRenderer.rectLine(160, 720, 160, 0, 2);
        shapeRenderer.rectLine(0, 0, 160, 0, 2);
        shapeRenderer.end();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(247f / 255f, 247f / 255f, 247f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderShapes();
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

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        stage.dispose();
    }
}
