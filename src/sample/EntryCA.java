package sample;

public class EntryCA extends EntryUebersicht {

    private String interruption;

    public EntryCA(String id, String date, String company, String begin, String end, String duration, float amount, String interruption) {
        super(id, date, company, begin, end, duration, amount);
        this.interruption = interruption;
    }

    public String getInterruption() {
        return interruption;
    }

    public void setInterruption(String interruption) {
        this.interruption = interruption;
    }
}
