package main.Handlers;

import main.Models.Transaction;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;


/**
 * A class that acts as an application programming
 * interface (API) to create our local database,
 * connect to it, allow us to create/edit/delete
 * transaction entries and manipulate data inputs
 * and outputs.
 */
public class DatabaseHandler {
    private Connection conn = null;

    /**
     * DatabaseHandler constructor initializes a connection with
     * the local database and correctly initializes it, if .db file
     * not yet created.
     */
    public DatabaseHandler(){
        this.connect();
        this.createTable();
    }

    /**
     * Generates empty transactions table in database,
     * only if one not yet exists.
     */
    private void createTable(){
        String sql = """
                CREATE TABLE IF NOT EXISTS transactions (
                	id text PRIMARY KEY,
                	date text NOT NULL,
                	title text NOT NULL,
                	amount real NOT NULL,
                	account text NOT NULL,
                	type text NOT NULL,
                	subtype text NOT NULL,
                	note text
                );""";
        try (Statement statement = conn.createStatement()) {
            statement.execute(sql);
        } catch (SQLException exception) {
            System.out.println("ERROR! At createTable(): " + exception.getMessage());
        }
    }

    /**
     * Pulls all transaction entries from the table and
     * returns them in a List of Transaction type.
     * @return List<Transaction> - All transactions
     */
    public List<Transaction> getTransactions(){
        List<Transaction> transactionList = new ArrayList<Transaction>();
        String sql = "SELECT * FROM transactions";
        try (
                Statement statement  = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ){
            while (resultSet.next()) {
                transactionList.add(new Transaction(
                        resultSet.getString(1),    // UUID String
                        resultSet.getString(2),    // date String
                        resultSet.getString(3),    // title String
                        resultSet.getDouble(4),    // amount Double
                        resultSet.getString(5),    // account String
                        resultSet.getString(6),    // type String
                        resultSet.getString(7),    // subtype String
                        resultSet.getString(8)     // note String
                ));
            }
        } catch (SQLException exception) {
            System.out.println("ERROR! At display(): " + exception.getMessage());
        }
        return transactionList;
    }

    /**
     * Adds an entry to the transactions table from an object.
     * @param transaction - Object used for entry
     */
    public void insertTransaction(Transaction transaction){
        String sql = "INSERT INTO transactions VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, transaction.getId());
            preparedStatement.setString(2, transaction.getDate());
            preparedStatement.setString(3, transaction.getTitle());
            preparedStatement.setDouble(4, transaction.getAmount());
            preparedStatement.setString(5, transaction.getAccount());
            preparedStatement.setString(6, transaction.getType());
            preparedStatement.setString(7, transaction.getSubtype());
            preparedStatement.setString(8, transaction.getNote());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            System.out.println("ERROR! At insertTransaction(): " + exception.getMessage());
        }
    }

    /**
     * Given a UUID String, delete a transaction from
     * the database.
     * @param id - String UUID
     */
    public void deleteTransaction(String id){
        String sql = "DELETE FROM transactions WHERE id=?";
        try(PreparedStatement preparedStatement = conn.prepareStatement(sql)){
            preparedStatement.setString(1, id);
            preparedStatement.execute();
        } catch (SQLException exception) {
            System.out.println("ERROR! At deleteTransaction(): " + exception.getMessage());
        }
    }

    /**
     * Display all transactions in the database
     * on the CLI.
     */
    public void display(){
        String sql = "SELECT * FROM transactions";

        try (
             Statement statement  = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)
        ){
            while (resultSet.next()) {
                String entry = String.format(
                        "Transaction %s | %s | %s | %f | %s | %s | %s | %s \n",
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8)
                );
                System.out.println(entry);
            }
        } catch (SQLException exception) {
            System.out.println("ERROR! At display(): " + exception.getMessage());
        }
    }

    /**
     * Establish connection to database file. Will create a new
     * data.db file if one is not yet available.
     */
    private void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:data.db");
        } catch (SQLException exception) {
            System.out.println("ERROR! At connect(): " + exception.getMessage());
        }
    }

}