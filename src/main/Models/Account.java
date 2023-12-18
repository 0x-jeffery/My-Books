package main.Models;

import java.util.ArrayList;
import java.util.List;


/**
 * Class that defines an instance of an Account
 * object; these can be created for Checking,
 * Savings, Investing accounts or other. Used to
 * keep track of account balance, transactions made
 * with the account.
 */
public class Account {
    // TODO: Handle the negative balance error >> A negative
    //  balance will not be allowed in this version.
    private final String account;
    private final List<Transaction> transactionList;
    private double balance = 0;

    /**
     * 
     * @param account
     */
    public Account(String account) {
        this.account = account;
        this.transactionList = new ArrayList<>();
    }

    public List<Transaction> getTransactionList() {
        return this.transactionList;
    }

    public double getBalance(){
        return (double) Math.round(this.balance * 100) / 100;
    }

    public void add(Transaction transaction){
        this.transactionList.add(transaction);
        if(transaction.getType().equals("INCOME")) this.balance += transaction.getAmount();
        else this.balance -= transaction.getAmount();
    }

    public void remove(Transaction transaction){
        this.transactionList.remove(transaction);
        if(transaction.getType().equals("EXPENSE")) this.balance += transaction.getAmount();
        else this.balance -= transaction.getAmount();
    }

    public void display(){
        System.out.printf("\t%s\n", this.account);
        System.out.println("\t---");
        for(Transaction transaction : this.transactionList){
            System.out.println(transaction.toString());
        }
    }
}
