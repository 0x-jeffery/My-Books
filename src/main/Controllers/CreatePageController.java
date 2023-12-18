package main.Controllers;

import main.App;
import main.Models.Transaction;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.collections.FXCollections;


/**
 * Controller for the CreatePage FXML page. Used
 * to initialize components in the CreatePage as
 * well as create a new Transaction object
 */
public class CreatePageController {
    @FXML DatePicker datePicker;
    @FXML TextField titleTextField;
    @FXML TextField amountTextField;
    @FXML ChoiceBox<String> accountChoiceBox;
    @FXML ChoiceBox<String> typeChoiceBox;
    @FXML ChoiceBox<String> subtypeChoiceBox;
    @FXML TextField noteTextField;

    @FXML Button quitButton;
    @FXML Button createButton;

    public void initialize(){
        accountChoiceBox.setItems(FXCollections.observableList(App.ACCOUNTS));
        typeChoiceBox.setItems(FXCollections.observableList(App.TYPES));
        subtypeChoiceBox.setDisable(true);
        typeChoiceBox.setOnAction(actionEvent -> this.typeSelector());
        datePicker.setDayCellFactory(param -> this.restrictDate());
        createButton.setOnAction(actionEvent -> {
            createTransaction();
            this.quit();
        });
        quitButton.setOnAction(actionEvent -> this.quit());
    }

    /**
     * Using values from the widgets, it creates a
     * Transaction object using the main App.
     */
    private void createTransaction(){
        // TODO: Might have to change this, maybe use the DBHandler.
        Transaction transaction = new Transaction(
                datePicker.getValue().toString(),
                titleTextField.getText(),
                Double.parseDouble(amountTextField.getText()),
                accountChoiceBox.getValue(),
                typeChoiceBox.getValue(),
                subtypeChoiceBox.getValue(),
                noteTextField.getText()
        );
        App.createTransaction(transaction);
    }

    /**
     * Updates the subtypeCheckBox widget depending
     * on the value of the typeCheckBox.
     */
    private void typeSelector(){
        if(typeChoiceBox.getSelectionModel().getSelectedItem().equals("INCOME")){
            subtypeChoiceBox.setDisable(false);
            subtypeChoiceBox.setItems(FXCollections.observableList(App.SUBTYPES_INCOME));
        } else {
            subtypeChoiceBox.setDisable(false);
            subtypeChoiceBox.setItems(FXCollections.observableList(App.SUBTYPES_EXPENSE));
        }
    }

    /**
     * Function restricts the datePicker widgets
     * to have a max-date of the current date. Used
     * to set the datePicker widgets with the
     * returned DateCell obj.
     * @return DateCell
     */
    private DateCell restrictDate(){
        return new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        };
    }

    /**
     * Quits out of the Window, closing it. Used
     * for the Quit button and after a Transaction
     * is created.
     */
    private void quit(){
        ((Stage) quitButton.getScene().getWindow()).close();
    }

}
