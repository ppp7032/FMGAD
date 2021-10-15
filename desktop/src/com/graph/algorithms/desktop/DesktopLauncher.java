package com.graph.algorithms.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.graph.algorithms.Main;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = false;
        config.foregroundFPS = 60;
        config.width = 1280;
        config.height = 720;

        config.addIcon("icon/OCR_128x128.png", Files.FileType.Internal);
        config.addIcon("icon/OCR_32x32.png", Files.FileType.Internal);
        new LwjglApplication(new Main(), config);
    }
}