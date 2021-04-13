package sample;

import java.time.LocalTime;
import java.util.Date;

public class EntryUebersicht {
    String id;
    String date;
    String company;
    String begin;
    String end;
    String duration;
    float amount;

    public EntryUebersicht(String id, String date, String company, String begin, String end, String duration, float amount) {
        this.id = id;
        this.date = date;
        this.company = company;
        this.begin = begin;
        this.end = end;
        this.duration = duration;
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

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
