package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuTest {

    private Menu testMenu;

    @BeforeEach
    void runBefore() {
        testMenu = new Menu();
    }

    @Test
    void testConstructor() {
        assertEquals(11, testMenu.getMenu().size());
    }

    @Test
    void testGetFoodPrice() {
        for (Food food: testMenu.getMenu()) {
            assertEquals(food.getPrice(), testMenu.getFoodPrice(food.getName()));
        }
        assertEquals(0.00, testMenu.getFoodPrice("Does not meet if statement"));
    }
}
