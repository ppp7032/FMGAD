package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class GraphTypeSelection implements Screen {
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final Texture background = new Texture(Gdx.files.internal("backgrounds/4k.jpeg"));
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final Stage stage = new Stage();
    private final float scaleFactor = Gdx.graphics.getHeight() / 720f;

    public GraphTypeSelection() {
        stage.addActor(new Text("New Graph Options", Gdx.graphics.getWidth() / 2f, 560 * scaleFactor, Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 25f * scaleFactor, 0), new float[]{0, 0, 0, 1}));
        Skin buttonSkin = Text.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0));
        TextButton back = new TextButton("Back", buttonSkin, "default");
        TextButton apply = new TextButton("Apply", buttonSkin, "default");
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });
        back.setWidth(127 * scaleFactor);
        back.setHeight(46 * scaleFactor);
        back.setY(162 * scaleFactor);
        back.setX(414f * scaleFactor);
        apply.setWidth(back.getWidth());
        apply.setHeight(back.getHeight());
        apply.setY(back.getY());
        apply.setX(back.getX() + 325 * scaleFactor);
        stage.addActor(apply);
        stage.addActor(back);
        BitmapFont labelFont = Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0);
        stage.addActor(new Text("Graph Type", 490 * scaleFactor, 505 * scaleFactor, labelFont, new float[]{0, 0, 0, 1}));
        Skin labelSkin = Text.generateSkin(Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 12f * scaleFactor, 0));
        final SelectBox<String> graphType = new SelectBox<>(labelSkin);
        graphType.setX(757 * scaleFactor);
        graphType.setY(479 * scaleFactor);
        graphType.setWidth(88 * scaleFactor);
        graphType.setHeight(24 * scaleFactor);
        graphType.setItems("Undirected", "Directed");
        stage.addActor(graphType);
        apply.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (graphType.getSelectedIndex()) {
                    case 0:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new NewGraph(false));
                        break;
                    case 1:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new NewGraph(true));
                        break;
                }
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

        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();
        float width = 514 * scaleFactor;
        float height = 424 * scaleFactor;
        float x = (Gdx.graphics.getWidth() - width) / 2;
        float y = (Gdx.graphics.getHeight() - height) / 2;
        Settings.drawRectangleWithBorder(shapeRenderer, x, y, width, height, 2 * scaleFactor, new float[]{207f / 255f, 226f / 255f, 243f / 255f, 1});
        x = 414 * scaleFactor;
        y = 468 * scaleFactor;
        width = 452 * scaleFactor;
        height = 46 * scaleFactor;
        Settings.drawRectangleWithBorder(shapeRenderer, x, y, width, height, 2 * scaleFactor, new float[]{1f, 229f / 255f, 153f / 255f, 1});

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
        stage.dispose();
        background.dispose();
        shapeRenderer.dispose();
    }
}
