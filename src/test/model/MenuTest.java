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
        assertEquals(4.00, testMenu.getFoodPrice("Single Burger"));
        assertEquals(5.50, testMenu.getFoodPrice("Double Cheeseburger"));
        assertEquals(2.50, testMenu.getFoodPrice("Seasonal Lemonade"));
        assertEquals(1.75, testMenu.getFoodPrice("Soda"));
    }
}
