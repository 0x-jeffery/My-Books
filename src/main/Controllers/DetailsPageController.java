package main.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Controller for the DetailsPage FXML file.
 * All functions listed are called when the
 * FXML file is loaded using the FXML Loader.
 * It simply initializes all widgets, displaying
 * the transaction details, of the transaction
 * selected.
 */
public class DetailsPageController {
    @FXML private Label idLabel;
    @FXML private Label titleLabel;
    @FXML private Label dateLabel;
    @FXML private Label amountLabel;
    @FXML private Label accountLabel;
    @FXML private Label typeLabel;
    @FXML private Label subtypeLabel;
    @FXML private TextArea noteText;

    public void setIdLabel(String id){
        idLabel.setText(id);
    }
    public void setTitleLabel(String title){
        titleLabel.setText(title);
    }
    public void setDateLabel(String date){
        dateLabel.setText(date);
    }
    public void setAmountLabel(double amount){
        amountLabel.setText(Double.toString(amount));
    }
    public void setAccountLabel(String account){
        accountLabel.setText(account);
    }
    public void setTypeLabel(String type){
        typeLabel.setText(type);
    }
    public void setSubtypeLabel(String subtype){
        subtypeLabel.setText(subtype);
    }
    public void setNoteLabel(String note){
        noteText.setText(note);
    }
}
