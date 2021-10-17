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
    final float scaleFactor = Gdx.graphics.getHeight() / 720f;

    public NewGraph(final float scaleFactor) {
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
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(scaleFactor));
            }
        });
        newVertex.getLabel().setFontScale(0.58f * scaleFactor);
        newVertex.setWidth(127 * scaleFactor);
        newVertex.setHeight(46 * scaleFactor);
        newVertex.setPosition(80f * scaleFactor - newVertex.getWidth() / 2f, 652f * scaleFactor);
        stage.addActor(newVertex);
        newEdge.getLabel().setFontScale(0.58f * scaleFactor);
        newEdge.setWidth(127 * scaleFactor);
        newEdge.setHeight(46 * scaleFactor);
        newEdge.setPosition(80f * scaleFactor - newEdge.getWidth() / 2f, newVertex.getY() - 71 * scaleFactor);
        stage.addActor(newEdge);
        save.getLabel().setFontScale(0.58f * scaleFactor);
        save.setWidth(127 * scaleFactor);
        save.setHeight(46 * scaleFactor);
        save.setPosition(80f * scaleFactor - save.getWidth() / 2f, newEdge.getY() - 71 * scaleFactor);
        stage.addActor(save);
        saveAs.getLabel().setFontScale(0.58f * scaleFactor);
        saveAs.setWidth(127 * scaleFactor);
        saveAs.setHeight(46 * scaleFactor);
        saveAs.setPosition(80f * scaleFactor - saveAs.getWidth() / 2f, save.getY() - 71 * scaleFactor);
        stage.addActor(saveAs);
        finish.getLabel().setFontScale(0.58f * scaleFactor);
        finish.setWidth(127 * scaleFactor);
        finish.setHeight(46 * scaleFactor);
        finish.setPosition(80f * scaleFactor - finish.getWidth() / 2f, saveAs.getY() - 71 * scaleFactor);
        stage.addActor(finish);
        mainMenu.getLabel().setFontScale(0.58f * scaleFactor);
        mainMenu.setWidth(127 * scaleFactor);
        mainMenu.setHeight(46 * scaleFactor);
        mainMenu.setPosition(80f * scaleFactor - mainMenu.getWidth() / 2f, 95 * scaleFactor);
        stage.addActor(mainMenu);
        viewHotkeys.getLabel().setFontScale(0.58f * scaleFactor);
        viewHotkeys.setWidth(127 * scaleFactor);
        viewHotkeys.setHeight(46 * scaleFactor);
        viewHotkeys.setPosition(80f * scaleFactor - viewHotkeys.getWidth() / 2f, mainMenu.getY() - 71 * scaleFactor);
        stage.addActor(viewHotkeys);
    }

    public void renderShapes() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(207f / 255f, 226f / 255f, 243f / 255f, 1);
        shapeRenderer.rect(0, 0, 160f * scaleFactor, 720f * scaleFactor);
        shapeRenderer.setColor(95f / 256f, 96f / 256f, 97f / 256f, 1);
        shapeRenderer.rectLine(0, 0, 0, 720 * scaleFactor, 3 * scaleFactor);
        shapeRenderer.rectLine(0, 720 * scaleFactor, 160 * scaleFactor, 720 * scaleFactor, 4 * scaleFactor);
        shapeRenderer.rectLine(160 * scaleFactor, 720 * scaleFactor, 160 * scaleFactor, 0, 2 * scaleFactor);
        shapeRenderer.rectLine(0, 0, 160 * scaleFactor, 0, 2 * scaleFactor);
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
