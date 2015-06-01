package se.kordev.atm;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.kordev.atm.exception.ATMException;
import se.kordev.atm.exception.ATMSecurityException;
import se.kordev.atm.model.ATMCard;
import se.kordev.atm.model.ATMReceipt;
import se.kordev.atm.service.ATM;
import se.kordev.atm.service.ATMSession;
import se.kordev.atm.service.Bank;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ATMTest {
//    - Att ett ATMException kastas om beloppet är mindre än 100, över 10000 eller inte ett jämnt hundratal
//    - Att ett ATMSecurityException kastas om fel pinkod matas in
//    - Att ett ATMException kastas om ett bankomatkort som inte är anslutet till någon av bankomatens
//    banker användas
//    - Att ett ATMException kastas om du en andra gång anropar någon av de metoder som avslutar en
//    session på din subklass till AbstractATMSession
//    - Alla publika metoder i din subklass till AbstractATMSession är testade
//    - Att ett IllegalArgumentException kastas om en tom lista med banker skickas till ATM-klassen vid new
//    - Att ett ATMException kastas om ett högre belopp än det finns täckning för anges till withdrawAmount

    List<Bank> bankList;
    ATM atm;

    @Mock
    Bank nordea;

    @Before
    public void setup() {
        bankList = new ArrayList<Bank>();
        bankList.add(nordea);
        when(nordea.getBankId()).thenReturn("nordea");
        when(nordea.getBalance("1001")).thenReturn(50000L, 49000L);
        atm = new ATM(bankList);
    }

    @After
    public void tearDown() {
        reset(nordea);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void throwsExceptionIfWrongAmountIsWithdrawn() {
        ATMCard card = new ATMCard("1001", "nordea", 1234);
        ATMSession session1 = atm.verifyPin(1234, card);
        ATMSession session2 = atm.verifyPin(1234, card);
        ATMSession session3 = atm.verifyPin(1234, card);

        try {
            session1.withdrawAmount(99);
            fail();
        } catch (ATMException e) {
        }

        try {
            session2.withdrawAmount(11000);
            fail();
        } catch (ATMException e) {
        }

        try {
            session3.withdrawAmount(501);
            fail();
        } catch (ATMException e) {
        }
    }

    @Test
    public void throwsExceptionWhenWrongPincodeIsEntered() {
        ATMCard card = new ATMCard("1001", "nordea", 1234);
        thrown.expect(ATMSecurityException.class);
        thrown.expectMessage(equalTo("Wrong pincode"));
        atm.verifyPin(4321, card);
    }

    @Test
    public void throwsExceptionIfBankDoesNotExist() {
        ATMCard card = new ATMCard("1001", "seb", 1234);
        thrown.expect(ATMException.class);
        thrown.expectMessage(equalTo("No bank connected to this card"));
        atm.verifyPin(1234, card);
    }

    @Test
    public void throwsExceptionIfTryingToWithdrawTwiceFromSameSession() {
        ATMCard card = new ATMCard("1001", "nordea", 1234);
        thrown.expect(ATMException.class);
        thrown.expectMessage(equalTo("Session is dead"));
        ATMSession session = atm.verifyPin(1234, card);
        session.withdrawAmount(1000);
        session.withdrawAmount(2000);
    }

    @Test
    public void throwsExceptionIfTryingToInitializeAtmWithEmptyBankList() {
        thrown.expect(IllegalArgumentException.class);
        new ATM(new ArrayList<Bank>());
    }

    @Test
    public void throwsExceptionIfThereIsNotEnoughMoneyOnAccount() {
        ATMCard card = new ATMCard("1001", "nordea", 1234);
        ATMSession session = atm.verifyPin(1234, card);
        thrown.expect(ATMException.class);
        thrown.expectMessage(equalTo("Not enough funds on account"));
        session.withdrawAmount(50001);
    }

    @Test
    public void testWithdrawAmount() {
        ATMCard card = new ATMCard("1001", "nordea", 1234);
        ATMSession session = atm.verifyPin(1234, card);
        int amountToWithdraw = 1000;
        long balanceBeforeWithdraw = session.checkBalance();
        session.withdrawAmount(amountToWithdraw);
        assertTrue(session.checkBalance() == (balanceBeforeWithdraw - amountToWithdraw));
    }

    @Test
    public void testRequestReceipt() {
        ATMCard card = new ATMCard("1001", "nordea", 1234);
        ATMSession session = atm.verifyPin(1234, card);
        int amountToWithdraw = 1000;

        session.withdrawAmount(amountToWithdraw);
        ATMReceipt receipt = session.requestReceipt();

        assertTrue(receipt.getTransactionId() == session.getTransactionId());
    }

    @Test
    public void testCheckBalance() {
        ATMCard card = new ATMCard("1001", "nordea", 1234);
        ATMSession session = atm.verifyPin(1234, card);
        long balanceBeforeWithdraw = session.checkBalance();
        int amountToWithdraw = 1000;

        session.withdrawAmount(amountToWithdraw);

        assertTrue(session.checkBalance() == (balanceBeforeWithdraw - amountToWithdraw));
    }

}
