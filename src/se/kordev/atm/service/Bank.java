package se.kordev.atm.service;

import se.kordev.atm.model.BankReceipt;

public interface Bank {
    String getBankId();
    long getBalance(String accountHolderId);
    long withdrawAmount(int amount);
    BankReceipt requestReceipt(long transactionId);
}