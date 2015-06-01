package se.kordev.atm.service;

import se.kordev.atm.exception.ATMException;
import se.kordev.atm.model.ATMCard;
import se.kordev.atm.model.ATMReceipt;

import java.util.UUID;

public class ATMSessionImpl extends AbstractATMSession {
    private boolean sessionIsDead;


    private int transactionId;
    private ATMReceipt atmReceipt;


    public ATMSessionImpl(ATMCard atmCard, Bank bank) {
        super(atmCard, bank);
        sessionIsDead = false;

    }

    @Override
    public long withdrawAmount(int amount) {
        if (sessionIsDead) {
            throw new ATMException("Session is dead");
        }
        if (amount > checkBalance()) {
            throw new ATMException("Not enough funds on account");
        }

        if ((amount < 100) || (amount > 10000) || (amount % 100 != 0)) {
            throw new ATMException("Invalid amount: " + amount);
        } else {
            sessionIsDead = true;
            transactionId = UUID.randomUUID().hashCode();
            atmReceipt = new ATMReceipt(transactionId, amount);

            return bank.withdrawAmount(amount);
        }
    }

    @Override
    public ATMReceipt requestReceipt() {
        return atmReceipt;
    }

    @Override
    public long checkBalance() {
        return bank.getBalance(atmCard.getAccountHolderId());
    }

    public int getTransactionId() {
        return transactionId;
    }
}
