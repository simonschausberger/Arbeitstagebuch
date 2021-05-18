package sample;

import java.time.LocalTime;
import java.util.Date;

public class EntryUebersicht {
    String id;
    String date;
    String company;
    String amount;

    public EntryUebersicht(String id, String date, String company, String amount) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
