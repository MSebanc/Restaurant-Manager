package model;

public class Food {

    private String name;
    private Double price;


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
