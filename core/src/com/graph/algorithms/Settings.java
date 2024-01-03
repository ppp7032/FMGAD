package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Settings implements Screen {

  private final Stage stage = new Stage();
  private final ShapeRenderer shapeRenderer = new ShapeRenderer();
  private final Texture background = new Texture(Gdx.files.internal("backgrounds/4k.jpeg"));
  private final SpriteBatch spriteBatch = new SpriteBatch();
  private final Text alertMessage;
  private final SelectBox<String> resolutionBox = new SelectBox<>(Graphics.skins[0]);
  private final SelectBox<String> fullscreenBox = new SelectBox<>(Graphics.skins[0]);

  public Settings() {

    final TextButton back = new TextButton("Back", Graphics.skins[2], "default");
    final TextButton apply = new TextButton("Apply", Graphics.skins[2], "default");
    alertMessage = new Text("Please restart the application\nto apply any changes made!",
        Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Graphics.fonts[3],
        new float[]{0, 0, 0, 1}, 0, 0, -1);

    resolutionBox.setX(376 * Graphics.scaleFactor + (0.5f * (Gdx.graphics.getWidth()
        - 452 * Graphics.scaleFactor)));
    resolutionBox.setY(479 * Graphics.scaleFactor);
    resolutionBox.setWidth(55 * Graphics.scaleFactor);
    resolutionBox.setHeight(24 * Graphics.scaleFactor);
    resolutionBox.setItems("2160p", "1440p", "1080p", "900p", "720p");

    fullscreenBox.setX(resolutionBox.getX() - 33f * Graphics.scaleFactor);
    fullscreenBox.setY(418 * Graphics.scaleFactor);
    fullscreenBox.setWidth(88 * Graphics.scaleFactor);
    fullscreenBox.setHeight(24 * Graphics.scaleFactor);
    fullscreenBox.setItems("Fullscreen", "Windowed");

    setUpBoxes();

    Graphics.setupBackAndApplyButtons(back, apply, true);

    alertMessage.setVisible(false);

    stage.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (Graphics.applyNotPressed(apply, x, y)) {
          alertMessage.setVisible(false);
        }
      }
    });
    back.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        ((Main) Gdx.app.getApplicationListener()).setScreen(Main.ScreenKey.MainMenu);
      }
    });
    apply.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Settings.writeToConfigFile(
            new String[]{resolutionBox.getSelected(), fullscreenBox.getSelected()});
        alertMessage.setVisible(true);
      }
    });

    stage.addActor(resolutionBox);
    stage.addActor(fullscreenBox);
    stage.addActor(back);
    stage.addActor(apply);
    Graphics.addTextToMenu(stage, "Settings", new String[]{"Windowed Resolution", "Display Mode"},
        Graphics.fonts[4], Graphics.fonts[3]);
  }

  public static String[] readFromConfigFile() { //return value representing resolution, then value representing display mode
    Scanner scanner = new Scanner(System.in);
    try {
      scanner = new Scanner(new File("config.txt"));
    } catch (FileNotFoundException ignored) { //This should never happen
    }
    final String[] config = new String[2];
    while (scanner.hasNext()) {
      final String currentLine = scanner.nextLine();
      final int colon = currentLine.indexOf(':');
      final String attribute = currentLine.substring(0, colon);
      final String field = currentLine.substring(colon + 2);
      if (attribute.equals("resolution")) {
        config[0] = field;
      } else {
        config[1] = field;
      }
    }
    scanner.close();
    return config;
  }

  private static void writeToConfigFile(final String[] config) {
    final FileHandle file = Gdx.files.local("config.txt");
    file.writeString("resolution: " + config[0] + "\ndisplay mode: " + config[1], false);
  }

  private void setUpBoxes() {
    final String[] config = Settings.readFromConfigFile();
    resolutionBox.setSelected(config[0]);
    fullscreenBox.setSelected(config[1]);
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
    setUpBoxes();
  }

  @Override
  public void render(final float delta) {
    Graphics.drawSelectionMenu(spriteBatch, background, shapeRenderer, stage, 2);
    if (alertMessage.isVisible()) {
      shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
      Graphics.renderAlert(shapeRenderer, alertMessage);
      shapeRenderer.end();
      stage.getBatch().begin();
      alertMessage.draw(stage.getBatch(), 0);
      stage.getBatch().end();
    }
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
    shapeRenderer.dispose();
    background.dispose();
    spriteBatch.dispose();
  }
}
