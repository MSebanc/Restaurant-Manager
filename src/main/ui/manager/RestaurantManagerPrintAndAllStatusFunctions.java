package ui.manager;

import model.Food;
import model.Restaurant;
import model.Table;
import persistence.JsonReader;
import ui.interfaces.AllStatusIfBoolean;
import ui.interfaces.TableToStringFunction;

import java.io.FileNotFoundException;
import java.io.IOException;

public class RestaurantManagerPrintAndAllStatusFunctions extends RestaurantManagerFunctions {

    public RestaurantManagerPrintAndAllStatusFunctions() throws FileNotFoundException {
    }

    // EFFECTS: prints all tables name and max occupancy
    protected static void printTablesMax() {
        TableToStringFunction printStatement = (Table table) -> ", Max Occupancy: " + table.getMaxOccupancy();
        printTableFunction(printStatement);
    }

    protected static void printValidTables() {
        TableToStringFunction printStatement = (Table table) -> ", Max Occupancy: " + table.getMaxOccupancy()
                + ", Assignable Status: " + restaurant.validStatuses(table);
        printTableFunction(printStatement);
    }

    protected static void printCleanTables() {
        TableToStringFunction printStatement = (Table table) -> ", Clean Status: " + table.getCleanStatus();
        printTableFunction(printStatement);
    }

    protected static void printSetTables() {
        TableToStringFunction printStatement = (Table table) -> ", Set Status: " + table.getSetStatus();
        printTableFunction(printStatement);
    }

    protected static void printAvailabilityTables() {
        TableToStringFunction printStatement = (Table table) -> ", Availability Status: "
                + table.getAvailabilityStatus();
        printTableFunction(printStatement);
    }

    protected static void printOrderTables() {
        TableToStringFunction printStatement = (Table table) -> ", Can Order Food: "
                + !table.getAvailabilityStatus();
        printTableFunction(printStatement);
    }

    protected static void printDeliveryTables() {
        TableToStringFunction printStatement = (Table table) -> ", Delivery Status: " + table.getFoodDeliveryStatus();
        printTableFunction(printStatement);
    }

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

    protected static void printTables() {
        TableToStringFunction printStatement = (Table table) -> "";
        printTableFunction(printStatement);
    }

    protected static void printFood() {
        System.out.println("\nFood That Can Be Ordered: ");
        for (Food food : restaurant.getMenu()) {
            System.out.println("Food Name: " + food.getName() + ", Price: $" + DF.format(food.getPrice()));
        }
    }

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

    protected static boolean allClean() {
        AllStatusIfBoolean ifBoolean = (Table table) -> !table.getCleanStatus();
        return allStatus(ifBoolean);
    }

    protected static boolean allSet() {
        AllStatusIfBoolean ifBoolean = (Table table) -> !table.getSetStatus();
        return allStatus(ifBoolean);
    }

    protected static boolean allEmpty() {
        AllStatusIfBoolean ifBoolean = (Table table) -> !table.getAvailabilityStatus();
        return allStatus(ifBoolean);
    }

    protected static boolean allNotHungry() {
        AllStatusIfBoolean ifBoolean = (Table table) -> !table.getFoodDeliveryStatus();
        return allStatus(ifBoolean);
    }

    protected static boolean allPaid() {
        AllStatusIfBoolean ifBoolean = (Table table) -> !table.getBill().getPayStatus();
        return allStatus(ifBoolean);
    }
}
