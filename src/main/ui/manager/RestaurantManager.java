package ui.manager;

import model.Restaurant;
import model.Table;

import java.text.DecimalFormat;
import java.util.*;

import static ui.manager.RestaurantManagerFunctions.*;
import static ui.manager.RestaurantManagerPrintAndAllStatusFunctions.*;

public class RestaurantManager {

    protected static final DecimalFormat DF = new DecimalFormat("0.00");

    protected static Restaurant restaurant;
    protected static Scanner input;
    protected static String currentTableName;

    // EFFECTS: runs the restaurant application
    public RestaurantManager() {
        runRestaurant();
    }

    // MODIFIES: this
    // EFFECTS: processes user input for main menu
    private void runRestaurant() {
        boolean keepGoing = true;
        String command;
        init();
        displayInfo();
        input.nextLine();

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

        System.out.println("\nGoodbye!");
    }

    // EFFECTS: Displays Info to user
    private void displayInfo() {
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
    private void processCommand(String command) {
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
            default:
                System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes restaurant
    private void init() {
        restaurant = new Restaurant();
        currentTableName = null;
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // EFFECTS: displays main menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add table");
        System.out.println("\tr -> remove table");
        System.out.println("\tt -> table management");
        System.out.println("\te -> check earnings");
        System.out.println("\tc -> assign customers");
        System.out.println("\ti -> info");
        System.out.println("\tq -> quit");

        System.out.print("Enter: ");
    }

    // MODIFIES: this
    // EFFECTS: displays table display menu and processes table commands
    private void doTableCommands() {
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
    private void displayTableMenu() {
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
    private void processTableCommand(String command) {
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
        }
    }

    // MODIFIES: this
    // processes user input for cleaning menu
    private void cleanTables() {
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
    private void displayCleanMenu() {
        printCleanTables();

        System.out.println("\nSelect from:");
        System.out.println("\tc -> clean table");
        System.out.println("\th -> history");
        System.out.println("\tr -> return back");

        System.out.print("Enter: ");
    }

    private void processCleanCommand(String command) {
        switch (command) {
            case "c":
                cleanTable();
                break;
            case "h":
                cleaningHistoryTable();
                break;
            default:
                System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for food menu
    private void foodTables() {
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
    private void displayFoodMenu() {
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
    private void processFoodCommand(String command) {
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
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for bill menu
    private void billTables() {
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
    private void displayBillMenu() {
        printBillTables();

        System.out.println("\nSelect from:");
        System.out.println("\tp -> pay bills");
        System.out.println("\th -> history");
        System.out.println("\tr -> return back");

        System.out.print("Enter: ");
    }

    // MODIFIES: this
    // EFFECTS: processes user command for bill menu
    private void processBillCommand(String command) {
        switch (command) {
            case "p":
                billTable();
                break;
            case "h":
                purchaseHistoryTable();
                break;
            default:
                System.out.println("Selection not valid...");
        }
    }

}
