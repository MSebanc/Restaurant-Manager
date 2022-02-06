package model;

// Represents a bill with cost, tip, and a pay status. Also has a set tax amount.
public class Bill {

    private static final Double TAX = 0.05;

    private Double cost;
    private Double tip;
    private boolean payStatus;

    // EFFECTS: Constructs a bill with amount and tip set to 0 and pay status as false
    public Bill() {
        this.cost = 0.00;
        this.tip = 0.00;
        this.payStatus = false;
    }

    // MODIFIES: this
    // EFFECTS: changes payStatus to true
    public void payBill() {
        this.payStatus = true;
    }

    // REQUIRES: amount > 0 and no more than two decimal places
    // MODIFIES: this
    // EFFECTS: adds given amount to cost
    public void addCost(Double amount) {
        cost += amount;
    }

    // REQUIRES: amount > 0 and no more than two decimal places
    // MODIFIES: this
    // EFFECTS: sets tip to given amount
    public void setTip(Double amount) {
        tip = amount;
    }

    // EFFECTS: gets the total cost of the meal by adding the cost of food to the price due to tax and the tip
    public Double getTotalCost() {
        return (double) Math.round((cost + cost * TAX + tip) * 100d) / 100d;
    }

    // getters
    public Double getTip() {
        return tip;
    }

    public Double getCost() {
        return cost;
    }

    public Boolean getPayStatus() {
        return payStatus;
    }
}
