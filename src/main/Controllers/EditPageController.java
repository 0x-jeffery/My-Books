package main.Controllers;

import main.App;
import main.Models.Transaction;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.collections.FXCollections;


/**
 * Controller for the EditPage, is called
 * whenever the user decides to edit a
 * Transaction object and the FXML file
 * is loaded. The setters simply, give
 * the widgets their appropriate data
 * values, the initializer takes care of
 * setting up the functions on the widgets
 * to edit a Transaction.
 */
public class EditPageController {
    @FXML private Label idLabel;
    @FXML private TextField titleTextField;
    @FXML private DatePicker datePicker;
    @FXML private TextField amountTextField;
    @FXML private ChoiceBox<String> accountChoiceBox;
    @FXML private ChoiceBox<String> typeChoiceBox;
    @FXML private ChoiceBox<String> subtypeChoiceBox;
    @FXML private TextArea noteText;

    @FXML private Button quitButton;
    @FXML private Button editButton;

    private Transaction transaction;

    /**
     * Initializes widgets with their appropriate
     * functions and values.
     */
    public void initialize(){
        accountChoiceBox.setItems(FXCollections.observableList(App.ACCOUNTS));
        typeChoiceBox.setItems(FXCollections.observableList(App.TYPES));
        datePicker.setDayCellFactory(param -> this.restrictDate());
        editButton.setOnAction(actionEvent -> {this.editTransaction(); this.quit();});
        quitButton.setOnAction(actionEvent -> this.quit());
    }

    public void setTransaction(Transaction transaction){
        this.transaction = transaction;
    }
    public void setId(String id){
        idLabel.setText(id);
    }
    public void setTitle(String title){
        titleTextField.setText(title);
    }
    public void setDatePicker(String date){
        datePicker.setValue(LocalDate.parse(date));
    }
    public void setAmount(double amount){
        amountTextField.setText(Double.toString(amount));
    }
    public void setAccount(String account){
        accountChoiceBox.setValue(account);
    }
    public void setType(String type){
        typeChoiceBox.setValue(type);
        typeChoiceBox.setOnAction(actionEvent -> {
            if(typeChoiceBox.getSelectionModel().getSelectedItem().equals("INCOME")){
                subtypeChoiceBox.setItems(FXCollections.observableList(App.SUBTYPES_INCOME));
            } else {
                subtypeChoiceBox.setItems(FXCollections.observableList(App.SUBTYPES_EXPENSE));
            }
        });
    }
    public void setSubtype(String subtype){
        subtypeChoiceBox.setValue(subtype);
        if(typeChoiceBox.getSelectionModel().getSelectedItem().equals("INCOME")){
            subtypeChoiceBox.setItems(FXCollections.observableList(App.SUBTYPES_INCOME));
        } else {
            subtypeChoiceBox.setItems(FXCollections.observableList(App.SUBTYPES_EXPENSE));
        }
    }
    public void setNote(String note){
        noteText.setText(note);
    }

    /**
     * Editing a transaction works simply by removing
     * it from the App's view, and adding a new transaction
     * with the SAME transaction ID as the previous.
     */
    private void editTransaction(){
        App.removeTransaction(transaction);
        App.createTransaction(
                new Transaction(
                        transaction.getId(),
                        datePicker.getValue().toString(),
                        titleTextField.getText(),
                        Double.parseDouble(amountTextField.getText()),
                        accountChoiceBox.getValue(),
                        typeChoiceBox.getValue(),
                        subtypeChoiceBox.getValue(),
                        noteText.getText()
                )
        );
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
