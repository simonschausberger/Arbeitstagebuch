package sample;

import java.time.LocalTime;
import java.util.Date;

public class EntryUebersicht {
    String id;
    Date date;
    String company;
    double amount;

    public EntryUebersicht(String id, Date date, String company, double amount) {
        this.id = id;
        this.date = date;
        this.company = company;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
