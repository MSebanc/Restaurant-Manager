package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Menu {

    private List<Food> menu;

    // EFFECTS: Constructs a menu and adds food items to the menu
    public Menu() {
        this.menu = new ArrayList<>();
        Food singleBurger = new Food("Single Burger", 4.00);
        menu.add(singleBurger);
        Food doubleBurger = new Food("Double Burger", 5.00);
        menu.add(doubleBurger);
        Food singleCheeseburger = new Food("Single Cheeseburger", 4.50);
        menu.add(singleCheeseburger);
        Food doubleCheeseburger = new Food("Double Cheeseburger", 5.50);
        menu.add(doubleCheeseburger);
        Food veggieBurger = new Food("Veggie Burger", 5.00);
        menu.add(veggieBurger);
        Food veganBurger = new Food("Vegan Burger", 6.00);
        menu.add(veganBurger);
        Food originalFries = new Food("Original Fries", 3.00);
        menu.add(originalFries);
        Food baconCheeseFries = new Food("Bacon and Cheese Fries", 5.00);
        menu.add(baconCheeseFries);
        Food lemonade = new Food("Lemonade", 2.00);
        menu.add(lemonade);
        Food seasonalLemonade = new Food("Seasonal Lemonade", 2.50);
        menu.add(seasonalLemonade);
        Food soda = new Food("Soda", 1.75);
        menu.add(soda);
    }

    // REQUIRES: given food exists in menu
    // EFFECTS: gets the price of food
    public Double getFoodPrice(String foodName) {
        Double foodPrice = 0.00;
        for (Food food : menu) {
            if (Objects.equals(food.getName(), foodName)) {
                foodPrice = food.getPrice();
                break;
            }
        }
        return foodPrice;
    }

    // getters
    public List<Food> getMenu() {
        return menu;
    }
}
