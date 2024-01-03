package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu implements Screen {

  private final Stage stage = new Stage();

  public MainMenu() {
    final Image background = new Image(new Texture(Gdx.files.internal("backgrounds/4k.jpeg")));
    final TextButton newGraph = new TextButton("New Graph", Graphics.skins[3], "default");
    final TextButton loadGraph = new TextButton("Load Graph", Graphics.skins[3], "default");
    final TextButton settings = new TextButton("Settings", Graphics.skins[3], "default");
    final TextButton exit = new TextButton("Quit", Graphics.skins[3], "default");

    background.setHeight(Gdx.graphics.getHeight());
    background.setWidth(Gdx.graphics.getWidth());

    newGraph.setWidth(382 * Graphics.scaleFactor);
    newGraph.setHeight(65 * Graphics.scaleFactor);
    newGraph.setPosition(0.5f * (Gdx.graphics.getWidth() - newGraph.getWidth()),
        516f * Graphics.scaleFactor);

    loadGraph.setWidth(382 * Graphics.scaleFactor);
    loadGraph.setHeight(65 * Graphics.scaleFactor);
    loadGraph.setPosition(0.5f * (Gdx.graphics.getWidth() - loadGraph.getWidth()),
        newGraph.getY() - 126 * Graphics.scaleFactor);

    settings.setWidth(382 * Graphics.scaleFactor);
    settings.setHeight(65 * Graphics.scaleFactor);
    settings.setPosition(0.5f * (Gdx.graphics.getWidth() - loadGraph.getWidth()),
        loadGraph.getY() - 126 * Graphics.scaleFactor);

    exit.setWidth(382 * Graphics.scaleFactor);
    exit.setHeight(65 * Graphics.scaleFactor);
    exit.setPosition(0.5f * (Gdx.graphics.getWidth() - exit.getWidth()),
        settings.getY() - 126 * Graphics.scaleFactor);

    newGraph.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        ((Main) Gdx.app.getApplicationListener()).setScreen(Main.ScreenKey.SelectGraphType);
      }
    });
    loadGraph.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        ((Main) Gdx.app.getApplicationListener()).setScreen(Main.ScreenKey.SelectGraph);
      }
    });
    settings.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        ((Main) Gdx.app.getApplicationListener()).setScreen(Main.ScreenKey.Settings);
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
    stage.addActor(new Text("FM Graph Algorithm Demonstrator", Gdx.graphics.getWidth() / 2f,
        665 * Graphics.scaleFactor, Graphics.fonts[6], new float[]{1, 1, 1, 1}, 0, 0, -1));
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
  }

  @Override
  public void dispose() {
    stage.dispose();
  }

}