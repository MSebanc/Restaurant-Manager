package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Event class
 */
public class EventTest {
	private Event e;
	private Date d;
	
	//NOTE: these tests might fail if time at which line (2) below is executed
	//is different from time that line (1) is executed.  Lines (1) and (2) must
	//run in same millisecond for this test to make sense and pass.
	
	@BeforeEach
	public void runBefore() {
		e = new Event("Added Table To Restaurant");   // (1)
		d = Calendar.getInstance().getTime();   // (2)
	}

	@Test
	public void testEvent() {
		assertEquals("Added Table To Restaurant", e.getDescription());
		assertEquals(d, e.getDate());
	}

    @Test
    public void testEquals() {
        List<Object> equalsTestList = new ArrayList<>();

        equalsTestList.add(null);
        equalsTestList.add(new Bill());
        equalsTestList.add(new Event("Removed Table From Restaurant"));
        equalsTestList.add(e);

        for (Object o : equalsTestList) {
            boolean b = e.equals(o);
            if (e.equals(o)) {
                assertTrue(b);
            } else {
                assertFalse(b);
            }
        }
    }

    @Test
    public void testHashCode() {
        Event otherEvent = new Event("Removed Table From Restaurant");

        assertEquals((13 * e.getDate().hashCode() + e.getDescription().hashCode()), e.hashCode());
        assertEquals((13 * otherEvent.getDate().hashCode() + otherEvent.getDescription().hashCode()), otherEvent.hashCode());
    }

	@Test
	public void testToString() {
		assertEquals(d.toString() + "\n" + "Added Table To Restaurant", e.toString());
	}
}
