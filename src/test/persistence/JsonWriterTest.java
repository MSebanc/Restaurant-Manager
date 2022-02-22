package persistence;

import model.Bill;
import model.Restaurant;
import model.Table;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            Restaurant r = new Restaurant("My Restaurant");
            JsonWriter writer = new JsonWriter("./data/json/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            Restaurant r = new Restaurant("My Restaurant");
            JsonWriter writer = new JsonWriter("./data/json/testWriterEmptyRestaurant.json");
            writer.open();
            writer.write(r);
            writer.close();

            JsonReader reader = new JsonReader("./data/json/testWriterEmptyRestaurant.json");
            r = reader.read();
            assertEquals("My Restaurant", r.getName());
            assertTrue(r.getTables().isEmpty());
            assertEquals(0, r.getTotalCustomers());
            assertEquals(0.00, r.getTips());
            assertEquals(0.00, r.getEarnings());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Restaurant r = new Restaurant("My Restaurant", 18, 12.35, 19.75);

            List<String> cleaningHistory = new ArrayList<>();
            cleaningHistory.add("2022-02-21 18:46:04");
            cleaningHistory.add("2022-02-21 18:42:58");

            List<String> deliveryHistory = new ArrayList<>();
            deliveryHistory.add("2022-02-21 18:41:38");
            deliveryHistory.add("2022-02-21 18:49:10");

            List<String> purchaseHistory = new ArrayList<>();
            purchaseHistory.add("2022-02-21 18:47:43");
            purchaseHistory.add("2022-02-21 18:49:24");

            Table t = new Table(true, true, false, false,
                    "Table 1", 4, cleaningHistory, deliveryHistory, purchaseHistory);
            t.addBillFromJson(new Bill(15.25, 2.00, false));
            r.addTableFromJson(t);
            r.addTableFromJson(new Table(7, "Table 2"));

            JsonWriter writer = new JsonWriter("./data/json/testWriterGeneralRestaurant.json");
            writer.open();
            writer.write(r);
            writer.close();

            JsonReader reader = new JsonReader("./data/json/testWriterGeneralRestaurant.json");
            r = reader.read();

            Table t1 = r.getTables().get(0);
            Bill b1 = t1.getBill();
            assertFalse(r.getTables().isEmpty());
            assertFalse(t1.getFoodDeliveryStatus());
            assertFalse(t1.getCleaningHistory().isEmpty());
            assertFalse(t1.getDeliveryHistory().isEmpty());
            assertFalse(t1.getPurchaseHistory().isEmpty());
            assertFalse(t1.getAvailabilityStatus());
            assertEquals(15.25, b1.getCost());
            assertFalse(b1.getPayStatus());
            assertEquals(2.00, b1.getTip());
            assertTrue(t1.getSetStatus());
            assertTrue(t1.getCleanStatus());
            assertEquals("Table 1", t1.getName());
            assertEquals(4, t1.getMaxOccupancy());

            Table t2 = r.getTables().get(1);
            Bill b2 = t2.getBill();
            assertFalse(r.getTables().isEmpty());
            assertTrue(t2.getFoodDeliveryStatus());
            assertTrue(t2.getCleaningHistory().isEmpty());
            assertTrue(t2.getDeliveryHistory().isEmpty());
            assertTrue(t2.getPurchaseHistory().isEmpty());
            assertTrue(t2.getAvailabilityStatus());
            assertEquals(0.00, b2.getCost());
            assertFalse(b2.getPayStatus());
            assertEquals(0.00, b2.getTip());
            assertFalse(t2.getSetStatus());
            assertTrue(t2.getCleanStatus());
            assertEquals("Table 2", t2.getName());
            assertEquals(7, t2.getMaxOccupancy());

            assertEquals("My Restaurant", r.getName());
            assertEquals(18, r.getTotalCustomers());
            assertEquals(12.35, r.getTips());
            assertEquals(19.75, r.getEarnings());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}