package ui.console.manager;

import model.Restaurant;
import model.Table;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.exceptions.StopRedoException;

import java.text.DecimalFormat;
import java.util.*;

import static ui.console.manager.RestaurantManagerFunctions.*;
import static ui.console.manager.RestaurantManagerPrintAndAllStatusFunctions.*;

// Restaurant Manager Application: Code structure is loosely based on the project JsonSerializationDemo
public class RestaurantManager {

    protected static final DecimalFormat DF = new DecimalFormat("0.00");

    protected static Restaurant restaurant;
    protected static Scanner input;
    protected static Scanner inputLoad;
    protected static String currentTableName;
    protected static JsonWriter jsonWriter;
    protected static JsonReader jsonReader;
    protected static String jsonStore;
    protected static boolean newRestaurant = false;

    // EFFECTS: runs the restaurant application
    public RestaurantManager() {
        loadRestaurant();
    }

    // MODIFIES: this
    // EFFECTS: processes user input for load menu
    private void loadRestaurant() {
        boolean keepGoing = true;
        String command;
        inputLoad = new Scanner(System.in);
        inputLoad.useDelimiter("\n");
        while (keepGoing) {
            displayLoadMenu();
            command = inputLoad.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processLoadCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // EFFECTS: displays load menu to user
    private void displayLoadMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tn -> new restaurant");
        System.out.println("\tl -> load restaurant");
        System.out.println("\tq -> quit");

        System.out.print("Enter: ");
    }

    // MODIFIES: this, Restaurant
    // EFFECTS: processes user command for load menu
    private void processLoadCommand(String command) {
        switch (command) {
            case "n":
                init();
                System.out.print("Enter New Restaurant Name: ");
                String name = input.next();
                restaurant = new Restaurant(name);
                newRestaurant = true;
                runRestaurant();
                break;
            case "l":
                loadStoreRestaurant();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for loading saved restaurants
    private void loadStoreRestaurant() {
        boolean keepGoing = true;
        String command;
        while (keepGoing) {
            displayLoadStoreMenu();
            command = inputLoad.next();
            command = command.toLowerCase();

            try {
                processLoadStoreCommand(command);
            } catch (StopRedoException e) {
                keepGoing = false;
            }
        }
    }

    // EFFECTS: displays save slots to user
    private static void displayLoadStoreMenu() {
        System.out.println("\n\nSelect from:");
        printStores();
        System.out.print("Enter: ");
    }

    // MODIFIES: this, Restaurant
    // EFFECTS: processes user command for loading saved restaurants
    private void processLoadStoreCommand(String command) throws StopRedoException {
        boolean runRestaurant = true;
        switch (command) {
            case "1":
                jsonStore = "./data/json/store1.json";
                break;
            case "2":
                jsonStore = "./data/json/store2.json";
                break;
            case "3":
                jsonStore = "./data/json/store3.json";
                break;
            case "4":
                jsonStore = "./data/json/store4.json";
                break;
            case "5":
                jsonStore = "./data/json/store5.json";
                break;
            default:
                System.out.println("Selection not valid...");
                runRestaurant = false;
                break;
        }
        load(runRestaurant);
    }

    // MODIFIES: this
    // EFFECTS: processes user input for main menu
    protected static void runRestaurant() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else if (command.equals("i")) {
                displayInfo();
                input.next();
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nQuitting program...");
    }

    // EFFECTS: Displays Info to user
    private static void displayInfo() {
        System.out.println(
                  "\nWelcome to Restaurant Manager, here is some basic information to get started:"
                + "\nA menu of possible actions will be displayed,"
                + "\nYou can trigger those actions by typing the letter assigned to the action"

                + "\n\nMost actions require other actions to be completed first"
                + "\nTable actions cannot be done without first adding a table"
                + "\nCannot assign customers to invalid tables (not clean, not set, and/or already occupied)"
                + "\nCertain actions (like cleaning) require the table to have the opposite effect done first"

                + "\n\nComplete list of prerequisite actions before action can be called:"
                + "\n\tAdd Table: Nothing"
                + "\n\tCheck Earnings: Nothing"
                + "\n\tInfo: Nothing"
                + "\n\tQuit/Return/Cancel: Nothing"
                + "\n\tRemove Table: Add Table"
                + "\n\tClean: Add Table, Mark Unoccupied"
                + "\n\tSet: Add Table, Mark Unoccupied"
                + "\n\tMark Unoccupied: Assign Customers"
                + "\n\tAssign Customers: Mark Unoccupied"
                + "\n\tOrder Food: Assign Customers"
                + "\n\tDeliver Food: Order Food"
                + "\n\tPay For Food: Assign Customers"
                + "\n\tCheck History (purchase, cleaning, or delivery history): Add Table");

        System.out.print("\n\nPress Enter To Continue:");
    }

    // MODIFIES: this, Restaurant
    // EFFECTS: processes user command for main menu
    private static void processCommand(String command) {
        switch (command) {
            case "a":
                doAddTable();
                break;
            case "r":
                doRemoveTable();
                break;
            case "t":
                doTableCommands();
                break;
            case "e":
                doShowEarnings();
                break;
            case "c":
                doAssignCustomer();
                break;
            case "s":
                saveRestaurant();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes restaurant manager
    protected static void init() {
        currentTableName = null;
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // EFFECTS: displays main menu options to user
    private static void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add table");
        System.out.println("\tr -> remove table");
        System.out.println("\tt -> table management");
        System.out.println("\te -> check earnings");
        System.out.println("\tc -> assign customers");
        System.out.println("\ti -> info");
        System.out.println("\ts -> save restaurant");
        System.out.println("\tq -> quit");

        System.out.print("Enter: ");
    }

    // MODIFIES: this
    // EFFECTS: displays table display menu and processes table commands
    private static void doTableCommands() {
        if (new ArrayList<Table>().equals(restaurant.getTables())) {
            System.out.println("No tables to manage...");
        } else {
            boolean keepGoing = true;
            String command;

            while (keepGoing) {
                displayTableMenu();
                command = input.next().toLowerCase();

                if (command.equals("r")) {
                    keepGoing = false;
                } else {
                    processTableCommand(command);
                }
            }

            System.out.println("\nRetuning to main menu...");
        }
    }

    // EFFECTS: displays menu of table options to user
    private static void displayTableMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tc -> clean");
        System.out.println("\ts -> set");
        System.out.println("\ta -> availability");
        System.out.println("\tf -> food");
        System.out.println("\tb -> bill");
        System.out.println("\tr -> return back");

        System.out.print("Enter: ");
    }

    // MODIFIES: this
    // EFFECTS: processes user commands for table menu
    private static void processTableCommand(String command) {
        switch (command) {
            case "c":
                cleanTables();
                break;
            case "s":
                setTables();
                break;
            case "a":
                availabilityTables();
                break;
            case "f":
                foodTables();
                break;
            case "b":
                billTables();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // MODIFIES: this
    // processes user input for cleaning menu
    private static void cleanTables() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayCleanMenu();
            command = input.next().toLowerCase();

            if (command.equals("r")) {
                keepGoing = false;
            } else {
                processCleanCommand(command);
            }
        }

        System.out.println("\nRetuning to table menu...");
    }

    // EFFECTS: displays cleaning menu of options to user
    private static void displayCleanMenu() {
        printCleanTables();

        System.out.println("\nSelect from:");
        System.out.println("\tc -> clean table");
        System.out.println("\th -> history");
        System.out.println("\tr -> return back");

        System.out.print("Enter: ");
    }

    // MODIFIES: this
    // EFFECTS: processes user commands for cleaning menu
    private static void processCleanCommand(String command) {
        switch (command) {
            case "c":
                cleanTable();
                break;
            case "h":
                cleaningHistoryTable();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for food menu
    private static void foodTables() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayFoodMenu();
            command = input.next().toLowerCase();

            if (command.equals("r")) {
                keepGoing = false;
            } else {
                processFoodCommand(command);
            }
        }

        System.out.println("\nRetuning to table menu...");
    }

    // EFFECTS: displays food menu to user
    private static void displayFoodMenu() {
        printAvailabilityTables();
        System.out.println("\nSelect from:");
        System.out.println("\to -> order food");
        System.out.println("\td -> deliver food");
        System.out.println("\th -> history");
        System.out.println("\tr -> return back");

        System.out.print("Enter: ");
    }

    // MODIFIES: this
    // EFFECTS: processes user commands for food menu
    private static void processFoodCommand(String command) {
        switch (command) {
            case "o":
                orderFoodTable();
                break;
            case "d":
                deliverFoodTables();
                break;
            case "h":
                deliveryHistoryTable();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for bill menu
    private static void billTables() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayBillMenu();
            command = input.next().toLowerCase();

            if (command.equals("r")) {
                keepGoing = false;
            } else {
                processBillCommand(command);
            }
        }

        System.out.println("\nRetuning to table menu...");
    }

    // EFFECTS: displays bill menu to user
    private static void displayBillMenu() {
        printBillTables();

        System.out.println("\nSelect from:");
        System.out.println("\tp -> pay bills");
        System.out.println("\th -> history");
        System.out.println("\tr -> return back");

        System.out.print("Enter: ");
    }

    // MODIFIES: this
    // EFFECTS: processes user command for bill menu
    private static void processBillCommand(String command) {
        switch (command) {
            case "p":
                billTable();
                break;
            case "h":
                purchaseHistoryTable();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // EFFECTS: if newRestaurant is true, saves a new restaurant otherwise saves restaurant
    private static void saveRestaurant() {
        if (newRestaurant) {
            saveNewRestaurant();
        } else {
            try {
                save(true);
            } catch (StopRedoException e) {
                // do nothing
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for saving a new restaurant
    private static void saveNewRestaurant() {
        boolean keepGoing = true;
        String command;
        while (keepGoing) {
            displaySaveMenu();
            command = input.next();
            command = command.toLowerCase();

            try {
                processSaveCommand(command);
            } catch (StopRedoException e) {
                keepGoing = false;
            }
        }
    }

    // EFFECTS: displays save menu to user
    private static void displaySaveMenu() {
        System.out.println("\n\nChoose A Slot To Save To:");
        printStores();
        System.out.print("Enter: ");
    }

    // MODIFIES: this
    // EFFECTS: processes user command for saving restaurant
    private static void processSaveCommand(String command) throws StopRedoException {
        boolean saveRestaurant = true;
        switch (command) {
            case "1":
                jsonStore = "./data/json/store1.json";
                break;
            case "2":
                jsonStore = "./data/json/store2.json";
                break;
            case "3":
                jsonStore = "./data/json/store3.json";
                break;
            case "4":
                jsonStore = "./data/json/store4.json";
                break;
            case "5":
                jsonStore = "./data/json/store5.json";
                break;
            default:
                System.out.println("Selection not valid...");
                saveRestaurant = false;
                break;
        }
        save(saveRestaurant);
    }


}
