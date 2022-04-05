package com.graph.algorithms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Main extends Game {
    @Override
    public void create() {
        Gdx.graphics.setTitle("FMGAD");
        final String[] config = Settings.readFromConfigFile();
        Graphics.setDisplayMode(config[1], config[0]);
        setScreen(new MainMenu());
    }
}