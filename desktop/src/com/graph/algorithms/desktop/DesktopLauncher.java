package com.graph.algorithms.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.graph.algorithms.Main;
import com.graph.algorithms.Settings;

import java.awt.*;

public class DesktopLauncher {
    public static void main(final String[] args) {
        final LwjglApplicationConfiguration cnfg = new LwjglApplicationConfiguration();
        String[] config = Settings.readFromConfigFile();
        cnfg.width = Integer.parseInt(config[0].substring(0, config[0].length() - 1));
        cnfg.height = cnfg.width * 9 / 16;
        if (config[1].equals("Fullscreen")) {
            final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            cnfg.width = dimension.width;
            cnfg.height = dimension.height;
            cnfg.fullscreen = true;
        }
        cnfg.resizable = false;
        cnfg.foregroundFPS = 60;
        cnfg.addIcon("icon/OCR_32x32.png", Files.FileType.Internal);
        new LwjglApplication(new Main(), cnfg);
    }
}