package pt.isec.pa.deepsea.ui.utils;

import javafx.scene.image.Image;

import java.util.HashMap;

/**
 * Loads and caches images from {@code resources/images} for the UI.
 */

public class ImageLoader {
    private static final HashMap<String, Image> images = new HashMap<>();

    public static Image getImage(String name) {
        Image image  = images.get(name);
        if (image == null) {
            image = new Image(ImageLoader.class.getResourceAsStream("/images/"+name));
            images.put(name,image);
        }
        return image;
    }
}