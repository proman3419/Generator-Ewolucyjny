package agh.ics.oop.proman.Gui;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;

public class ImageHoarder {
    private final static LinkedHashMap<String, Image> imagePathToImage = new LinkedHashMap<>();

    public static void loadImagesDirectory(String imagesDirectoryPath) {
        File directory = new File(imagesDirectoryPath);
        File[] directoryFiles = directory.listFiles();

        if (directoryFiles != null)
            for (File file : directoryFiles)
                loadImage(file.getPath());
    }

    public static void loadImage(String imagePath) {
        Image image = null;
        try {
            image = new Image(new FileInputStream(imagePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageHoarder.imagePathToImage.put(imagePath, image);
    }

    public static Image getImage(String imagePath) {
        return ImageHoarder.imagePathToImage.get(imagePath);
    }
}
