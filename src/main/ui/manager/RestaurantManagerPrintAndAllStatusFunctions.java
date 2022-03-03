package ui.manager;

import model.Food;
import model.Restaurant;
import model.Table;
import persistence.JsonReader;
import ui.interfaces.AllStatusIfBoolean;
import ui.interfaces.TableToStringFunction;

import java.io.FileNotFoundException;
import java.io.IOException;

// Restaurant Manager Application (print functions and boolean check functions)
public class RestaurantManagerPrintAndAllStatusFunctions extends RestaurantManagerFunctions {

    // EFFECTS: Constructor
    public RestaurantManagerPrintAndAllStatusFunctions() throws FileNotFoundException {
        // does nothing
    }

    // EFFECTS: prints all tables name and max occupancy
    protected static void printTablesMax() {
        TableToStringFunction printStatement = (Table table) -> ", Max Occupancy: " + table.getMaxOccupancy();
        printTableFunction(printStatement);
    }

    // EFFECTS: prints all tables name and valid status
    protected static void printValidTables() {
        TableToStringFunction printStatement = (Table table) -> ", Max Occupancy: " + table.getMaxOccupancy()
                + ", Assignable Status: " + restaurant.validStatuses(table);
        printTableFunction(printStatement);
    }

    // EFFECTS: prints all tables name and clean status
    protected static void printCleanTables() {
        TableToStringFunction printStatement = (Table table) -> ", Clean Status: " + table.getCleanStatus();
        printTableFunction(printStatement);
    }

    // EFFECTS: prints all tables name and set status
    protected static void printSetTables() {
        TableToStringFunction printStatement = (Table table) -> ", Set Status: " + table.getSetStatus();
        printTableFunction(printStatement);
    }

    // EFFECTS: prints all tables name and availability status
    protected static void printAvailabilityTables() {
        TableToStringFunction printStatement = (Table table) -> ", Availability Status: "
                + table.getAvailabilityStatus();
        printTableFunction(printStatement);
    }

    // EFFECTS: prints all tables name and order status
    protected static void printOrderTables() {
        TableToStringFunction printStatement = (Table table) -> ", Can Order Food: "
                + !table.getAvailabilityStatus();
        printTableFunction(printStatement);
    }

    // EFFECTS: prints all tables name and delivery status
    protected static void printDeliveryTables() {
        TableToStringFunction printStatement = (Table table) -> ", Delivery Status: " + table.getFoodDeliveryStatus();
        printTableFunction(printStatement);
    }

    // EFFECTS: prints all tables name and pay status
    protected static void printBillTables() {
        TableToStringFunction printStatement = (Table table) -> {
            if (!table.getAvailabilityStatus()) {
                return ", Pay Status: " + table.getBill().getPayStatus();
            } else {
                return ", Not Occupied";
            }
        };
        printTableFunction(printStatement);
    }

    // EFFECTS: prints all tables name
    protected static void printTables() {
        TableToStringFunction printStatement = (Table table) -> "";
        printTableFunction(printStatement);
    }

    // EFFECTS: prints all foods name and price
    protected static void printFood() {
        System.out.println("\nFood That Can Be Ordered: ");
        for (Food food : restaurant.getMenu()) {
            System.out.println("Food Name: " + food.getName() + ", Price: $" + DF.format(food.getPrice()));
        }
    }

    // EFFECTS: prints json stores and the restaurant name that occupies that spot
    protected static void printStores() {
        try {
            jsonStore = "./data/json/store1.json";
            Restaurant store = new JsonReader(jsonStore).read();
            System.out.println("1: " + store.getName());

            jsonStore = "./data/json/store2.json";
            store = new JsonReader(jsonStore).read();
            System.out.println("2: " + store.getName());

            jsonStore = "./data/json/store3.json";
            store = new JsonReader(jsonStore).read();
            System.out.println("3: " + store.getName());

            jsonStore = "./data/json/store4.json";
            store = new JsonReader(jsonStore).read();
            System.out.println("4: " + store.getName());

            jsonStore = "./data/json/store5.json";
            store = new JsonReader(jsonStore).read();
            System.out.println("5: " + store.getName());

        } catch (IOException e) {
            System.out.println("Unable to read from file: " + jsonStore);
        }
    }

    // EFFECTS: returns true if all tables are clean
    protected static boolean allClean() {
        AllStatusIfBoolean ifBoolean = (Table table) -> !table.getCleanStatus();
        return allStatus(ifBoolean);
    }

    // EFFECTS: returns true if all tables are set
    protected static boolean allSet() {
        AllStatusIfBoolean ifBoolean = (Table table) -> !table.getSetStatus();
        return allStatus(ifBoolean);
    }

    // EFFECTS: returns true if all tables are empty
    protected static boolean allEmpty() {
        AllStatusIfBoolean ifBoolean = (Table table) -> !table.getAvailabilityStatus();
        return allStatus(ifBoolean);
    }

    // EFFECTS: returns true if all tables are not hungry
    protected static boolean allNotHungry() {
        AllStatusIfBoolean ifBoolean = (Table table) -> !table.getFoodDeliveryStatus();
        return allStatus(ifBoolean);
    }

    // EFFECTS: returns true if all bills are paid
    protected static boolean allPaid() {
        AllStatusIfBoolean ifBoolean = (Table table) -> !table.getBill().getPayStatus();
        return allStatus(ifBoolean);
    }
}
