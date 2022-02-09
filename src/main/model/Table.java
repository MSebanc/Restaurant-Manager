package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Represents a table with clean, set, availability, and food delivery statuses as well as a name, max occupancy, Bill,
// and lists for cleaning history, delivery history, and purchase history
public class Table {


    private Boolean cleanStatus;
    private Boolean setStatus;
    private Boolean availabilityStatus;
    private Boolean foodDeliveryStatus;
    private String name;
    private int maxOccupancy;
    private List<String> cleaningHistory;
    private List<String> deliveryHistory;
    private List<String> purchaseHistory;
    private Bill bill;

    // EFFECTS: Constructs a table with cleaning status true, set status false, food delivery status true,
    // availability status true, an empty cleaning history, delivery history and purchase history, list, and
    // given max occupancy, bill to null, and sets name to given name
    public Table(int max, String name) {
        this.cleanStatus = true;
        this.setStatus = false;
        this.availabilityStatus = true;
        this.foodDeliveryStatus = true;
        this.name = name;
        this.maxOccupancy = max;
        this.cleaningHistory = new ArrayList<>();
        this.deliveryHistory = new ArrayList<>();
        this.purchaseHistory = new ArrayList<>();
        this.bill = null;
    }

    // MODIFIES: this
    // EFFECTS: cleans table and records cleaning time in cleaning history
    // MODIFIES: this
    public void cleanTable() {
        LocalDateTime cleaningTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCleaningTime = cleaningTime.format(formatter);
        cleaningHistory.add(formattedCleaningTime);
        cleanStatus = true;

    }

    // MODIFIES: this
    // EFFECTS: sets availability status, clean status, and set status to false and set bill to null
    public void emptyTable() {
        availabilityStatus = true;
        cleanStatus = false;
        setStatus = false;
        bill = null;

    }

    // MODIFIES: this, Bill
    // EFFECTS: sets Bill pay status to true and records purchase time in purchase history
    public void payForFood() {
        LocalDateTime purchaseTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedPurchaseTime = purchaseTime.format(formatter);
        purchaseHistory.add(formattedPurchaseTime);
        bill.payBill();
    }

    // MODIFIES: this
    // EFFECTS: sets availability status to false and creates a new bill and changes food delivery status to true
    public void occupyTable() {
        availabilityStatus = false;
        foodDeliveryStatus = true;
        bill = new Bill();
    }

    // MODIFIES: this
    // EFFECTS: adds food delivery time to delivery history and set food delivered to true
    public void deliverFood() {
        LocalDateTime deliveryTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDeliveryTime = deliveryTime.format(formatter);
        deliveryHistory.add(formattedDeliveryTime);

        foodDeliveryStatus = true;
    }


    //setters
    public void trueSetTable() {
        setStatus = true;
    }

    public void falseDeliveryStatus() {
        foodDeliveryStatus = false;
    }

    // getters

    public Boolean getCleanStatus() {
        return cleanStatus;
    }

    public Boolean getSetStatus() {
        return setStatus;
    }

    public Boolean getAvailabilityStatus() {
        return availabilityStatus;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public List<String> getCleaningHistory() {
        return cleaningHistory;
    }

    public List<String> getPurchaseHistory() {
        return purchaseHistory;
    }

    public List<String> getDeliveryHistory() {
        return deliveryHistory;
    }

    public String getName() {
        return name;
    }

    public Bill getBill() {
        return bill;
    }

    public Boolean getFoodDeliveryStatus() {
        return foodDeliveryStatus;
    }

}
