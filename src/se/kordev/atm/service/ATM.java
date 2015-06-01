package se.kordev.atm.service;

import se.kordev.atm.exception.ATMException;
import se.kordev.atm.exception.ATMSecurityException;
import se.kordev.atm.model.ATMCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ATM {
    private final Map<String, Bank> banks;

    public ATM(List<Bank> banks) {
        this.banks = new HashMap<String, Bank>();

        if (banks.size() == 0) {
            throw new IllegalArgumentException();
        }
        for (Bank bank : banks) {
            this.banks.put(bank.getBankId(), bank);
        }
    }

    // Returnerar en implementation av ATMSession om pikoden är korrekt. Annars kastas ett // ATMSecurityException
    public ATMSession verifyPin(int pin, ATMCard card) {
        Bank bank = getBank(card);

        if (card.verifyPin(pin)) {
            return new ATMSessionImpl(card, bank);
        } else {
            throw new ATMSecurityException("Wrong pincode");
        }

        // Returnerar banken som angivet bankomatkort är kopplat till. Hittas ingen bank kastas ett // ATMException
    }
    private Bank getBank(ATMCard card) {
        if(banks.get(card.getBankId()) == null) {
            throw new ATMException("No bank connected to this card");
        }
        else {
            return banks.get(card.getBankId());
        }
    }
}
