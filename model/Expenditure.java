package model;

public class Expenditure {
    private String code;
    private double amount;
    private String date;
    private String phase;
    private String category;
    private String accountId;

    public Expenditure(String code, double amount, String date, String phase, String category, String accountId) {
        this.code = code;
        this.amount = amount;
        this.date = date;
        this.phase = phase;
        this.category = category;
        this.accountId = accountId;
    }

    public String getCode() { return code; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getPhase() { return phase; }
    public String getCategory() { return category; }
    public String getAccountId() { return accountId; }

    @Override
    public String toString() {
        return code + "|" + amount + "|" + date + "|" + phase + "|" + category + "|" + accountId;
    }
}