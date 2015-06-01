package se.kordev.atm.service;

import se.kordev.atm.model.ATMCard;

public abstract class AbstractATMSession implements ATMSession {
    protected final ATMCard atmCard;
    protected final Bank bank;

    public AbstractATMSession(ATMCard atmCard, Bank bank) {
        this.atmCard = atmCard;
        this.bank = bank;
    }
}