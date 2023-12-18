package main.Controllers;

import main.App;
import main.Models.Transaction;

import java.util.*;
import java.time.LocalDate;
import java.time.format.TextStyle;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;


/**
 * Controller for the FXML Analysis page that loads on the
 * main page and activates when the 'Analysis' button is
 * pressed. Object is called when the FXML page loads,
 * initializing page components and actions.
 */
public class AnalysisPaneController {
    @FXML private DatePicker fromDate;
    @FXML private DatePicker toDate;
    @FXML private Button loadButton;
    @FXML private PieChart incomeChart;
    @FXML private PieChart revenueChart;
    @FXML private PieChart expensesChart;
    @FXML private BarChart<String, Number> barChart;
    @FXML private ChoiceBox<String> accountChoice;

    private final XYChart.Series<String, Number> series = new XYChart.Series<>();
    private final LocalDate date = LocalDate.now();
    private final List<String> months = new ArrayList<>(List.of("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
    private final Map<String, Double> monthRevenue = new HashMap<>();

    /**
     * Initializes components with their restrictions, bounds,
     * and data types. Also loads all components. This function is
     * called immediately when the FXML page is loaded by the
     * FXML Loader.
     */
    public void initialize(){
        fromDate.setDayCellFactory(param -> restrictDate());
        toDate.setDayCellFactory(param -> restrictDate());
        fromDate.setValue(date.minusMonths(1));
        toDate.setValue(date);
        barChart.setTitle(Integer.toString(date.getYear()));
        for(String month : months){ monthRevenue.put(month, 0.0); }
        loadButton.setOnAction(event -> loadComponents());
        loadComponents();
    }

    /**
     * Loads components in the page, with their own
     * corresponding and updated data. Function can
     * be called again to update the page, once the
     * data has been updated.
     */
    private void loadComponents(){
        revenueChart.getData().clear();
        incomeChart.getData().clear();
        expensesChart.getData().clear();
        barChart.getData().clear();
        series.getData().clear();

        LocalDate startDate = fromDate.getValue();
        LocalDate endDate = toDate.getValue();
        double income = 0, expenses = 0;
        double[] incomeSubtypes = new double[3];
        double[] expensesSubtype = new double[3];

        for(Transaction transaction : App.allTransactions){
            LocalDate transactionDate = LocalDate.parse(transaction.getDate());
            String month = transactionDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            if(transactionDate.isAfter(endDate)){break;}
            if(transactionDate.isAfter(startDate.minusDays(1)) && transactionDate.isBefore(endDate.plusDays(1))){
                double amount = transaction.getAmount();
                switch(transaction.getType()){
                    case "INCOME" -> {
                        income += amount;
                        monthRevenue.put(month, monthRevenue.get(month)+amount);
                        switch (transaction.getSubtype()){
                            case "WORK" -> incomeSubtypes[0] += amount;
                            case "INVESTMENT" -> incomeSubtypes[1] += amount;
                            case "OTHER" -> incomeSubtypes[2] += amount;
                        }
                    }
                    case "EXPENSE" -> {
                        expenses += amount;
                        monthRevenue.put(month, monthRevenue.get(month)-amount);
                        switch (transaction.getSubtype()){
                            case "BILL" -> expensesSubtype[0] += amount;
                            case "FUN" -> expensesSubtype[1] += amount;
                            case "OTHER" -> expensesSubtype[2] += amount;
                        }
                    }
                }
            }
        }

        // Setting up entries in bar chart
        for(Map.Entry<String, Double> entry : monthRevenue.entrySet()){
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().add(series);

        // Updating pie charts
        revenueChart.getData().add(new PieChart.Data("INC.", income));
        revenueChart.getData().add(new PieChart.Data("EXP.", expenses));
        incomeChart.getData().add(new PieChart.Data("WORK", incomeSubtypes[0]));
        incomeChart.getData().add(new PieChart.Data("INV.", incomeSubtypes[1]));
        incomeChart.getData().add(new PieChart.Data("OTHER", incomeSubtypes[2]));
        expensesChart.getData().add(new PieChart.Data("BILL", expensesSubtype[0]));
        expensesChart.getData().add(new PieChart.Data("FUN", expensesSubtype[1]));
        expensesChart.getData().add(new PieChart.Data("OTHER", expensesSubtype[2]));
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
}
