package sample;

public class EntryNachhilfe extends EntryUebersicht {
    String begin;
    String end;
    String duration;
    private String clients;

    public EntryNachhilfe(String id, String date, String company, String amount, String begin, String end, String duration, String clients) {
        super(id, date, company, amount);
        this.begin = begin;
        this.end = end;
        this.duration = duration;
        this.clients = clients;
    }

    public String getClients() {
        return clients;
    }

    public void setClients(String clients) {
        this.clients = clients;
    }


}
