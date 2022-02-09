package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Represents a restaurant with tables, menu, count of total customers, tips (in dollars), and earnings (in dollars)
public class Restaurant {

    private Menu menu;
    private List<Table> tables;
    private int totalCustomers;
    private Double tips;
    private Double earnings;


    // EFFECTS: constructs a restaurant with menu, empty list of tables, total customers set to 0,
    // table count set to 0, tips set to 0, and earnings set to 0
    public Restaurant() {
        this.menu = new Menu();
        this.tables = new ArrayList<>();
        this.totalCustomers = 0;
        this.tips = 0.00;
        this.earnings = 0.00;
    }

    // REQUIRES: name not already in list of tables, name is not null, max > 0
    // MODIFIES: this
    // EFFECTS: adds table with given name and given max to list of tables
    public void addTable(int max, String name) {
        tables.add(new Table(max, name));
    }

    // REQUIRES: name is in list of tables
    // MODIFIES: this
    // EFFECTS: removes table with given name from list of tables
    public void removeTable(String name) {
        int index = 0;
        for (Table table : tables) {
            if (Objects.equals(table.getName().toLowerCase(), name.toLowerCase())) {
                tables.remove(index);
                break;
            }
            index++;
        }
    }

    // REQUIRES: partySize > 0
    // MODIFIES: this, Table
    // EFFECTS: if there is an available table with max size = party size or max size > party size and
    // table is valid to have someone occupying it, party size is added to total customers, occupyTable() is
    // called on Table, and table name is returned. Otherwise, returns "No Table"
    public String assignCustomers(int partySize) {
        String assigned = "No Table";
        for (Table table : tables) {
            if (partySize == table.getMaxOccupancy() && validStatuses(table)) {
                totalCustomers += partySize;
                table.occupyTable();
                assigned = table.getName();
            }
        }
        if (Objects.equals(assigned, "No Table")) {
            for (Table table : tables) {
                if (partySize < table.getMaxOccupancy() && validStatuses(table)) {
                    totalCustomers += partySize;
                    table.occupyTable();
                    assigned = table.getName();
                }
            }
        }
        return assigned;
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
            if (Objects.equals(table.getName(), name)) {
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
    // table's bill cost
    public void orderFood(String tableName, String foodName) {
        Table table = findTable(tableName);
        for (Food food : menu.getMenu()) {
            if (Objects.equals(food.getName(), foodName)) {
                table.getBill().addCost(food.getPrice());
                table.falseDeliveryStatus();
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
}

