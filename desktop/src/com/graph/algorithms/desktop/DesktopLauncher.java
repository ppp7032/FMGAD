package com.graph.algorithms.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.graph.algorithms.Main;
import com.graph.algorithms.Settings;

import java.awt.*;

public class DesktopLauncher {
    public static void main(final String[] args) {
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        String[] displaySettings = Settings.readFromConfigFile();
        if (displaySettings[1].equals("Fullscreen")) {
            final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            config.width = dimension.width;
            config.height = dimension.height;
            config.fullscreen = true;
        } else {
            config.height = Integer.parseInt(displaySettings[0].substring(0, displaySettings[0].length() - 1));
            config.width = config.height * 16 / 9;
        }
        config.resizable = false;
        config.addIcon("icon/OCR_32x32.png", Files.FileType.Internal);
        new LwjglApplication(new Main(), config);
    }
}