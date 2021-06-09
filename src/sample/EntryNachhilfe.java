package sample;

public class EntryNachhilfe extends EntryUebersicht {
    String begin;
    String end;
    String duration;
    private String clients;

    public EntryNachhilfe(String id, String date, String company, float amount, String begin, String end, String duration, String clients) {
        super(id, date, company, amount);
        this.begin = begin;
        this.end = end;
        this.duration = duration;
        this.clients = clients;
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

    public String getClients() {
        return clients;
    }

    public void setClients(String clients) {
        this.clients = clients;
    }


}
