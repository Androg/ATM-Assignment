package se.kordev.atm.service;

import se.kordev.atm.model.ATMReceipt;

public interface ATMSession {
    // Denna metod ska kasta ett exception om beloppet som anges är:
    // < 100 eller > 10000 eller inte ett jämnt hundratal (exempelvis kastas ett exception om
    // beloppet är 110)
    // Denna metod avslutar en session
    // Om man anropar denna metod två gånger efter varandra ska ett ATMException kastas
    // Denna metod kastar ett ATMException om ett högre belopp än det finns täckning för anges
    long withdrawAmount(int amount);

    // Begär ett kvitto från banken för angivet transactionId
    ATMReceipt requestReceipt();

    // Denna metod returnerar aktuellt belopp på kontot som är knutet till denna session
    // Denna metod avslutar en session
    // Om man anropar denna metod två gånger efter varandra ska ett ATMException kastas
    long checkBalance();

    int getTransactionId();
}
