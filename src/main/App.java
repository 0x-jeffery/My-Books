/*

 * ================================================
 *                  My-Books
 *
 *  My-Books is an expense management desktop
 *  application that focuses on ease of use, data
 *  visualization in forms of graphs and charts,
 *  and data comprehension. Built with Java, JavaFX
 *  SDK and SQLite3 JDBC this application is
 *  lightweight, fast, and able to run on all
 *  modern operating systems. This application runs
 *  locally on your machine, without any connection
 *  to the internet. With it, you'll be able to:
 *  keep track of three separate banking accounts
 *  (Checking, Savings & Investing), as well as
 *  create/edit/delete transactions, display P/L
 *  across any time period, group transactions by
 *  type or subtype. Keep track of income, revenue
 *  and expenses coming in every month. display
 *  pie-charts and point graphs.
 *
 *  Version 1.0.0
 *  Created by 0xJeffery
 *  ================================================
 */

package main;

import javafx.stage.Stage;
import javafx.application.Application;

import main.Models.Account;
import main.Models.AppStage;
import main.Models.Transaction;
import main.Handlers.DatabaseHandler;

import java.util.*;

public class App extends Application {
    final private int WIDTH = 1000;
    final private int HEIGHT = 800;
    final public static DatabaseHandler databaseHandler = new DatabaseHandler();
    final public static List<String> ACCOUNTS = new ArrayList<String>(List.of("CHECKING", "SAVINGS", "INVESTING"));
    final public static List<String> TYPES = new ArrayList<String>(List.of("INCOME", "EXPENSE"));
    final public static List<String> SUBTYPES_INCOME = new ArrayList<String>(List.of("WORK", "INVESTMENT", "OTHER"));
    final public static List<String> SUBTYPES_EXPENSE = new ArrayList<String>(List.of("BILL", "FUN", "OTHER"));
    public static List<Transaction> allTransactions;
    public static Account checkingAccount;
    public static Account savingsAccount;
    public static Account investingAccount;

    public static void main(String[] args) {
        allTransactions = initializeTransactions();

        checkingAccount = new Account("CHECKING");
        savingsAccount = new Account("SAVINGS");
        investingAccount = new Account("INVESTING");

        for(Transaction transaction : allTransactions){
            String account = transaction.getAccount();
            switch (account) {
                case "CHECKING" -> checkingAccount.add(transaction);
                case "SAVINGS" -> savingsAccount.add(transaction);
                case "INVESTING" -> investingAccount.add(transaction);
                default -> System.out.println("ERROR! In filtering into accounts with: " + transaction);
            }
        }

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        AppStage appStage = new AppStage("../FXML/ContentSwitcher.fxml");
        appStage.stage.setTitle("My-Books");
        appStage.stage.show();
    }

    public static void createTransaction(Transaction transaction){
        boolean flag = false;

        for(int i=0; i<allTransactions.size(); i++){
            if(allTransactions.get(i).getDate().compareTo(transaction.getDate()) < 0) {
                allTransactions.add(i, transaction);
                flag = true;
                break;
            }
        }

        if(!flag){allTransactions.add(allTransactions.size(), transaction);}
        databaseHandler.insertTransaction(transaction);

        switch (transaction.getAccount()) {
            case "CHECKING" -> checkingAccount.add(transaction);
            case "SAVINGS" -> savingsAccount.add(transaction);
            case "INVESTING" -> investingAccount.add(transaction);
            default -> System.out.println("ERROR! In adding to an account with " + transaction);
        }
    }

    public static void removeTransaction(Transaction transaction){
        allTransactions.remove(transaction);
        databaseHandler.deleteTransaction(transaction.getId());

        switch (transaction.getAccount()) {
            case "CHECKING" -> checkingAccount.remove(transaction);
            case "SAVINGS" -> savingsAccount.remove(transaction);
            case "INVESTING" -> investingAccount.remove(transaction);
            default -> System.out.println("ERROR! In removing from an account with " + transaction);
        }
    }

    private static List<Transaction> initializeTransactions(){
        allTransactions = databaseHandler.getTransactions();
        allTransactions.sort((Comparator.comparing(Transaction::getDate)));
        Collections.reverse(allTransactions);
        return allTransactions;
    }
}

