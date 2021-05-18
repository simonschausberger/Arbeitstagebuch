package sample;

public class EntryCA extends EntryUebersicht {


    public EntryCA(String id, String date, String company, String amount) {
        super(id, date, company, amount);
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public void setId(String id) {
        super.setId(id);
    }

    @Override
    public String getDate() {
        return super.getDate();
    }

    @Override
    public void setDate(String date) {
        super.setDate(date);
    }

    @Override
    public String getCompany() {
        return super.getCompany();
    }

    @Override
    public void setCompany(String company) {
        super.setCompany(company);
    }

    @Override
    public String getAmount() {
        return super.getAmount();
    }

    @Override
    public void setAmount(String amount) {
        super.setAmount(amount);
    }
}
