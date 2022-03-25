package ui.console.manager;

import model.Food;
import model.Table;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.exceptions.InvalidNameEnteredException;
import ui.exceptions.InvalidNumberException;
import ui.exceptions.StopRedoException;
import ui.exceptions.TableForLoopBodyException;
import ui.console.interfaces.*;

import java.io.IOException;

import static ui.console.manager.RestaurantManagerPrintAndAllStatusFunctions.*;

// Restaurant Manager Application (sub functions): Code structure is loosely based on the project JsonSerializationDemo
public class RestaurantManagerFunctions extends RestaurantManagerAbstractFunctions {

    // EFFECTS: Constructor
    public RestaurantManagerFunctions() {
        // does nothing
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: if runRestaurant is true, load restaurant from file and run restaurant
    protected static void load(boolean runRestaurant) throws StopRedoException {
        if (runRestaurant) {
            jsonReader = new JsonReader(jsonStore);
            jsonWriter = new JsonWriter(jsonStore);
            try {
                restaurant = jsonReader.read();
                System.out.println("Loaded " + restaurant.getName() + " from " + jsonStore);
                init();
                runRestaurant();
                throw new StopRedoException();
            } catch (IOException e) {
                System.out.println("Unable to read from file: " + jsonStore);
            }
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: if saveRestaurant is true, save restaurant to file
    protected static void save(boolean saveRestaurant) throws StopRedoException {
        if (saveRestaurant) {
            jsonReader = new JsonReader(jsonStore);
            jsonWriter = new JsonWriter(jsonStore);
            try {
                jsonWriter.open();
                jsonWriter.write(restaurant);
                jsonWriter.close();
                System.out.println("Saved " + restaurant.getName() + " to " + jsonStore);

                newRestaurant = false;
                System.out.print("\nPress Enter To Go To Main Menu:");
                input.next();
                throw new StopRedoException();
            } catch (IOException e) {
                System.out.println("Unable to write from file: " + jsonStore);
            }
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: adds table to restaurant
    protected static void doAddTable() {
        PrintFunction printTable = RestaurantManagerPrintAndAllStatusFunctions::printTablesMax;
        StopRedo stopRedo = () -> {

        };
        TableActionsCommands commandFunction = () -> {
            if (restaurant.getTables().isEmpty()) {
                nameToAddWhenEmpty();
            } else {
                nameToAdd();
            }
            int max = selectTableOccupancy();

            restaurant.addTable(max, currentTableName);
            System.out.println("\nTable named " + currentTableName + " with a max occupancy of "
                    + max + " has been added!");
        };

        abstractTableCommand(printTable, commandFunction,
                "add another table?", "main menu", stopRedo);
        currentTableName = null;
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: adds table to restaurant
    private static void nameToAddWhenEmpty() {
        try {
            System.out.print("\nEnter New Table Name: ");
            String selection = input.next();
            if (selection.equalsIgnoreCase("c")) {
                throw new InvalidNameEnteredException();
            }
            currentTableName = selection;
        } catch (InvalidNameEnteredException e) {
            System.out.println("\nInvalid name, please try again");
            nameToAddWhenEmpty();
        }
    }

    // EFFECTS: calls abstractTableFunction for adding a new table to restaurant
    private static void nameToAdd() {
        PrintFunction printTable = () -> System.out.print("");
        String enterStatement = "Enter New Table Name: ";
        TableForLoops forLoop = RestaurantManagerFunctions::nameToAddForLoop;
        String invalidInputStatement = "name";
        String redoQuestion = "";
        String menuName = "";
        StopRedo stopRedo = () -> {
            throw new StopRedoException();
        };

        printTable.run();

        abstractTableFunction(printTable, enterStatement, forLoop,
                invalidInputStatement, redoQuestion, menuName, stopRedo);
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: currentTableName is set to selection and for list of tables, if table name is equal to selection,
    // throws TableForLoopBodyException
    private static void nameToAddForLoop(String selection) throws TableForLoopBodyException {
        for (Table table : restaurant.getTables()) {
            if (selection.equalsIgnoreCase(table.getName())) {
                throw new TableForLoopBodyException();
            }
        }
        currentTableName = selection;
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: processes user input for an integer for max occupancy and returns it
    private static int selectTableOccupancy() {
        String selection;
        System.out.print("Enter Positive Number for Table Max Occupancy: ");
        try {
            selection = input.next();
            int selectionInt = Integer.parseInt(selection);
            if (selectionInt <= 0) {
                System.out.println("Selection not valid...\n");
                return selectTableOccupancy();
            } else {
                return selectionInt;
            }
        } catch (Exception e) {
            System.out.println("Selection not valid...\n");
            return selectTableOccupancy();
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: removes table from restaurant
    protected static void doRemoveTable() {
        PrintFunction printTable = RestaurantManagerPrintAndAllStatusFunctions::printTablesMax;
        StopRedo stopRedo = () -> {
            if (restaurant.getTables().isEmpty()) {
                System.out.println("\nNo tables to remove, bringing you back to the main menu...");
                System.out.print("Press Enter To Continue:");
                input.next();
                throw new StopRedoException();
            }
        };

        TableActionsCommands commandFunction = () -> {
            nameToRemove();

            restaurant.removeTable(currentTableName);
            System.out.println("Removed: " + currentTableName);
            stopRedo.run();
        };


        if (restaurant.getTables().isEmpty()) {
            System.out.println("No tables to remove...");
        } else {
            abstractTableCommand(printTable, commandFunction,
                    "remove another table?", "main menu", stopRedo);
        }
        currentTableName = null;

    }

    // EFFECTS: calls abstractTableFunction for removing a new table to restaurant
    private static void nameToRemove() {
        PrintFunction printTable = () -> System.out.print("");
        String enterStatement = "Enter Name of Table to Remove: ";
        TableForLoops forLoop = RestaurantManagerFunctions::nameToRemoveForLoop;
        String invalidInputStatement = "name";
        String redoQuestion = "";
        String menuName = "";
        StopRedo stopRedo = () -> {
            if (currentTableName != null) {
                throw new StopRedoException();
            }
        };

        printTable.run();

        abstractTableFunction(printTable, enterStatement, forLoop,
                invalidInputStatement, redoQuestion, menuName, stopRedo);
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: throws TableForLoopBodyException if all table names do not equal selection, otherwise sets
    // currentTableName to table name
    private static void nameToRemoveForLoop(String selection) throws TableForLoopBodyException {
        boolean forLoopException = true;
        for (Table table : restaurant.getTables()) {
            if (selection.equalsIgnoreCase(table.getName())) {
                currentTableName = table.getName();
                forLoopException = false;
            }
        }
        if (forLoopException) {
            throw new TableForLoopBodyException();
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: shows amount the restaurant has earned and the amount of tips earned
    protected static void doShowEarnings() {
        System.out.println("\nThe restaurant has made $" + DF.format(restaurant.getEarnings()) + "!");
        System.out.println("There has also been $" + DF.format(restaurant.getTips()) + " in tips!");
        System.out.println("\nThere has been a total of " + restaurant.getTotalCustomers() + " customers!");

        System.out.print("\nPress Enter to Continue:");
        input.next();
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: processes user input for assigning party to table
    protected static void doAssignCustomer() {
        if (notAvailable()) {
            System.out.println("No available tables or tables not ready to be occupied...");
        } else {
            printValidTables();

            System.out.print("\nEnter Party Size: ");
            String selection;
            int selectionInt;
            selection = input.next();

            try {
                selectionInt = Integer.parseInt(selection);
                Table table = restaurant.assignCustomers(restaurant.validTables(selectionInt), selectionInt);
                table.occupyTable();
                System.out.println("\nAssigned party to: " + table.getName());
            } catch (Exception e) {
                System.out.println("Party size entered is not valid...\n");
                doAssignCustomer();
            }
        }
    }

    // EFFECTS: checks availability of all tables and returns false if one is available
    private static boolean notAvailable() {
        boolean notAvailable = true;
        for (Table table : restaurant.getTables()) {
            if (restaurant.validStatuses(table)) {
                notAvailable = false;
                break;
            }
        }
        return notAvailable;
    }

    // EFFECTS: calls abstractTableFunction for cleaning a table
    protected static void cleanTable() {
        PrintFunction printTable = RestaurantManagerPrintAndAllStatusFunctions::printCleanTables;
        String enterStatement = "\nEnter Table Name To Clean: ";
        TableForLoops forLoop = RestaurantManagerFunctions::cleanTableForLoop;
        String invalidInputStatement = "name";
        String redoQuestion = "clean another table?";
        String menuName = "cleaning menu";
        StopRedo stopRedo = () -> {
            if (allClean()) {
                System.out.println("No more tables to clean, sending you back to the cleaning menu...");
                System.out.print("Press Enter To Continue:");
                input.next();
                throw new StopRedoException();
            }
        };

        if (allClean()) {
            System.out.println("No tables to clean...");
        } else {
            printTable.run();
            abstractTableFunction(printTable, enterStatement, forLoop,
                    invalidInputStatement, redoQuestion, menuName, stopRedo);
        }

    }

    // MODIFIES: RestaurantManager
    // EFFECTS: throws TableForLoopBodyException if all table names do not equal selection or all tables are clean,
    // otherwise cleans table
    private static void cleanTableForLoop(String selection) throws TableForLoopBodyException {
        boolean forLoopException = true;
        for (Table table : restaurant.getTables()) {
            if (selection.equalsIgnoreCase(table.getName()) && !table.getCleanStatus()) {
                table.cleanTable();
                System.out.println("\nCleaned " + table.getName() + "!");
                forLoopException = false;
            }
        }
        if (forLoopException) {
            throw new TableForLoopBodyException();
        }
    }

    // EFFECTS: calls abstractHistoryFunction for cleaningHistory
    protected static void cleaningHistoryTable() {
        String historyName = "Cleaning";
        PrintHistoryFunction printHistory = (Table table, String hName) -> {
            GetHistoryFunction getHistory = table::getCleaningHistory;
            printHistoryDates(getHistory, hName);
        };
        String redoQuestion = "check the cleaning history of another table?";
        String menuName = "cleaning menu ";

        printTables();
        abstractHistoryFunction(historyName, printHistory, redoQuestion, menuName);
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: calls abstractTableFunction for setting a table
    protected static void setTables() {
        PrintFunction printTable = RestaurantManagerPrintAndAllStatusFunctions::printSetTables;
        String enterStatement = "Enter Table Name To Set: ";
        TableForLoops forLoop = RestaurantManagerFunctions::setTableForLoop;
        String invalidInputStatement = "name";
        String redoQuestion = "set another table?";
        String menuName = "table menu";
        StopRedo stopRedo = () -> {
            if (allSet()) {
                System.out.println("No more tables to set, sending you back to the table menu...");
                System.out.print("Press Enter To Continue:");
                input.next();
                throw new StopRedoException();
            }
        };

        if (allSet()) {
            System.out.println("No tables to set...");
        } else {
            printTable.run();
            abstractTableFunction(printTable, enterStatement, forLoop,
                    invalidInputStatement, redoQuestion, menuName, stopRedo);
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: throws TableForLoopBodyException if all table names do not equal selection or all tables are set,
    // otherwise sets table
    private static void setTableForLoop(String selection) throws TableForLoopBodyException {
        boolean forLoopException = true;
        for (Table table : restaurant.getTables()) {
            if (selection.equalsIgnoreCase(table.getName()) && !table.getSetStatus()) {
                table.trueSetTable();
                System.out.println("\nSet " + table.getName() + "!");
                forLoopException = false;
            }
        }
        if (forLoopException) {
            throw new TableForLoopBodyException();
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: calls abstractTableFunction for setting a table as unoccupied
    protected static void availabilityTables() {
        PrintFunction printTable = RestaurantManagerPrintAndAllStatusFunctions::printAvailabilityTables;
        String enterStatement = "Enter Table Name To Mark As Unoccupied: ";
        TableForLoops forLoop = RestaurantManagerFunctions::availabilityTableForLoop;
        String invalidInputStatement = "name or table is already";
        String redoQuestion = "empty another table?";
        String menuName = "table menu";
        StopRedo stopRedo = () -> {
            if (allEmpty()) {
                System.out.println("No more tables to empty, sending you back to the table menu...");
                System.out.print("Press Enter To Continue:");
                input.next();
                throw new StopRedoException();
            }
        };

        if (allEmpty()) {
            System.out.println("No tables to empty...");
        } else {
            printTable.run();
            abstractTableFunction(printTable, enterStatement, forLoop,
                    invalidInputStatement, redoQuestion, menuName, stopRedo);
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: throws TableForLoopBodyException if all table names do not equal selection or all tables are available,
    // otherwise empties table
    private static void availabilityTableForLoop(String selection) throws TableForLoopBodyException {
        boolean forLoopException = true;
        for (Table table : restaurant.getTables()) {
            if (selection.equalsIgnoreCase(table.getName()) && !table.getAvailabilityStatus()) {
                table.emptyTable();
                System.out.println("\nEmptied " + table.getName() + "!");
                forLoopException = false;
            }
        }
        if (forLoopException) {
            throw new TableForLoopBodyException();
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: calls abstractTableFunction for ordering food for a table
    protected static void orderFoodTable() {
        PrintFunction printTable = RestaurantManagerPrintAndAllStatusFunctions::printOrderTables;
        String enterStatement = "\nEnter Table Name To Order Food: ";
        TableForLoops forLoop = RestaurantManagerFunctions::orderFoodTableForLoop;
        String invalidInputStatement = "name";
        String redoQuestion = "order food for another table?";
        String menuName = "food menu";
        StopRedo stopRedo = () -> {

        };

        if (allEmpty()) {
            System.out.println("No tables to order food for...");
        } else {
            printTable.run();
            abstractTableFunction(printTable, enterStatement, forLoop,
                    invalidInputStatement, redoQuestion, menuName, stopRedo);
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: throws TableForLoopBodyException if all table names do not equal selection or all tables are available,
    // otherwise orders food for table
    private static void orderFoodTableForLoop(String selection) throws TableForLoopBodyException {
        boolean forLoopException = true;
        for (Table table : restaurant.getTables()) {
            if (selection.equalsIgnoreCase(table.getName()) && !table.getAvailabilityStatus()) {
                order(table.getName());
                System.out.println("\n\nOrdered food for " + table.getName() + "!");
                forLoopException = false;
            }
        }
        if (forLoopException) {
            throw new TableForLoopBodyException();
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: calls abstractTableFunction for ordering food for a given table
    private static void order(String tableName) {
        PrintFunction printTable = RestaurantManagerPrintAndAllStatusFunctions::printFood;
        String enterStatement = "\nEnter Food Name: ";
        TableForLoops forLoop = (String selection) -> foodForLoop(selection, tableName);
        String invalidInputStatement = "name";
        String redoQuestion = "order more food for table?";
        String menuName = "ordering food for tables selection";
        StopRedo stopRedo = () -> {

        };

        printTable.run();
        abstractTableFunction(printTable, enterStatement, forLoop,
                invalidInputStatement, redoQuestion, menuName, stopRedo);
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: throws TableForLoopBodyException if all food names do not equal selection, otherwise sets table
    private static void foodForLoop(String selection, String tableName) throws TableForLoopBodyException {
        boolean forLoopException = true;
        for (Food food : restaurant.getMenu()) {
            if (selection.equalsIgnoreCase(food.getName())) {
                restaurant.orderFood(tableName, food.getName());
                System.out.println("Ordered " + food.getName() + " for " + tableName + "!");
                forLoopException = false;
            }
        }
        if (forLoopException) {
            throw new TableForLoopBodyException();
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: calls abstractTableFunction for delivery food to a table
    protected static void deliverFoodTables() {
        PrintFunction printTable = RestaurantManagerPrintAndAllStatusFunctions::printDeliveryTables;
        String enterStatement = "Enter Table Name To Deliver Food For: ";
        TableForLoops forLoop = RestaurantManagerFunctions::deliverTableForLoop;
        String invalidInputStatement = "name";
        String redoQuestion = "deliver food to another table?";
        String menuName = "food menu";
        StopRedo stopRedo = () -> {
            if (allNotHungry()) {
                System.out.println("No more tables to deliver food to, sending you back to the food menu...");
                System.out.print("Press Enter To Continue:");
                input.next();
                throw new StopRedoException();
            }
        };

        if (allNotHungry()) {
            System.out.println("No tables to deliver food to...");
        } else {
            printTable.run();
            abstractTableFunction(printTable, enterStatement, forLoop,
                    invalidInputStatement, redoQuestion, menuName, stopRedo);
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: throws TableForLoopBodyException if all table names do not equal selection or all tables are set,
    // otherwise sets table
    private static void deliverTableForLoop(String selection) throws TableForLoopBodyException {
        boolean forLoopException = true;
        for (Table table : restaurant.getTables()) {
            if (selection.equalsIgnoreCase(table.getName()) && !table.getFoodDeliveryStatus()) {
                table.deliverFood();
                System.out.println("\nDelivered food to " + table.getFoodDeliveryStatus() + "!");
                forLoopException = false;
            }
        }
        if (forLoopException) {
            throw new TableForLoopBodyException();
        }
    }

    // EFFECTS: calls abstractHistoryFunction for deliveryHistory
    protected static void deliveryHistoryTable() {
        String historyName = "Delivery";
        PrintHistoryFunction printHistory = (Table table, String hName) -> {
            GetHistoryFunction getHistory = table::getDeliveryHistory;
            printHistoryDates(getHistory, hName);
        };
        String redoQuestion = "check the delivery history of another table?";
        String menuName = "food menu";

        printTables();
        abstractHistoryFunction(historyName, printHistory, redoQuestion, menuName);
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: calls abstractTableFunction for billing a table
    protected static void billTable() {
        PrintFunction printTable = RestaurantManagerPrintAndAllStatusFunctions::printBillTables;
        String enterStatement = "Enter Table Name To Pay Bill For: ";
        TableForLoops forLoop = RestaurantManagerFunctions::billTableForLoop;
        String invalidInputStatement = "name";
        String redoQuestion = "bill another table?";
        String menuName = "billing menu";
        StopRedo stopRedo = () -> {
            if (allPaid()) {
                System.out.println("No more tables to bill, sending you back to the billing menu...");
                System.out.print("Press Enter To Continue:");
                input.next();
                throw new StopRedoException();
            }
        };

        if (allPaid()) {
            System.out.println("No tables to bill...");
        } else {
            printTable.run();
            abstractTableFunction(printTable, enterStatement, forLoop,
                    invalidInputStatement, redoQuestion, menuName, stopRedo);
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: throws TableForLoopBodyException if all table names do not equal selection or all bills are paid,
    // otherwise bills table
    private static void billTableForLoop(String selection) throws TableForLoopBodyException {
        boolean forLoopException = true;
        for (Table table : restaurant.getTables()) {
            if (selection.equalsIgnoreCase(table.getName()) && !table.getBill().getPayStatus()) {
                payBill(table);
                System.out.println("\nPaid Bill for " + table.getName() + "!");
                forLoopException = false;
            }
        }
        if (forLoopException) {
            throw new TableForLoopBodyException();
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: processes user input and pays for table
    private static void payBill(Table table) {
        System.out.println("\nCost: $" + DF.format(table.getBill().getCost()));
        tips(table);
        restaurant.tablePay(table.getName());
        System.out.println("\nTotal Cost: $" + DF.format(table.getBill().getTotalCost()));
        System.out.print("\nPress Enter To Continue:");
        input.next();
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: processes user input for tip amount and sets bill's tip to given amount
    private static void tips(Table table) {
        System.out.print("\nEnter Tip Amount: $");
        String selection = input.next();
        try {
            double selectionDouble = Double.parseDouble(selection);
            if (selectionDouble < 0) {
                throw new InvalidNumberException();
            }
            table.getBill().setTip(selectionDouble);
            System.out.println("\n$" + DF.format(selectionDouble) + " adding in tips!");

        } catch (Exception exception) {
            System.out.println("Selection not valid...\n");
            tips(table);
        }
    }

    // EFFECTS: calls abstractHistoryFunction for cleaningHistory
    protected static void purchaseHistoryTable() {
        String historyName = "Purchase";
        PrintHistoryFunction printHistory = (Table table, String hName) -> {
            GetHistoryFunction getHistory = table::getPurchaseHistory;
            printHistoryDates(getHistory, hName);
        };
        String redoQuestion = "check the purchase history of another table?";
        String menuName = "billing menu";

        printTables();
        abstractHistoryFunction(historyName, printHistory, redoQuestion, menuName);
    }
}
