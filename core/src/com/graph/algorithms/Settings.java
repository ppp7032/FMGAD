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
        Skin labelSkin = Graphics.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 12f * scaleFactor, 0));
        final SelectBox<String> resolutionBox = new SelectBox<>(labelSkin);
        final SelectBox<String> fullscreenBox = new SelectBox<>(labelSkin);
        Skin buttonSkin = Graphics.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0));
        TextButton back = new TextButton("Back", buttonSkin, "default");
        TextButton apply = new TextButton("Apply", buttonSkin, "default");
        BitmapFont labelFont = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);


        resolutionBox.setX(790 * scaleFactor);
        resolutionBox.setY(479 * scaleFactor);
        resolutionBox.setWidth(55 * scaleFactor);
        resolutionBox.setHeight(24 * scaleFactor);
        resolutionBox.setItems("2160p", "1440p", "1080p", "900p", "720p");

        fullscreenBox.setX(757 * scaleFactor);
        fullscreenBox.setY(418 * scaleFactor);
        fullscreenBox.setWidth(88 * scaleFactor);
        fullscreenBox.setHeight(24 * scaleFactor);
        fullscreenBox.setItems("Fullscreen", "Windowed");
        int[] config = Settings.readFromConfigFile();
        fullscreenBox.setSelectedIndex(config[1]);
        resolutionBox.setSelectedIndex(config[0]);

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
                switch (fullscreenBox.getSelectedIndex()) {
                    case 1:
                        switch (resolutionBox.getSelectedIndex()) {
                            case 0:
                                Gdx.graphics.setWindowedMode(3840, 2160);
                                break;
                            case 1:
                                Gdx.graphics.setWindowedMode(2560, 1440);
                                break;
                            case 2:
                                Gdx.graphics.setWindowedMode(1920, 1080);
                                break;
                            case 3:
                                Gdx.graphics.setWindowedMode(1600, 900);
                                break;
                            case 4:
                                Gdx.graphics.setWindowedMode(1280, 720);
                                break;
                        }
                        break;
                    case 0:
                        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                        break;
                }
                Settings.writeToConfigFile(new int[]{resolutionBox.getSelectedIndex(), fullscreenBox.getSelectedIndex()});
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Settings());
            }
        });


        stage.addActor(resolutionBox);
        stage.addActor(fullscreenBox);
        stage.addActor(back);
        stage.addActor(apply);
        stage.addActor(new Text("Windowed Resolution", 543 * scaleFactor, 505 * scaleFactor, labelFont, new float[]{0, 0, 0, 1}));
        stage.addActor(new Text("Display Mode", 502 * scaleFactor, 444 * scaleFactor, labelFont, new float[]{0, 0, 0, 1}));
        stage.addActor(new Text("Settings", Gdx.graphics.getWidth() / 2f, 560 * scaleFactor, Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 25f * scaleFactor, 0), new float[]{0, 0, 0, 1}));
    }

    public static int[] readFromConfigFile() { //return value representing resolution, then value representing display mode
        Scanner scanner = new Scanner(Gdx.files.internal("config.txt").read());
        int[] config = new int[2];
        while (scanner.hasNext()) {
            String currentLine = scanner.nextLine();
            int colon = currentLine.indexOf(':');
            String variable = currentLine.substring(0, colon);
            String value = currentLine.substring(colon + 2);
            switch (variable) {
                case ("resolution"):
                    switch (value) {
                        case ("2160p"):
                            config[0] = 0;
                            break;
                        case ("1440p"):
                            config[0] = 1;
                            break;
                        case ("1080p"):
                            config[0] = 2;
                            break;
                        case ("900p"):
                            config[0] = 3;
                            break;
                        case ("720p"):
                            config[0] = 4;
                            break;
                    }
                    break;
                case ("fullscreen"):
                    switch (value) {
                        case ("true"):
                            config[1] = 0;
                            break;
                        case ("false"):
                            config[1] = 1;
                            break;
                    }
                    break;
            }
        }
        return config;
    }

    public static void writeToConfigFile(int[] config) {
        String resolution = "";
        String fullscreen = "";
        switch (config[0]) {
            case (0):
                resolution = "2160p";
                break;
            case (1):
                resolution = "1440p";
                break;
            case (2):
                resolution = "1080p";
                break;
            case (3):
                resolution = "900p";
                break;
            case (4):
                resolution = "720p";
                break;
        }
        switch (config[1]) {
            case (0):
                fullscreen = "true";
                break;
            case (1):
                fullscreen = "false";
                break;
        }
        FileHandle file = Gdx.files.local("config.txt");
        file.writeString("resolution: " + resolution + "\nfullscreen: " + fullscreen, false);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
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
