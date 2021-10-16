package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NewGraph implements Screen {
    final Stage stage = new Stage();
    final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public NewGraph() {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton newVertex = new TextButton("New Vertex", skin, "default");
        TextButton newEdge = new TextButton("New Edge", skin, "default");
        TextButton save = new TextButton("Save", skin, "default");
        TextButton saveAs = new TextButton("Save As", skin, "default");
        TextButton finish = new TextButton("Finish", skin, "default");
        TextButton mainMenu = new TextButton("Main Menu", skin, "default");
        TextButton viewHotkeys = new TextButton("View Hotkeys", skin, "default");
        mainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });
        newVertex.getLabel().setFontScale(0.58f);
        newVertex.setWidth(127);
        newVertex.setHeight(46);
        newVertex.setPosition(80f - newVertex.getWidth() / 2f, 652f);
        stage.addActor(newVertex);
        newEdge.getLabel().setFontScale(0.58f);
        newEdge.setWidth(127);
        newEdge.setHeight(46);
        newEdge.setPosition(80f - newEdge.getWidth() / 2f, newVertex.getY() - 71);
        stage.addActor(newEdge);
        save.getLabel().setFontScale(0.58f);
        save.setWidth(127);
        save.setHeight(46);
        save.setPosition(80f - save.getWidth() / 2f, newEdge.getY() - 71);
        stage.addActor(save);
        saveAs.getLabel().setFontScale(0.58f);
        saveAs.setWidth(127);
        saveAs.setHeight(46);
        saveAs.setPosition(80f - saveAs.getWidth() / 2f, save.getY() - 71);
        stage.addActor(saveAs);
        finish.getLabel().setFontScale(0.58f);
        finish.setWidth(127);
        finish.setHeight(46);
        finish.setPosition(80f - finish.getWidth() / 2f, saveAs.getY() - 71);
        stage.addActor(finish);
        mainMenu.getLabel().setFontScale(0.58f);
        mainMenu.setWidth(127);
        mainMenu.setHeight(46);
        mainMenu.setPosition(80f - mainMenu.getWidth() / 2f, 95);
        stage.addActor(mainMenu);
        viewHotkeys.getLabel().setFontScale(0.58f);
        viewHotkeys.setWidth(127);
        viewHotkeys.setHeight(46);
        viewHotkeys.setPosition(80f - viewHotkeys.getWidth() / 2f, mainMenu.getY() - 71);
        stage.addActor(viewHotkeys);
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
