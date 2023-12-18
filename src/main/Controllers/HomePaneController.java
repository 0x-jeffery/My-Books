package main.Controllers;

import main.App;
import main.Models.Transaction;
import main.Models.TableCellFactory;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.util.Callback;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;
import java.time.LocalDate;


/**
 * Controller for main application page and opening page of the entire application, where
 * it mainly displays broad details about your accounts, balances, recent account activity,
 * profit/loss for the month, etc. All components on home page are loaded within this
 * class.
 */
public class HomePaneController {
    @FXML private Label checkingLabel;
    @FXML private Label savingsLabel;
    @FXML private Label investingLabel;
    @FXML private Label totalLabel;

    @FXML private TableView<Transaction> tableView;
    @FXML private TableColumn<Transaction, String> dateColumn;
    @FXML private TableColumn<Transaction, String> titleColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, String> accountColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, String> subtypeColumn;
    @FXML private TableColumn<Transaction, String> noteColumn;
    @FXML private TableColumn<Transaction, MenuButton> buttonColumn;

    @FXML private Label rt1d; @FXML Label rt1t;
    @FXML private Label rt2d; @FXML Label rt2t;
    @FXML private Label rt3d; @FXML Label rt3t;
    @FXML private Label rt4d; @FXML Label rt4t;
    @FXML private Label rt5d; @FXML Label rt5t;

    @FXML private PieChart monthChart;
    @FXML private Label plLabel;

    @FXML private LineChart<Number, Number> lineChart;
    private final XYChart.Series<Number, Number> series = new XYChart.Series<>();

    /**
     * Initializing components on Home Page when page is called by loader.
     */
    public void initialize(){
        // Setting up TableView columns and button for each transaction row
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        subtypeColumn.setCellValueFactory(new PropertyValueFactory<>("subtype"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        Callback<TableColumn<Transaction, MenuButton>, TableCell<Transaction, MenuButton>> cellFactory = new TableCellFactory();
        buttonColumn.setCellFactory(cellFactory);

        loadComponents();
    }

    /**
     * Load all page components with values specific to the application use.
     */
    //TODO: Might need to break some of this down into individual functions
    private void loadComponents(){
        tableView.getItems().clear();
        monthChart.getData().clear();
        lineChart.getData().clear();
        series.getData().clear();

        checkingLabel.setText("$"+App.checkingAccount.getBalance());
        savingsLabel.setText("$"+App.savingsAccount.getBalance());
        investingLabel.setText("$"+App.investingAccount.getBalance());
        totalLabel.setText(String.format("Total: $%.2f", App.checkingAccount.getBalance()+App.savingsAccount.getBalance()+App.investingAccount.getBalance()));

        LocalDate date = LocalDate.now();
        monthChart.setTitle(date.getMonth().toString() + " " + date.getYear());

        Label[] dateLabels = {rt1d, rt2d, rt3d, rt4d, rt5d};
        Label[] transactionLabels = {rt1t, rt2t, rt3t, rt4t, rt5t};

        // Changing text on labels
        for(int i=0; i<5; i++){
            try{
                Transaction t = App.allTransactions.get(i);
                dateLabels[i].setText(t.getDate());
                transactionLabels[i].setText(String.format( "%s - $%.2f - %s - %s", t.getTitle(), t.getAmount(), t.getAccount(), t.getType()));
            } catch (Exception e){
                break;
            }
        }

        // Adding transactions to table
        List<Transaction> monthTransactions = new ArrayList<>();
        for(Transaction transaction : App.allTransactions){
            LocalDate transactionDate = LocalDate.parse(transaction.getDate());
            if(transactionDate.getMonth() != date.getMonth()) break;
            tableView.getItems().add(transaction);
            monthTransactions.add(0, transaction);
        }

        // Calculating income/expenses/net totals
        double income = 0, expenses = 0, net = 0;
        Map<Integer, Double> map = new HashMap<>();
        for(Transaction transaction : monthTransactions){
            LocalDate transactionDate = LocalDate.parse(transaction.getDate());
            switch(transaction.getType()){
                case "INCOME" -> {
                    net += transaction.getAmount();
                    income += transaction.getAmount();
                }
                case "EXPENSE" -> {
                    net -= transaction.getAmount();
                    expenses += transaction.getAmount();
                }
            }
            map.put(transactionDate.getDayOfMonth(), net);
        }

        for(Map.Entry<Integer, Double> entry : map.entrySet()){
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().add(series);

        if(net > 0) {
            plLabel.setTextFill(Color.GREEN);
            plLabel.setText(String.format("+$%.2f", net));
        } else if (0 > net){
            plLabel.setTextFill(Color.RED);
            plLabel.setText(String.format("-$%.2f", net));
        } else {
            plLabel.setTextFill(Color.BLACK);
            plLabel.setText("NO P/L");
        }

        monthChart.getData().add(new PieChart.Data("INCOME", income));
        monthChart.getData().add(new PieChart.Data("EXPENSE", expenses));
    }
}
