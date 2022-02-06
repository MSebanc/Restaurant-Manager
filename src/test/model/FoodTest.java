package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FoodTest {

    private Food testFood;

    @BeforeEach
    void runBefore() {
        testFood = new Food("Test Food", 10.00);
    }

    @Test
    void testConstructor() {
        assertEquals("Test Food", testFood.getName());
        assertEquals(10.00, testFood.getPrice());
    }
}
