package main.Models;

import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

import java.util.Objects;
import java.io.IOException;


/**
 * The AppStage class loads an FXML resource, in a
 * completely new JavaFX Stage app window, and
 * adds the app icon to the loaded stage.
 *
 * The FXML resource will initialize itself when
 * called, setting up the page, loading components
 * with its own loaded values.
 */
public class AppStage {
    //TODO: Might change this to private and have accessing methods
    public FXMLLoader loader;
    public Stage stage;

    /**
     * Constructor for an AppStage object.
     * @param FXMLFilePath - Path to URL of .fxml file
     * @throws IOException - Thrown when path is invalid
     */
    public AppStage(String FXMLFilePath) throws IOException {
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(FXMLFilePath)));
        stage = loader.load();
        stage.getIcons().add(new Image("main/FXML/assets/icon.png"));
    }
}
