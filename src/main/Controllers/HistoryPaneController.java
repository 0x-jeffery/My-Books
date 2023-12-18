package main.Controllers;

import main.App;
import main.Models.AppStage;
import main.Models.TableCellFactory;
import main.Models.Transaction;

import java.io.IOException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.util.Callback;


/**
 * Controller for the HistoryPane initializes whenever the
 * FXML file is called. Used to display all transactions
 * within a given range, create a new transaction, delete
 * a transaction, load the table with all Transaction
 * objects as table rows with options to each transaction.
 */
public class HistoryPaneController {
    @FXML private TableView<Transaction> tableView;
    @FXML private TableColumn<Transaction, String> dateColumn;
    @FXML private TableColumn<Transaction, String> titleColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, String> accountColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, String> subtypeColumn;
    @FXML private TableColumn<Transaction, String> noteColumn;
    @FXML private TableColumn<Transaction, MenuButton> buttonColumn;
    @FXML private Label countLabel;
    @FXML private Button loadButton;
    @FXML private Button createTransactionButton;
    @FXML private PieChart incomeChart;
    @FXML private PieChart revenueChart;
    @FXML private PieChart expensesChart;
    @FXML private ChoiceBox<String> pastChoiceBox;

    /**
     * Initialize all components with their proper functions.
     */
    public void initialize(){
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        subtypeColumn.setCellValueFactory(new PropertyValueFactory<>("subtype"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        Callback<TableColumn<Transaction, MenuButton>, TableCell<Transaction, MenuButton>> cellFactory = new TableCellFactory();
        buttonColumn.setCellFactory(cellFactory);
        pastChoiceBox.getItems().addAll("ALL", "30", "90", "180", "360");
        pastChoiceBox.setValue("ALL");
        loadButton.setOnAction(event -> loadComponents());
        createTransactionButton.setOnAction(event -> openCreateTransactionPage());
        loadComponents();
    }

    /**
     * Allows us to load components with correct values that are dependent
     * on the use of the application. i.e. profit.
     */
    private void loadComponents(){
        tableView.getItems().clear();
        revenueChart.getData().clear();
        incomeChart.getData().clear();
        expensesChart.getData().clear();

        double income = 0, expenses = 0;
        double i_work = 0, i_investment = 0, i_other = 0;
        double e_bill = 0, e_fun = 0, e_other = 0;

        boolean all = pastChoiceBox.getValue().equals("ALL");
        String date = "";
        if(!all){
            date = LocalDate.now().minusDays(Long.parseLong(pastChoiceBox.getValue())).toString();
        }

        for(Transaction transaction : App.allTransactions){
            if(!all && transaction.getDate().compareTo(date) <  0){
                break;
            }
            tableView.getItems().add(transaction);
            double amount = transaction.getAmount();
            if(transaction.getType().equals("INCOME")){
                income += amount;
                switch (transaction.getSubtype()) {
                    case "WORK" -> i_work += amount;
                    case "INVESTMENT" -> i_investment += amount;
                    case "OTHER" -> i_other += amount;
                }
            } else if (transaction.getType().equals("EXPENSE")){
                expenses += amount;
                switch (transaction.getSubtype()){
                    case "BILL" -> e_bill += amount;
                    case "FUN" -> e_fun += amount;
                    case "OTHER" -> e_other += amount;
                }
            }
        }

        countLabel.setText(String.format("Count: %d", tableView.getItems().size()));

        revenueChart.getData().add(new PieChart.Data("INC.", income));
        revenueChart.getData().add(new PieChart.Data("EXP.", expenses));

        incomeChart.getData().add(new PieChart.Data("WORK", i_work));
        incomeChart.getData().add(new PieChart.Data("INV.", i_investment));
        incomeChart.getData().add(new PieChart.Data("OTHER", i_other));

        expensesChart.getData().add(new PieChart.Data("BILL", e_bill));
        expensesChart.getData().add(new PieChart.Data("FUN", e_fun));
        expensesChart.getData().add(new PieChart.Data("OTHER", e_other));
    }

    /**
     * Command to open and load CreatePage.fxml to allow
     * user to create a new transaction through a page. This
     * will create a new window and wait for the user to finish
     * and close it.
     */
    private void openCreateTransactionPage(){
        try {
            AppStage appStage = new AppStage("../FXML/CreatePage.fxml");
            appStage.stage.initModality(Modality.APPLICATION_MODAL);
            appStage.stage.showAndWait();
            this.loadComponents();
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
