package model;

import structure.MyLinkedList;

public class BankAccount {
    private String accountId;
    private String bankName;
    private double balance;
    private MyLinkedList expenditureCodes;

    public BankAccount(String accountId, String bankName, double balance) {
        this.accountId = accountId;
        this.bankName = bankName;
        this.balance = balance;
        this.expenditureCodes = new MyLinkedList();
    }

    public String getAccountId() { return accountId; }
    public String getBankName() { return bankName; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public MyLinkedList getExpenditureCodes() { return expenditureCodes; }

    public void addExpenditureCode(String code) {
        expenditureCodes.add(code);
    }

    @Override
    public String toString() {
        StringBuilder codes = new StringBuilder();
        expenditureCodes.forEach(new MyLinkedList.MyLinkedListConsumer() {
    @Override
    public void accept(String code) {
        codes.append(code).append(",");
    }
    });

        if (codes.length() > 0) codes.setLength(codes.length() - 1); // remove trailing comma
        return accountId + "|" + bankName + "|" + balance + "|" + codes;
    }
}