package main.Models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.UUID;


/**
 * Definition of Transaction objects. Using StringProperty as
 * a type for faster application loading times. An important thing
 * to note about this object, is that the 'id' attribute will be
 * a random UUID, once 'id' has been assigned, there will be no
 * method to change it.
 */
public class Transaction {
    private final SimpleStringProperty id;
    private final SimpleStringProperty date;
    private final SimpleStringProperty title;
    private final SimpleDoubleProperty amount;
    private final SimpleStringProperty account;
    private final SimpleStringProperty type;
    private final SimpleStringProperty subtype;
    private final SimpleStringProperty note;

    /**
     * Constructor of Transaction with provided id and
     * all other attributes. Use this when creating
     * Transaction saved in the database.
     */
    public Transaction(String id, String date, String title, double amount, String account, String type, String subtype, String note){
        this.id = new SimpleStringProperty(id);
        this.date = new SimpleStringProperty(date);
        this.title = new SimpleStringProperty(title);
        this.amount = new SimpleDoubleProperty(amount);
        this.account = new SimpleStringProperty(account);
        this.type = new SimpleStringProperty(type);
        this.subtype = new SimpleStringProperty(subtype);
        this.note = new SimpleStringProperty(note);
    }

    /**
     * Constructor of a Transaction  provided id and
     * all other attributes. Use this when you want to
     * create a new Transaction object with its UUID
     * and later add it to the database.
     */
    public Transaction(String date, String title, double amount, String account, String type, String subtype, String note){
        this.id = new SimpleStringProperty(UUID.randomUUID().toString());
        this.date = new SimpleStringProperty(date);
        this.title = new SimpleStringProperty(title);
        this.amount = new SimpleDoubleProperty(amount);
        this.account = new SimpleStringProperty(account);
        this.type = new SimpleStringProperty(type);
        this.subtype = new SimpleStringProperty(subtype);
        this.note = new SimpleStringProperty(note);
    }

    /**
     * getFoo returns foo.
     * @return foo - value of StringProperty object
     */
    public String getId() {
        return id.get();
    }
    public String getDate() {
        return date.get();
    }
    public String getTitle() {
        return title.get();
    }
    public double getAmount() {
        return amount.get();
    }
    public String getAccount(){return account.get();}
    public String getType() {
        return type.get();
    }
    public String getSubtype() {
        return subtype.get();
    }
    public String getNote() {
        return note.get();
    }


    /**
     * setFoo sets the value of Foo to be bar.
     * @param "bar"
     */
    public void setDate(String date) {
        this.date.set(date);
    }
    public void setTitle(String title) {
        this.title.set(title);
    }
    public void setAmount(double amount) {
        this.amount.set(amount);
    }
    public void setAccount(String account){this.account.set(account);}
    public void setType(String type) {
        this.type.set(type);
    }
    public void setSubtype(String subtype) {
        this.subtype.set(subtype);
    }
    public void setNote(String note) {
        this.note.set(note);
    }

    /**
     * Return Transaction details as String obj.
     * @return String
     */
    @Override
    public String toString(){
        return String.format("%s | %s | %s | %f | %s | %s | %s | %s",
                this.getId(), this.getDate(),
                this.getTitle(), this.getAmount(),
                this.getAccount(), this.getType(),
                this.getSubtype(), this.getNote());
    }
}