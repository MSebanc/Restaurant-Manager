package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BillTest {

    private Bill testBill;

    @BeforeEach
    void runBefore() {
        testBill = new Bill();
    }

    @Test
    void testConstructor() {
        assertEquals(0.00, testBill.getCost());
        assertEquals(0.00, testBill.getTip());
        assertFalse(testBill.getPayStatus());
    }

    @Test
    void testOtherConstructor() {
        Bill bill = new Bill(0.00, 0.00, false);

        assertEquals(0.00, bill.getCost());
        assertEquals(0.00, bill.getTip());
        assertFalse(bill.getPayStatus());
    }

    @Test
    void testPayBill() {
        testBill.payBill();
        assertTrue(testBill.getPayStatus());
    }

    @Test
    void testAddCost() {
        testBill.addCost(4.00);
        assertEquals(4.00, testBill.getCost());
        testBill.addCost(3.50);
        assertEquals(7.50, testBill.getCost());
    }

    @Test
    void testSetTip() {
        testBill.setTip(2.00);
        assertEquals(2.00, testBill.getTip());
        testBill.setTip(3.50);
        assertEquals(3.50, testBill.getTip());
    }

    @Test
    void testGetTotalCost() {
        assertEquals(0.00, testBill.getTotalCost());
        testBill.addCost(3.75);
        assertEquals(3.94, testBill.getTotalCost());
        testBill.addCost(1.00);
        assertEquals(4.99, testBill.getTotalCost());
        testBill.setTip(2.00);
        assertEquals(6.99, testBill.getTotalCost());
    }
}
