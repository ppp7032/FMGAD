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
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Scanner;

public class Settings implements Screen {
    private final float scaleFactor = Graphics.findScaleFactor();
    private final Stage stage = new Stage();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final Texture background = new Texture(Gdx.files.internal("backgrounds/4k.jpeg"));
    private final SpriteBatch spriteBatch = new SpriteBatch();

    public Settings() {
        final BitmapFont twenty = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        final Skin labelSkin = Graphics.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 12f * scaleFactor, 0));
        final SelectBox<String> resolutionBox = new SelectBox<>(labelSkin);
        final SelectBox<String> fullscreenBox = new SelectBox<>(labelSkin);
        final Skin buttonSkin = Graphics.generateSkin(twenty);
        final TextButton back = new TextButton("Back", buttonSkin, "default");
        final TextButton apply = new TextButton("Apply", buttonSkin, "default");
        final String[] config = Settings.readFromConfigFile();


        resolutionBox.setX(790 * scaleFactor);
        resolutionBox.setY(479 * scaleFactor);
        resolutionBox.setWidth(55 * scaleFactor);
        resolutionBox.setHeight(24 * scaleFactor);
        resolutionBox.setItems("2160p", "1440p", "1080p", "900p", "720p");
        resolutionBox.setSelected(config[0]);

        fullscreenBox.setX(757 * scaleFactor);
        fullscreenBox.setY(418 * scaleFactor);
        fullscreenBox.setWidth(88 * scaleFactor);
        fullscreenBox.setHeight(24 * scaleFactor);
        fullscreenBox.setItems("Fullscreen", "Windowed");
        fullscreenBox.setSelected(config[1]);

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
                Graphics.setDisplayMode(fullscreenBox.getSelected(), resolutionBox.getSelected());
                Settings.writeToConfigFile(new String[]{resolutionBox.getSelected(), fullscreenBox.getSelected()});
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Settings());
            }
        });


        stage.addActor(resolutionBox);
        stage.addActor(fullscreenBox);
        stage.addActor(back);
        stage.addActor(apply);
        Graphics.addTextToMenu(stage, "Settings", new String[]{"Windowed Resolution", "Display Mode"}, scaleFactor, Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 25f * scaleFactor, 0), twenty);
    }

    public static String[] readFromConfigFile() { //return value representing resolution, then value representing display mode
        final Scanner scanner = new Scanner(Gdx.files.internal("config.txt").read());
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

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Graphics.drawMenu(2, scaleFactor, shapeRenderer);
        shapeRenderer.end();
        stage.act();
        stage.draw();

        //stage.getActors().get(0).setX(stage.getActors().get(0).getX()+1);
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
        shapeRenderer.dispose();
        background.dispose();
        spriteBatch.dispose();
    }
}
