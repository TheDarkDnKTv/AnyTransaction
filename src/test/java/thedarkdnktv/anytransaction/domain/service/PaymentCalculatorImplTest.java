package thedarkdnktv.anytransaction.domain.service;

import org.junit.jupiter.api.Test;
import thedarkdnktv.anytransaction.domain.PaymentCalculatorImpl;
import thedarkdnktv.anytransaction.domain.enums.PaymentType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentCalculatorImplTest {

    @Test
    public void test_1() {
        var calc = new PaymentCalculatorImpl(PaymentType.GRAB_PAY, 1000D);
        assertEquals(calc.calculatePoints(), 10);
        assertEquals(calc.calculatePrice(0.99D), 1000D);
    }

    @Test
    public void test_2() {
        var calc = new PaymentCalculatorImpl(PaymentType.BANK_TRANSFER, 1000D);
        assertEquals(calc.calculatePoints(), 0);
        assertEquals(calc.calculatePrice(0.99D), 1000D);
    }

    @Test
    public void test_3() {
        var calc = new PaymentCalculatorImpl(PaymentType.POINTS, 100D);
        assertEquals(calc.calculatePoints(), 0);
        assertEquals(calc.calculatePrice(0.99D), 100D);
    }

    @Test
    public void test_4() {
        var calc = new PaymentCalculatorImpl(PaymentType.JCB, 100D);
        assertEquals(calc.calculatePoints(), 5);
        assertEquals(calc.calculatePrice(0.95D), 95D);
    }

    @Test
    public void test_5() {
        var calc = new PaymentCalculatorImpl(PaymentType.CASH_ON_DELIVERY, 100D);
        assertEquals(calc.calculatePoints(), 5);
        assertEquals(calc.calculatePrice(1.02D), 102D);
    }
}
