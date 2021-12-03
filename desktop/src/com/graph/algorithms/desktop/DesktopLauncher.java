package com.graph.algorithms.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.graph.algorithms.Main;

public class DesktopLauncher {
    public static void main(final String[] args) {
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = false;
        config.foregroundFPS = 60;
        config.addIcon("icon/OCR_32x32.png", Files.FileType.Internal);
        new LwjglApplication(new Main(), config);
    }
}