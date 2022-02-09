package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantTest {
    private Restaurant testRestaurant;
    private List<Table> testTableList;


    @BeforeEach
    void runBefore() {
        testRestaurant = new Restaurant();
        testTableList = new ArrayList<>();
    }

    @Test
    void testConstructor() {
        assertEquals(11, testRestaurant.getMenu().size());
        assertEquals(testTableList, testRestaurant.getTables());
        assertEquals(0, testRestaurant.getTotalCustomers());
        assertEquals(0.00, testRestaurant.getTips());
        assertEquals(0.00, testRestaurant.getEarnings());
    }

    @Test
    void testAddTable() {
        List<String> tableStringList = new ArrayList<>();
        List<Integer> tableIntList = new ArrayList<>();

        testRestaurant.addTable(5, "Test Table 1");
        tableStringList.add("Test Table 1");
        tableIntList.add(5);

        for (Table table : testRestaurant.getTables()) {
            assertEquals(tableStringList.get(testRestaurant.getTables().indexOf(table)), table.getName());
            assertEquals(tableIntList.get(testRestaurant.getTables().indexOf(table)), table.getMaxOccupancy());
        }

        testRestaurant.addTable(4, "Test Table 2");
        tableStringList.add("Test Table 2");
        tableIntList.add(4);

        for (Table table : testRestaurant.getTables()) {
            assertEquals(tableStringList.get(testRestaurant.getTables().indexOf(table)), table.getName());
            assertEquals(tableIntList.get(testRestaurant.getTables().indexOf(table)), table.getMaxOccupancy());
        }

        testRestaurant.addTable(10, "Test Table 3");
        tableStringList.add("Test Table 3");
        tableIntList.add(10);

        for (Table table : testRestaurant.getTables()) {
            assertEquals(tableStringList.get(testRestaurant.getTables().indexOf(table)), table.getName());
            assertEquals(tableIntList.get(testRestaurant.getTables().indexOf(table)), table.getMaxOccupancy());
        }

    }

    @Test
    void testRemoveTable() {
        List<String> tableStringList = new ArrayList<>();
        List<Integer> tableIntList = new ArrayList<>();

        testRestaurant.addTable(5, "Test Table 1");
        tableStringList.add("Test Table 1");
        tableIntList.add(5);
        testRestaurant.addTable(4, "Test Table 2");
        tableStringList.add("Test Table 2");
        tableIntList.add(4);
        testRestaurant.addTable(10, "Test Table 3");
        tableStringList.add("Test Table 3");
        tableIntList.add(10);

        testRestaurant.removeTable("Test Table 2");
        tableStringList.remove("Test Table 2");
        tableIntList.remove(Integer.valueOf(4));

        for (Table table : testRestaurant.getTables()) {
            assertEquals(tableStringList.get(testRestaurant.getTables().indexOf(table)), table.getName());
            assertEquals(tableIntList.get(testRestaurant.getTables().indexOf(table)), table.getMaxOccupancy());
        }

        testRestaurant.removeTable("Test Table 3");
        tableStringList.remove("Test Table 3");
        tableIntList.remove(Integer.valueOf(10));

        for (Table table : testRestaurant.getTables()) {
            assertEquals(tableStringList.get(testRestaurant.getTables().indexOf(table)), table.getName());
            assertEquals(tableIntList.get(testRestaurant.getTables().indexOf(table)), table.getMaxOccupancy());
        }

        testRestaurant.removeTable("Test Table 1");
        tableStringList.remove("Test Table 1");
        tableIntList.remove(Integer.valueOf(5));

        for (Table table : testRestaurant.getTables()) {
            assertEquals(tableStringList.get(testRestaurant.getTables().indexOf(table)), table.getName());
            assertEquals(tableIntList.get(testRestaurant.getTables().indexOf(table)), table.getMaxOccupancy());
        }

        assertEquals(testTableList, testRestaurant.getTables());

    }

    @Test
    void testAssignCustomers() {
        assertEquals("No Table", testRestaurant.assignCustomers(10));

        testRestaurant.addTable(4, "Test Table 1");
        assertEquals("No Table", testRestaurant.assignCustomers(9));
        assertEquals(0, testRestaurant.getTotalCustomers());
        assertTrue(testRestaurant.findTable("Test Table 1").getAvailabilityStatus());
        assertEquals("No Table", testRestaurant.assignCustomers(4));
        assertEquals(0, testRestaurant.getTotalCustomers());
        assertTrue(testRestaurant.findTable("Test Table 1").getAvailabilityStatus());

        testRestaurant.removeTable("Test Table 1");
        testRestaurant.addTable(4, "Test Table 1");
        testRestaurant.findTable("Test Table 1").trueSetTable();

        assertEquals("No Table", testRestaurant.assignCustomers(9));
        assertEquals(0, testRestaurant.getTotalCustomers());
        assertTrue(testRestaurant.findTable("Test Table 1").getAvailabilityStatus());
        assertEquals("Test Table 1", testRestaurant.assignCustomers(4));
        assertEquals(4, testRestaurant.getTotalCustomers());
        assertFalse(testRestaurant.findTable("Test Table 1").getAvailabilityStatus());

        testRestaurant.removeTable("Test Table 1");
        testRestaurant.addTable(4, "Test Table 1");
        testRestaurant.findTable("Test Table 1").trueSetTable();
        testRestaurant.addTable(5, "Test Table 2");
        testRestaurant.findTable("Test Table 2").trueSetTable();
        testRestaurant.addTable(10, "Test Table 3");
        testRestaurant.findTable("Test Table 3").trueSetTable();

        assertEquals("No Table", testRestaurant.assignCustomers(11));
        assertEquals(4, testRestaurant.getTotalCustomers());
        assertTrue(testRestaurant.findTable("Test Table 1").getAvailabilityStatus());
        assertTrue(testRestaurant.findTable("Test Table 2").getAvailabilityStatus());
        assertTrue(testRestaurant.findTable("Test Table 3").getAvailabilityStatus());

        assertEquals("Test Table 2", testRestaurant.assignCustomers(5));
        assertEquals(9, testRestaurant.getTotalCustomers());
        assertTrue(testRestaurant.findTable("Test Table 1").getAvailabilityStatus());
        assertFalse(testRestaurant.findTable("Test Table 2").getAvailabilityStatus());
        assertTrue(testRestaurant.findTable("Test Table 3").getAvailabilityStatus());

        assertEquals("Test Table 3", testRestaurant.assignCustomers(5));
        assertEquals(14, testRestaurant.getTotalCustomers());
        assertTrue(testRestaurant.findTable("Test Table 1").getAvailabilityStatus());
        assertFalse(testRestaurant.findTable("Test Table 2").getAvailabilityStatus());
        assertFalse(testRestaurant.findTable("Test Table 3").getAvailabilityStatus());

        assertEquals("No Table", testRestaurant.assignCustomers(11));
        assertEquals(14, testRestaurant.getTotalCustomers());
        assertTrue(testRestaurant.findTable("Test Table 1").getAvailabilityStatus());
        assertFalse(testRestaurant.findTable("Test Table 2").getAvailabilityStatus());
        assertFalse(testRestaurant.findTable("Test Table 3").getAvailabilityStatus());

        assertEquals("Test Table 1", testRestaurant.assignCustomers(2));
        assertEquals(16, testRestaurant.getTotalCustomers());
        assertFalse(testRestaurant.findTable("Test Table 1").getAvailabilityStatus());
        assertFalse(testRestaurant.findTable("Test Table 2").getAvailabilityStatus());
        assertFalse(testRestaurant.findTable("Test Table 3").getAvailabilityStatus());

        testRestaurant.removeTable("Test Table 1");
        testRestaurant.removeTable("Test Table 2");
        testRestaurant.removeTable("Test Table 2");
        testRestaurant.addTable(4, "Test Table 1");
        testRestaurant.findTable("Test Table 1").trueSetTable();
        testRestaurant.addTable(5, "Test Table 2");
        testRestaurant.findTable("Test Table 2").trueSetTable();
        testRestaurant.addTable(10, "Test Table 3");
        testRestaurant.findTable("Test Table 3").trueSetTable();

        assertEquals("Test Table 3", testRestaurant.assignCustomers(7));
        assertEquals(23, testRestaurant.getTotalCustomers());
        assertTrue(testRestaurant.findTable("Test Table 1").getAvailabilityStatus());
        assertTrue(testRestaurant.findTable("Test Table 2").getAvailabilityStatus());
        assertFalse(testRestaurant.findTable("Test Table 3").getAvailabilityStatus());
    }

    @Test
    void testValidStatuses() {
        testRestaurant.addTable(4, "Test Table 1");
        assertFalse(testRestaurant.validStatuses(testRestaurant.findTable("Test Table 1")));

        testRestaurant.findTable("Test Table 1").trueSetTable();
        assertTrue(testRestaurant.validStatuses(testRestaurant.findTable("Test Table 1")));

        testRestaurant.findTable("Test Table 1").occupyTable();
        assertFalse(testRestaurant.validStatuses(testRestaurant.findTable("Test Table 1")));

        testRestaurant.findTable("Test Table 1").trueSetTable();
        assertFalse(testRestaurant.validStatuses(testRestaurant.findTable("Test Table 1")));

        testRestaurant.findTable("Test Table 1").emptyTable();
        assertFalse(testRestaurant.validStatuses(testRestaurant.findTable("Test Table 1")));

        testRestaurant.findTable("Test Table 1").trueSetTable();
        assertFalse(testRestaurant.validStatuses(testRestaurant.findTable("Test Table 1")));

        testRestaurant.findTable("Test Table 1").cleanTable();
        testRestaurant.findTable("Test Table 1").occupyTable();
        testRestaurant.findTable("Test Table 1").emptyTable();
        testRestaurant.findTable("Test Table 1").cleanTable();
        assertFalse(testRestaurant.validStatuses(testRestaurant.findTable("Test Table 1")));

        testRestaurant.findTable("Test Table 1").setAvailabilityStatusTrue();
        assertFalse(testRestaurant.validStatuses(testRestaurant.findTable("Test Table 1")));
    }

    @Test
    void testFindTable() {
        testRestaurant.addTable(4, "Test Table 1");
        testRestaurant.addTable(5, "Test Table 2");
        testRestaurant.addTable(10, "Test Table 3");

        assertEquals("Test Table 1", testRestaurant.findTable("Test Table 1").getName());
        assertEquals("Test Table 2", testRestaurant.findTable("Test Table 2").getName());
        assertEquals("Test Table 3", testRestaurant.findTable("Test Table 3").getName());
    }

    @Test
    void testTablePay() {
        testRestaurant.addTable(4, "Test Table 1");
        testRestaurant.findTable("Test Table 1").occupyTable();
        testRestaurant.addTable(5, "Test Table 2");
        testRestaurant.findTable("Test Table 2").occupyTable();
        testRestaurant.addTable(10, "Test Table 3");
        testRestaurant.findTable("Test Table 3").occupyTable();
        testRestaurant.orderFood("Test Table 3", "Lemonade");

        testRestaurant.tablePay("Test Table 3");
        assertFalse(testRestaurant.findTable("Test Table 1").getBill().getPayStatus());
        assertFalse(testRestaurant.findTable("Test Table 2").getBill().getPayStatus());
        assertTrue(testRestaurant.findTable("Test Table 3").getBill().getPayStatus());
        assertEquals(2.00, testRestaurant.getEarnings());
        assertEquals(0.00, testRestaurant.getTips());

        testRestaurant.removeTable("Test Table 1");
        testRestaurant.removeTable("Test Table 2");
        testRestaurant.removeTable("Test Table 2");
        testRestaurant.addTable(4, "Test Table 1");
        testRestaurant.findTable("Test Table 1").occupyTable();
        testRestaurant.orderFood("Test Table 1", "Single Cheeseburger");
        testRestaurant.findTable("Test Table 1").getBill().setTip(3.00);
        testRestaurant.addTable(5, "Test Table 2");
        testRestaurant.findTable("Test Table 2").occupyTable();
        testRestaurant.orderFood("Test Table 2", "Vegan Burger");
        testRestaurant.findTable("Test Table 2").getBill().setTip(5.30);
        testRestaurant.addTable(10, "Test Table 3");
        testRestaurant.findTable("Test Table 3").occupyTable();

        testRestaurant.tablePay("Test Table 1");
        assertTrue(testRestaurant.findTable("Test Table 1").getBill().getPayStatus());
        assertEquals(6.50, testRestaurant.getEarnings());
        assertEquals(3.00, testRestaurant.getTips());
        assertFalse(testRestaurant.findTable("Test Table 3").getBill().getPayStatus());

        testRestaurant.tablePay("Test Table 2");
        assertTrue(testRestaurant.findTable("Test Table 2").getBill().getPayStatus());
        assertEquals(12.50, testRestaurant.getEarnings());
        assertEquals(8.30, testRestaurant.getTips());
    }

    @Test
    void testOrderFood() {
        testRestaurant.addTable(4, "Test Table 1");
        testRestaurant.findTable("Test Table 1").occupyTable();
        testRestaurant.addTable(5, "Test Table 2");
        testRestaurant.findTable("Test Table 2").occupyTable();
        testRestaurant.addTable(10, "Test Table 3");
        testRestaurant.findTable("Test Table 3").occupyTable();

        testRestaurant.orderFood("Test Table 3", "Single Cheeseburger");
        assertEquals(4.50, testRestaurant.findTable("Test Table 3").getBill().getCost());

        testRestaurant.orderFood("Test Table 3", "Lemonade");
        assertEquals(6.50, testRestaurant.findTable("Test Table 3").getBill().getCost());

        testRestaurant.orderFood("Test Table 1", "Vegan Burger");
        assertEquals(6.00, testRestaurant.findTable("Test Table 1").getBill().getCost());

        assertEquals(0.00, testRestaurant.findTable("Test Table 2").getBill().getCost());
    }


}
