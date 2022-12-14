package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Represents a table with clean, set, availability, and food delivery statuses as well as a name, max occupancy, Bill,
// and lists for cleaning history, delivery history, and purchase history
public class Table implements Writable {


    private Boolean cleanStatus;
    private Boolean setStatus;
    private Boolean availabilityStatus;
    private Boolean foodDeliveryStatus;
    private final String name;
    private final int maxOccupancy;
    private final List<String> cleaningHistory;
    private final List<String> deliveryHistory;
    private final List<String> purchaseHistory;
    private Bill bill;

    // EFFECTS: Constructs a table with cleaning status true, set status false, food delivery status true,
    // availability status true, an empty cleaning history, delivery history and purchase history, list, and
    // given max occupancy, creates a new bill, and sets name to given name
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
        this.bill = new Bill();
    }

    // EFFECTS: Constructs a table with given cleaning status, set status, availability status, food delivery status
    // name, maxOccupancy, cleaning history, delivery history, and purchase history and creates a new bill
    public Table(boolean cleanStatus, boolean setStatus, boolean availabilityStatus, boolean foodDeliveryStatus,
                 String name, int maxOccupancy, List<String> cleaningHistory, List<String> deliveryHistory,
                 List<String> purchaseHistory) {
        this.cleanStatus = cleanStatus;
        this.setStatus = setStatus;
        this.availabilityStatus = availabilityStatus;
        this.foodDeliveryStatus = foodDeliveryStatus;
        this.name = name;
        this.maxOccupancy = maxOccupancy;
        this.cleaningHistory = cleaningHistory;
        this.deliveryHistory = deliveryHistory;
        this.purchaseHistory = purchaseHistory;
        this.bill = new Bill();
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
    // EFFECTS: sets availability status, clean status, and set status to false and sets bill to a new Bill
    public void emptyTable() {
        availabilityStatus = true;
        cleanStatus = false;
        setStatus = false;
        bill = new Bill();

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
    // EFFECTS: sets availability status to false, sets bill to a new Bill and changes food delivery status to true
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

    // setter only for test
    public void setAvailabilityStatusTrue() {
        availabilityStatus = true;
    }

    // EFFECTS: writes JSON representation of Table and returns it
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("clean status", cleanStatus);
        json.put("set status", setStatus);
        json.put("availability status", availabilityStatus);
        json.put("food delivery status", foodDeliveryStatus);
        json.put("name", name);
        json.put("max occupancy", maxOccupancy);
        json.put("cleaning history", convertListToJsonArray(cleaningHistory));
        json.put("delivery history", convertListToJsonArray(deliveryHistory));
        json.put("purchase history", convertListToJsonArray(purchaseHistory));
        json.put("bill", bill.toJson());

        return json;
    }

    // EFFECTS: turns list of strings into a jsonArray and returns the jsonArray
    private JSONArray convertListToJsonArray(List<String> history) {
        JSONArray jsonArray = new JSONArray();
        for (String date : history) {
            jsonArray.put(date);
        }
        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: sets bill to given Bill
    public void addBillFromJson(Bill b) {
        bill = b;
    }
}
