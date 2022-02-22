package persistence;

import model.Bill;
import model.Restaurant;
import model.Table;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Restaurant read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRestaurant(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private Restaurant parseRestaurant(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int totalCustomers = jsonObject.getInt("total customers");
        Double tips = jsonObject.getDouble("tips");
        Double earnings = jsonObject.getDouble("earnings");
        Restaurant r = new Restaurant(name, totalCustomers, tips, earnings);
        addTables(r, jsonObject);
        return r;
    }

    // MODIFIES: r
    // EFFECTS: parses tables from JSON object and adds them to workroom
    private void addTables(Restaurant r, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("tables");
        for (Object json : jsonArray) {
            JSONObject nextTable = (JSONObject) json;
            addTable(r, nextTable);
        }
    }

    // MODIFIES: r
    // EFFECTS: parses table from JSON object and adds it to workroom
    private void addTable(Restaurant r, JSONObject jsonObject) {
        boolean cleanStatus = jsonObject.getBoolean("clean status");
        boolean setStatus = jsonObject.getBoolean("set status");
        boolean availabilityStatus = jsonObject.getBoolean("availability status");
        boolean foodDeliveryStatus = jsonObject.getBoolean("food delivery status");
        String name = jsonObject.getString("name");
        int maxOccupancy = jsonObject.getInt("max occupancy");
        List<String> cleaningHistory = toCleaningHistory(jsonObject);
        List<String> deliveryHistory = toDeliveryHistory(jsonObject);
        List<String> purchaseHistory = toPurchaseHistory(jsonObject);

        Table t = new Table(cleanStatus, setStatus, availabilityStatus, foodDeliveryStatus, name, maxOccupancy,
                cleaningHistory, deliveryHistory, purchaseHistory);

        JSONObject bill = jsonObject.getJSONObject("bill");
        addBill(t, bill);

        r.addTableFromJson(t);
    }

    private List<String> toCleaningHistory(JSONObject jsonObject) {
        JSONArray jsonArrayCleaning = jsonObject.getJSONArray("cleaning history");
        List<String> cleaningHistory = new ArrayList<>();
        for (Object json : jsonArrayCleaning) {
            String nextCleaningHistory = (String) json;
            cleaningHistory.add(nextCleaningHistory);
        }
        return cleaningHistory;
    }

    private List<String> toDeliveryHistory(JSONObject jsonObject) {
        JSONArray jsonArrayDelivery = jsonObject.getJSONArray("delivery history");
        List<String> deliveryHistory = new ArrayList<>();
        for (Object json : jsonArrayDelivery) {
            String nextDeliveryHistory = (String) json;
            deliveryHistory.add(nextDeliveryHistory);
        }
        return deliveryHistory;
    }

    private List<String> toPurchaseHistory(JSONObject jsonObject) {
        JSONArray jsonArrayPurchase = jsonObject.getJSONArray("purchase history");
        List<String> purchaseHistory = new ArrayList<>();
        for (Object json : jsonArrayPurchase) {
            String nextPurchaseHistory = (String) json;
            purchaseHistory.add(nextPurchaseHistory);
        }
        return purchaseHistory;
    }

    private void addBill(Table t, JSONObject jsonObject) {
        Double cost = jsonObject.getDouble("cost");
        Double tip = jsonObject.getDouble("tip");
        boolean payStatus = jsonObject.getBoolean("pay status");

        Bill b = new Bill(cost, tip, payStatus);
        t.addBillFromJson(b);
    }
}
