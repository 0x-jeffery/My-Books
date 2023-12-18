package main.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.util.Objects;
import java.io.IOException;


/**
 * The ContentSwitcher class is the CONTROLLING
 * class for the ContentSwitcher.fxml page. This
 * controlling class is in charge of initializing
 * all buttons in the main application window by
 * assigning loading functions to every button,
 * ensuring a proper page switching within the
 * main application window.
 */
public class ContentSwitcher {
    @FXML private Button homeButton;
    @FXML private Button analysisButton;
    @FXML private Button historyButton;
    @FXML private HBox contentHBox;

    /**
     * Initializes the buttons and their actions
     * in the main app window.
     */
    public void initialize(){
        loadFXMLFile("../FXML/HomePane.fxml"); // Loading home page immediately

        homeButton.setOnAction(actionEvent -> loadFXMLFile("../FXML/HomePane.fxml"));
        homeButton.setGraphic(loadIcon("main/FXML/assets/homeIcon.png"));

        analysisButton.setOnAction(actionEvent -> loadFXMLFile("../FXML/AnalysisPane.fxml"));
        analysisButton.setGraphic(loadIcon("main/FXML/assets/chartIcon.png"));

        historyButton.setOnAction(actionEvent -> loadFXMLFile("../FXML/HistoryPane.fxml"));
        historyButton.setGraphic(loadIcon("main/FXML/assets/bookIcon.png"));
    }

    /**
     * Returns a loadable icon for a JavaFX component.
     * @param iconFilePath - Relative path of icon.
     * @return JavaFX ImageView object
     */
    private ImageView loadIcon(String iconFilePath){
        return new ImageView(new Image(iconFilePath));
    }

    /**
     * Loads page from provided FXML file path and
     * modifies main window application view to
     * display the page.
     * @param FXMLFilePath - Path of FXML file
     */
    private void loadFXMLFile(String FXMLFilePath)
    {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(FXMLFilePath)));
            Pane pane = loader.load();
            if (contentHBox.getChildren().size() == 2) {
                contentHBox.getChildren().remove(1);
            }
            contentHBox.getChildren().add(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
