package sample;

public class EntryNachhilfe extends EntryUebersicht {
    private String clients;

    public EntryNachhilfe(String id, String date, String company, String begin, String end, String duration, float amount, String clients) {
        super(id, date, company, begin, end, duration, amount);
        this.clients = clients;
    }

    public String getClients() {
        return clients;
    }

    public void setClients(String clients) {
        this.clients = clients;
    }


}
