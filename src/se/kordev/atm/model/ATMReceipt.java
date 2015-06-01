package se.kordev.atm.model;

import java.util.Date;

public final class ATMReceipt {
    private final long transactionId;
    private final int amount;
    private final Date date;

    public ATMReceipt(long transactionId, int amount) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.date = new Date(); // Date in java.util
    }

    public long getTransactionId() {
        return transactionId;
    }

    public int getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }
}
