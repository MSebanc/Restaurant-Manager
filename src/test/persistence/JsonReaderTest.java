package persistence;

import model.Bill;
import model.Restaurant;
import model.Table;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/json/noSuchFile.json");
        try {
            Restaurant r = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyRestaurant() {
        JsonReader reader = new JsonReader("./data/json/testReaderEmptyRestaurant.json");
        try {
            Restaurant r = reader.read();
            assertTrue(r.getTables().isEmpty());
            assertEquals("Empty Restaurant", r.getName());
            assertEquals(0, r.getTotalCustomers());
            assertEquals(0.00, r.getTips());
            assertEquals(0.00, r.getEarnings());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/json/testReaderGeneralRestaurant.json");
        try {
            Restaurant r = reader.read();

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
            assertFalse(t2.getCleaningHistory().isEmpty());
            assertFalse(t2.getDeliveryHistory().isEmpty());
            assertFalse(t2.getPurchaseHistory().isEmpty());
            assertTrue(t2.getAvailabilityStatus());
            assertEquals(0, b2.getCost());
            assertFalse(b2.getPayStatus());
            assertEquals(0, b2.getTip());
            assertFalse(t2.getSetStatus());
            assertEquals("Table 2", t2.getName());
            assertEquals(7, t2.getMaxOccupancy());

            assertEquals("Test Restaurant", r.getName());
            assertEquals(18, r.getTotalCustomers());
            assertEquals(12.35, r.getTips());
            assertEquals(19.75, r.getEarnings());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}