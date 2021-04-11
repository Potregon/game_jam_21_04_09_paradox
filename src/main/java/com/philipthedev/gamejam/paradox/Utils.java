package com.philipthedev.gamejam.paradox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public final class Utils {

    private Utils() {
        //DO NOT INSTANTIATE
    }

    public static BufferedImage loadImage(Class<?> clazz, String name) {
        try {
            return ImageIO.read(clazz.getResourceAsStream(name));
        } catch (IOException e) {
            System.err.println("Image \"" + name + "\" not found!");
            e.printStackTrace();
            return null;
        }
    }

}
