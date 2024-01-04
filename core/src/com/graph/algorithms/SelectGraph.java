package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.util.ArrayList;

public class SelectGraph implements Screen {

  private final List<String> graphSelector;
  private final Stage stage = new Stage();
  private final ShapeRenderer shapeRenderer = new ShapeRenderer();
  private final Texture background = new Texture(Gdx.files.internal("backgrounds/4k.jpeg"));
  private final SpriteBatch spriteBatch = new SpriteBatch();

  public SelectGraph() {
    graphSelector = new List<>(Graphics.skins[2]);
    final ScrollPane scrollBar = new ScrollPane(graphSelector, Graphics.skins[2], "default");
    final TextButton back = new TextButton("Back", Graphics.skins[2], "default");
    final TextButton load = new TextButton("Load", Graphics.skins[2], "default");
    final TextButton delete = new TextButton("Delete", Graphics.skins[2], "default");

    setListOfGraphs();

    scrollBar.setX((0.5f * (Gdx.graphics.getWidth() - 452 * Graphics.scaleFactor)));
    scrollBar.setY(224f * Graphics.scaleFactor);
    scrollBar.setHeight(290f * Graphics.scaleFactor);
    scrollBar.setWidth(452f * Graphics.scaleFactor);

    Graphics.setupBackAndApplyButtons(back, load, true);

    delete.setX((back.getX() + load.getX()) / 2f);
    delete.setY(back.getY());
    delete.setWidth(back.getWidth());
    delete.setHeight(back.getHeight());

    back.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        ((Main) Gdx.app.getApplicationListener()).setScreen(Main.ScreenKey.MainMenu);
      }
    });
    load.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (graphSelector.getSelected() != null) {
          ((Main) Gdx.app.getApplicationListener()).loadGraph(
              new Graph(Gdx.files.absolute(
                  Settings.getGraphsDirectoryPath() + "/" + graphSelector.getSelected()
                      + ".graph")));
          ((Main) Gdx.app.getApplicationListener()).setScreen(Main.ScreenKey.LoadGraph);
        }
      }
    });
    delete.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Gdx.files.absolute(
                Settings.getGraphsDirectoryPath() + "/" + graphSelector.getSelected() + ".graph")
            .delete();
        final ArrayList<String> newItems = Graphics.getItems(graphSelector);
        newItems.remove(graphSelector.getSelected());
        graphSelector.clearItems();
        graphSelector.setItems(newItems.toArray(new String[0]));
      }
    });

    stage.addActor(scrollBar);
    stage.addActor(back);
    stage.addActor(load);
    stage.addActor(delete);
    Graphics.addTextToMenu(stage, "Graph Selection", new String[]{}, Graphics.fonts[4],
        Graphics.fonts[3]);
  }

  public void setListOfGraphs() {
    final FileHandle graphsDirectory = Gdx.files.absolute(Settings.getGraphsDirectoryPath());
    final FileHandle[] graphs = graphsDirectory.list("graph");
    final String[] graphNames = new String[graphs.length];
    for (int a = 0; a < graphs.length; a++) {
      graphNames[a] = graphs[a].file().getName();
      graphNames[a] = graphNames[a].substring(0, graphNames[a].lastIndexOf("."));
    }
    graphSelector.setItems(graphNames);
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
    setListOfGraphs();
  }

  @Override
  public void render(float delta) {
    Graphics.drawSelectionMenu(spriteBatch, background, shapeRenderer, stage, 0);
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
    stage.dispose();
    shapeRenderer.dispose();
    background.dispose();
    spriteBatch.dispose();
  }
}
