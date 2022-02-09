package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TableTest {

    private Table testTable;
    private List<String> testList;

    @BeforeEach
    void runBefore() {
        testTable = new Table(5, "Test Name");
        testList = new ArrayList<>();
    }

    @Test
    void testConstructor() {
        assertTrue(testTable.getCleanStatus());
        assertFalse(testTable.getSetStatus());
        assertTrue(testTable.getAvailabilityStatus());
        assertTrue(testTable.getFoodDeliveryStatus());
        assertEquals("Test Name", testTable.getName());
        assertEquals(5, testTable.getMaxOccupancy());
        assertEquals(testList, testTable.getCleaningHistory());
        assertNull(testTable.getBill());
    }

    @Test
    void testCleanTable() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime testTime1 = LocalDateTime.now();
        String formattedTestTime1 = testTime1.format(formatter);
        testList.add(formattedTestTime1);

        testTable.cleanTable();
        assertEquals(testList, testTable.getCleaningHistory());
        assertTrue(testTable.getCleanStatus());

        testTable.emptyTable();
        LocalDateTime testTime2 = LocalDateTime.now();
        String formattedTestTime2 = testTime2.format(formatter);
        testList.add(formattedTestTime2);

        testTable.cleanTable();
        assertEquals(testList, testTable.getCleaningHistory());
        assertTrue(testTable.getCleanStatus());
    }

    @Test
    void testEmptyTable() {
        testTable.emptyTable();
        assertTrue(testTable.getAvailabilityStatus());
        assertFalse(testTable.getCleanStatus());
        assertFalse(testTable.getSetStatus());
        assertNull(testTable.getBill());

        testTable.cleanTable();
        testTable.trueSetTable();
        testTable.occupyTable();
        testTable.getBill().payBill();

        testTable.emptyTable();
        assertTrue(testTable.getAvailabilityStatus());
        assertFalse(testTable.getCleanStatus());
        assertFalse(testTable.getSetStatus());
        assertNull(testTable.getBill());
    }

    @Test
    void testPayForFood() {
        testTable.occupyTable();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime testTime1 = LocalDateTime.now();
        String formattedTestTime1 = testTime1.format(formatter);
        testList.add(formattedTestTime1);

        testTable.payForFood();
        assertEquals(testList, testTable.getPurchaseHistory());
        assertTrue(testTable.getBill().getPayStatus());

        testTable.cleanTable();
        testTable.occupyTable();

        LocalDateTime testTime2 = LocalDateTime.now();
        String formattedTestTime2 = testTime2.format(formatter);
        testList.add(formattedTestTime2);

        testTable.payForFood();
        assertEquals(testList, testTable.getPurchaseHistory());
        assertTrue(testTable.getBill().getPayStatus());
    }

    @Test
    void testOccupyTable() {
        testTable.occupyTable();
        assertFalse(testTable.getAvailabilityStatus());
        assertFalse(testTable.getBill().getPayStatus());
        assertTrue(testTable.getFoodDeliveryStatus());

        testTable.deliverFood();
        testTable.getBill().payBill();

        testTable.emptyTable();

        testTable.occupyTable();
        assertFalse(testTable.getAvailabilityStatus());
        assertFalse(testTable.getBill().getPayStatus());
        assertTrue(testTable.getFoodDeliveryStatus());
    }

    @Test
    void testDeliverFood() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime testTime1 = LocalDateTime.now();
        String formattedTestTime1 = testTime1.format(formatter);
        testList.add(formattedTestTime1);

        testTable.deliverFood();
        assertEquals(testList, testTable.getDeliveryHistory());
        assertTrue(testTable.getFoodDeliveryStatus());

        testTable.emptyTable();
        testTable.cleanTable();

        LocalDateTime testTime2 = LocalDateTime.now();
        String formattedTestTime2 = testTime2.format(formatter);
        testList.add(formattedTestTime2);

        testTable.deliverFood();
        assertEquals(testList, testTable.getDeliveryHistory());
        assertTrue(testTable.getFoodDeliveryStatus());
    }
}
