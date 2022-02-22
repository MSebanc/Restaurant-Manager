package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a food item with a name and price
public class Food {

    private final String name;
    private final Double price;


    // REQUIRED: Price must have no more than two decimal points
    // EFFECTS: Constructs food with given name and price
    public Food(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // getters
    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

}
