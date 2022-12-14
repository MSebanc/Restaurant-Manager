package model;

import model.exceptions.InvalidPartySizeInputException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Represents a restaurant with tables, menu, count of total customers, tips (in dollars), and earnings (in dollars)
public class Restaurant implements Writable {

    private final Menu menu;
    private final List<Table> tables;
    private final String name;
    private int totalCustomers;
    private Double tips;
    private Double earnings;


    // EFFECTS: constructs a restaurant with menu, empty list of tables, given name, total customers set to 0,
    // table count set to 0, tips set to 0, and earnings set to 0
    public Restaurant(String name) {
        this.menu = new Menu();
        this.tables = new ArrayList<>();
        this.name = name;
        this.totalCustomers = 0;
        this.tips = 0.00;
        this.earnings = 0.00;
    }

    // EFFECTS: constructs a restaurant with menu, empty list of tables, given name, given total customers, given tips,
    // and given earnings
    public Restaurant(String name, int totalCustomers, Double tips, Double earnings) {
        this.menu = new Menu();
        this.tables = new ArrayList<>();
        this.name = name;
        this.totalCustomers = totalCustomers;
        this.tips = tips;
        this.earnings = earnings;
    }

    // REQUIRES: name not already in list of tables, name is not null, max > 0
    // MODIFIES: this
    // EFFECTS: adds table with given name and given max to list of tables and logs event
    public void addTable(int max, String name) {
        tables.add(new Table(max, name));
        EventLog.getInstance().logEvent(new Event("Added " + name + " to " + this.name));
    }

    // REQUIRES: name is in list of tables
    // MODIFIES: this
    // EFFECTS: removes table with given name from list of tables and logs event
    public void removeTable(String name) {
        int index = 0;
        for (Table table : tables) {
            if (table.getName().equalsIgnoreCase(name)) {
                tables.remove(index);
                EventLog.getInstance().logEvent(new Event("Removed " + name + " from " + this.name));
                break;
            }
            index++;
        }
    }

    // REQUIRES: partySize > 0
    // EFFECTS: returns a list of tables where the table's max occupancy is greater or equal than the party size as well
    // a valid based on validStatuses(table);
    public List<Table> validTables(int partySize) throws InvalidPartySizeInputException {
        List<Table> validTables = new ArrayList<>();

        for (Table table: tables) {
            if (table.getMaxOccupancy() >= partySize && validStatuses(table)) {
                validTables.add(table);
            }
        }

        if (validTables.isEmpty()) {
            throw new InvalidPartySizeInputException();
        }

        return validTables;
    }

    // REQUIRES: partySize > 0
    // MODIFIES: this
    // EFFECTS: adds party size to totalCustomers and calls findBestTableToAssign with list of the max occupancy of
    // every table in tables and returns the result
    public Table assignCustomers(List<Table> tables, int partySize) {
        List<Integer> tableMaxOccupancy = new ArrayList<>();

        for (Table table: tables) {
            tableMaxOccupancy.add(table.getMaxOccupancy());
        }

        totalCustomers += partySize;
        return findBestTableToAssign(tables, tableMaxOccupancy, partySize);
    }

    // REQUIRES: partySize > 0
    // EFFECTS: returns the table in a list of given tables where the max occupancy is closest to the given partySize
    public Table findBestTableToAssign(List<Table> tables, List<Integer> tableMaxOccupancy,
                                        int partySize) {
        Table bestTableToAssign = null;
        int bestTableToAssignInt = Integer.MAX_VALUE;
        List<Integer> closeToMax = new ArrayList<>();
        for (int max: tableMaxOccupancy) {
            closeToMax.add(max - partySize);
        }

        for (int i: closeToMax) {
            if (i < bestTableToAssignInt) {
                bestTableToAssign = tables.get(closeToMax.indexOf(i));
                bestTableToAssignInt = i;
            }
        }
        return bestTableToAssign;
    }

    // EFFECTS: return true if table is clean, set, and available
    public boolean validStatuses(Table table) {
        return (table.getCleanStatus() && table.getSetStatus() && table.getAvailabilityStatus());
    }

    // REQUIRES: name !null, name is in list of tables
    // EFFECTS: returns table with given name
    public Table findTable(String name) {
        Table foundTable = null;
        for (Table table : tables) {
            if (name.equalsIgnoreCase(table.getName())) {
                foundTable = table;
            }
        }
        return foundTable;
    }

    // REQUIRES: name !null, name is in list of tables
    // MODIFIES: this, Table
    // EFFECTS: uses findTable() to get table with given name, calls payForFood() on table and
    // adds amount paid to earnings and amount tipped to tips
    public void tablePay(String name) {
        Table table = findTable(name);
        table.payForFood();
        earnings += table.getBill().getCost();
        tips += table.getBill().getTip();
    }

    // REQUIRES: tableName and foodName !null, tableName is in list of tables, foodName is in list of food in menu
    // MODIFIES: Table, Bill
    // EFFECTS: uses findTable() to get table with given name and find food with given name, adds price to
    // table's bill cost and sets pay status and delivery status to false
    public void orderFood(String tableName, String foodName) {
        Table table = findTable(tableName);
        for (Food food : menu.getMenu()) {
            if (foodName.equalsIgnoreCase(food.getName())) {
                table.getBill().addCost(food.getPrice());
                table.falseDeliveryStatus();
                table.getBill().falsePayStatus();
                break;
            }
        }
    }

    // getters
    public List<Food> getMenu() {
        return menu.getMenu();
    }

    public List<Table> getTables() {
        return tables;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public Double getTips() {
        return tips;
    }

    public Double getEarnings() {
        return earnings;
    }

    public String getName() {
        return name;
    }

    // EFFECTS: writes JSON representation of Restaurant and returns it
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("tables", tablesToJson());
        json.put("name", name);
        json.put("total customers", totalCustomers);
        json.put("tips", tips);
        json.put("earnings", earnings);
        return json;
    }

    // EFFECTS: returns tables in this restaurant as a JSON array
    private JSONArray tablesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Table table : tables) {
            jsonArray.put(table.toJson());
        }

        return jsonArray;
    }

    // REQUIRES: table not already in restaurant
    // MODIFIES: this
    // EFFECTS: adds given table to tables and logs event
    public void addTableFromJson(Table t) {
        tables.add(t);
        EventLog.getInstance().logEvent(new Event("Added " + name + " to " + this.name));
    }
}

