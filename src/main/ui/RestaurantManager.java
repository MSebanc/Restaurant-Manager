package ui;

import model.Food;
import model.Restaurant;
import model.Table;

import java.text.DecimalFormat;
import java.util.*;

// Restaurant Manager Application: Code structure is based on the project TellerApp
public class RestaurantManager {

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    private Restaurant restaurant;
    private Scanner input;

    // EFFECTS: runs the teller application
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

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else if (command.equals("i")) {
                displayInfo();
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // EFFECTS: Displays Info to user
    private void displayInfo() {
        System.out.println("Welcome to Restaurant Manager, here is some basic information to get started:"
                + "\nA menu of possible actions will be displayed,"
                + "\nYou can trigger those actions by typing the letter assigned to the action"

                + "\n\nMost actions require other actions to be completed first"
                + "\nTable actions cannot be done without first adding a table"
                + "\nCannot assign customers to invalid tables (not clean, not set, and/or already occupied)"
                + "\nCertain actions (like cleaning) require the table to have the opposite effect done first"

                + "\n\nComplete list of prerequisite actions before action can be called:"
                + "\n\tAdd Table: Nothing"
                + "\n\tCheck Profit: Nothing"
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
        input.next();
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
            case "p":
                doShowProfit();
                break;
            case "c":
                if (notAvailable()) {
                    System.out.println("No available tables or tables not ready to be occupied...");
                } else {
                    doAssignCustomer();
                }
                break;
            default:
                System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes restaurant
    private void init() {
        restaurant = new Restaurant();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // EFFECTS: displays main menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add table");
        System.out.println("\tr -> remove table");
        System.out.println("\tt -> table management");
        System.out.println("\tp -> check profit");
        System.out.println("\tc -> assign customers");
        System.out.println("\ti -> info");
        System.out.println("\tq -> quit");

        System.out.print("Enter: ");
    }

    // MODIFIES: this, Restaurant
    // EFFECTS: adds table to restaurant
    private void doAddTable() {
        printTables();

        String name = nameToAdd();
        int max = selectTableOccupancy();

        restaurant.addTable(max, name);
        System.out.println("\nTable named " + name + " with a max occupancy of " + max + " has been added!");

        reAddTable();
    }

    // MODIFIES: this, Restaurant
    // processes user command for if the user wants to add another table
    private void reAddTable() {
        System.out.println("\nWould you like to add another table?");
        System.out.println("\ty -> yes");
        System.out.println("\tn -> no");

        System.out.print("Enter: ");
        String command;
        command = input.next();

        reAddTableSwitch(command);
    }

    // MODIFIES: this, Restaurant
    // EFFECTS: processes user commands for if the user wants to add another table
    private void reAddTableSwitch(String command) {
        switch (command) {
            case "y":
                doAddTable();
                break;
            case "n":
                printTables();
                System.out.print("\nBringing you back to main menu, press enter to continue:");
                input.next();
                break;
            default:
                System.out.println("Selection not valid...");
                reAddTable();
                break;
        }
    }

    // EFFECTS: processes user input for a name and returns it
    private String nameToAdd() {
        boolean loop = true; // force entry into loop
        String selection = "";

        while (loop) {
            System.out.print("\nEnter New Table Name: ");
            selection = input.next();
            String lowerCase = selection.toLowerCase();
            loop = false;
            if (lowerCase.equals("c")) {
                System.out.print("Name cannot be \"c\" as \"c\" is a program command, sorry");
                loop = true;
            } else {
                for (Table table : restaurant.getTables()) {
                    if (lowerCase.equals(table.getName().toLowerCase())) {
                        loop = true;
                        break;
                    }
                }
            }
            if (loop) {
                System.out.println("\nInvalid name, please try again");
            }
        }

        return selection;
    }

    // processes user input for an integer for max occupancy and returns it
    private int selectTableOccupancy() {
        String selection;
        System.out.print("Enter Positive Number for Table Max Occupancy: ");
        selection = input.next();
        try {
            int selectionInt = Integer.parseInt(selection);
            if (selectionInt <= 0) {
                System.out.println("Selection not valid...\n");
                return selectTableOccupancy();
            } else {
                return selectionInt;
            }
        } catch (Exception exception) {
            System.out.println("Selection not valid...\n");
            return selectTableOccupancy();
        }
    }

    // MODIFIES: this, Restaurant
    // EFFECTS: removes table from restaurant
    private void doRemoveTable() {
        if (new ArrayList<Table>().equals(restaurant.getTables())) {
            System.out.println("No tables to remove...");
        } else {
            printTables();

            String name = nameToRemove();
            restaurant.removeTable(name);

            System.out.println("Removed: " + name);

            reRemoveTable();

        }
    }

    // MODIFIES: this, Restaurant
    // processes user input for if the user wants to remove another table
    private void reRemoveTable() {
        if (new ArrayList<Table>().equals(restaurant.getTables())) {
            System.out.println("No more tables to remove, sending you back to the main menu...");
            System.out.print("Press Enter To Continue:");
            input.next();
        } else {
            System.out.println("\nWould you like to remove another table?");
            System.out.println("\ty -> yes");
            System.out.println("\tn -> no");

            System.out.print("Enter: ");
            String command;
            command = input.next();

            reRemoveTableSwitch(command);
        }
    }

    // MODIFIES: this, Restaurant
    // EFFECTS: processes user commands for if the user wants to remove another table
    private void reRemoveTableSwitch(String command) {
        switch (command) {
            case "y":
                doRemoveTable();
                break;
            case "n":
                printTables();
                System.out.print("\nBringing you back to main menu, press enter to continue:");
                input.next();
                break;
            default:
                System.out.println("Selection not valid...");
                reRemoveTable();
                break;
        }
    }

    // EFFECTS: processes user input and returns table name
    private String nameToRemove() {
        String selection = "";
        boolean loop = true; // force entry into loop

        while (loop) {
            System.out.print("\nEnter Table Name: ");
            selection = input.next();
            String lowerCase = selection.toLowerCase();
            for (Table table : restaurant.getTables()) {
                if (lowerCase.equals(table.getName().toLowerCase())) {
                    loop = false;
                    break;
                }
            }
            if (loop) {
                System.out.println("\nInvalid name, please try again");
            }
        }

        return selection;
    }

    // EFFECTS: shows amount the restaurant has earned and the amount of tips earned
    private void doShowProfit() {
        System.out.println("\nThe restaurant has made $" + DF.format(restaurant.getEarnings()) + "!");
        System.out.println("There has also been $" + DF.format(restaurant.getTips()) + " in tips!");

        System.out.print("\nPress Enter to Continue:");
        input.next();
    }

    // MODIFIES: this, Restaurant
    // EFFECTS: processes user input for assigning party to table
    private void doAssignCustomer() {
        printValidTables();

        System.out.print("\nEnter Party Size: ");
        String selection;
        int selectionInt;
        selection = input.next();

        try {
            selectionInt = Integer.parseInt(selection);
            String name = restaurant.assignCustomers(selectionInt);

            if (Objects.equals(name, "No Table")) {
                System.out.println("No available table to fit party size...\n");
                doAssignCustomer();
            } else {
                System.out.println("\nAssigned party to: " + name);
                reAssignCustomer();
            }
        } catch (Exception exception) {
            System.out.println("Selection not valid...\n");
            doAssignCustomer();
        }
    }

    // EFFECTS: prints all tables name that validStatuses = true
    private void printValidTables() {
        System.out.println("\nCurrent Tables:");
        for (Table table : restaurant.getTables()) {
            System.out.println("Table Name: " + table.getName() + ", Max Occupancy: " + table.getMaxOccupancy()
                    + ", Assignable Status: " + restaurant.validStatuses(table));
        }
    }

    // MODIFIES: this, Restaurant
    // prompts user if they would like to assign another party to a table and follows that command
    private void reAssignCustomer() {
        if (notAvailable()) {
            System.out.println("All tables are occupied or unavailable, sending you back to menu...");
            System.out.print("Press Enter To Continue:");
            input.next();
        } else {
            System.out.println("\nWould you like to assign another party to another table?");
            System.out.println("\ty -> yes");
            System.out.println("\tn -> no");

            System.out.print("Enter: ");
            String command = input.next().toLowerCase();

            reAssignCustomerSwitch(command);
        }
    }

    // MODIFIES: this, Restaurant
    // EFFECTS: processes user commands
    private void reAssignCustomerSwitch(String command) {
        switch (command) {
            case "y":
                doAssignCustomer();
                break;
            case "n":
                printTables();
                System.out.print("\nBringing you back to menu, press enter to continue:");
                input.next();
                break;
            default:
                System.out.println("Selection not valid...");
                reAssignCustomer();
                break;
        }
    }

    // EFFECTS: prints all tables name and max occupancy
    private void printTables() {
        System.out.println("\nCurrent Tables:");
        for (Table table : restaurant.getTables()) {
            System.out.println("Table Name: " + table.getName() + ", Max Occupancy: " + table.getMaxOccupancy());
        }
    }

    // EFFECTS: checks availability of all tables and returns false if one is available
    private boolean notAvailable() {
        boolean notAvailable = true;
        for (Table table : restaurant.getTables()) {
            if (restaurant.validStatuses(table)) {
                notAvailable = false;
                break;
            }
        }
        return notAvailable;
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

    // MODIFIES: this
    // EFFECTS: processes user commands for table menu
    private void processTableCommand(String command) {
        switch (command) {
            case "c":
                cleanTables();
                break;
            case "s":
                if (allSet()) {
                    System.out.println("No tables to set...");
                } else {
                    setTables();
                }
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

    // EFFECTS: returns true if all tables are set
    private boolean allSet() {
        boolean allSet = true;
        for (Table table : restaurant.getTables()) {
            if (!table.getSetStatus()) {
                allSet = false;
                break;
            }
        }
        return allSet;
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
        System.out.println("\nTable Clean Statuses:");
        for (Table table : restaurant.getTables()) {
            System.out.println("Table Name: " + table.getName() + ", Clean Status: " + table.getCleanStatus());
        }

        System.out.println("\nSelect from:");
        System.out.println("\tc -> clean table");
        System.out.println("\th -> history");
        System.out.println("\tr -> return back");

        System.out.print("Enter: ");
    }

    // MODIFIES: this, Restaurant
    // EFFECTS: processes user command for cleaning menu
    private void processCleanCommand(String command) {
        switch (command) {
            case "c":
                if (allClean()) {
                    System.out.println("No tables to clean...");
                } else {
                    cleanTable();
                }
                break;
            case "h":
                cleaningHistoryTable();
                break;
            default:
                System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this, Restaurant, Table
    // EFFECTS: processes user input for table needing to be cleaned and cleans selected table
    private void cleanTable() {
        System.out.println("\nTables Needing Cleaning: ");
        for (Table table : restaurant.getTables()) {
            if (!table.getCleanStatus()) {
                System.out.println("Table Name: " + table.getName() + ", Clean Status: " + table.getCleanStatus());
            }
        }
        boolean loop = true; // force entry into loop

        while (loop) {
            System.out.print("\nEnter Table Name: ");
            String selection = input.next().toLowerCase();
            for (Table table : restaurant.getTables()) {
                if (selection.equals(table.getName().toLowerCase())) {
                    loop = false;
                    table.cleanTable();
                    System.out.println("\nCleaned " + table.getName() + "!");
                    break;
                }
            }
            if (loop) {
                System.out.println("\nInvalid name, please try again");
            }
        }
        reCleanTable();
    }

    // MODIFIES: this
    // EFFECTS: processes user input for if the user wants to clean another table
    private void reCleanTable() {
        if (allClean()) {
            System.out.println("No tables to clean, sending you back to the cleaning menu...");
            System.out.print("Press Enter To Continue:");
            input.next();
        } else {
            System.out.println("Tables Needing Cleaning: ");
            for (Table table : restaurant.getTables()) {
                if (!table.getCleanStatus()) {
                    System.out.println("Table Name: " + table.getName() + ", Clean Status: " + table.getCleanStatus());
                }
            }
            System.out.println("\nWould you like to clean another table?");
            System.out.println("\ty -> yes");
            System.out.println("\tn -> no");

            System.out.print("Enter: ");
            String command;
            command = input.next().toLowerCase();

            reCleanTableSwitch(command);
        }
    }

    // MODIFIES: this, Restaurant
    // EFFECTS: processes user command for if the user wants to clean another table
    private void reCleanTableSwitch(String command) {
        switch (command) {
            case "y":
                cleanTable();
                break;
            case "n":
                printTables();
                System.out.print("\nBringing you back to cleaning menu, press enter to continue:");
                input.next();
                break;
            default:
                System.out.println("Selection not valid...");
                reCleanTable();
                break;
        }
    }

    // EFFECTS: produces true if all tables are clean
    private boolean allClean() {
        boolean allClean = true;
        for (Table table : restaurant.getTables()) {
            if (!table.getCleanStatus()) {
                allClean = false;
                break;
            }
        }
        return allClean;
    }

    // MODIFIES: this
    // EFFECTS: processes user input for cleaning history
    private void cleaningHistoryTable() {
        boolean loop = true; // force entry into loop
        while (loop) {
            System.out.println("\nTables:");
            for (Table table : restaurant.getTables()) {
                System.out.println("Table Name: " + table.getName());
            }
            System.out.print("\nEnter Table to Get Cleaning History: ");
            String selection = input.next().toLowerCase();
            for (Table table : restaurant.getTables()) {
                if (selection.equals(table.getName().toLowerCase())) {
                    loop = false;
                    printCleaningDates(table);
                    break;
                }
            }
            if (loop) {
                System.out.println("\nInvalid name, please try again");
            }
        }
        reCleaningHistory();
    }

    // EFFECTS: prints each date in cleaning history list for given table
    private void printCleaningDates(Table table) {
        List<String> history = table.getCleaningHistory();
        Collections.reverse(history);
        System.out.println("\nCleaning History:");
        for (String date : history) {
            System.out.println(date);
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for if the user wants to see cleaning history of another table
    private void reCleaningHistory() {
        System.out.println("\nTables:");
        for (Table table : restaurant.getTables()) {
            System.out.println("Table Name: " + table.getName());
        }
        System.out.println("\nWould you like to check the cleaning history of another table?");
        System.out.println("\ty -> yes");
        System.out.println("\tn -> no");

        System.out.print("Enter: ");
        String command;
        command = input.next().toLowerCase();


        reCleaningHistorySwitch(command);

    }

    // MODIFIES: this
    // EFFECTS: processes user command for if the user wants to see cleaning history for another table
    private void reCleaningHistorySwitch(String command) {
        switch (command) {
            case "y":
                cleaningHistoryTable();
                break;
            case "n":
                printTables();
                System.out.print("\nBringing you back to cleaning menu, press enter to continue:");
                input.next();
                break;
            default:
                System.out.println("Selection not valid...");
                reCleaningHistory();
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for setting tables
    private void setTables() {
        boolean loop = true; // force entry into loop

        while (loop) {
            System.out.println("\nTable Set Statuses: ");
            for (Table table : restaurant.getTables()) {
                System.out.println("Table Name: " + table.getName() + ", Set Status: " + table.getSetStatus());
            }
            System.out.print("\nEnter Table Name (enter c to cancel): ");
            String selection = input.next().toLowerCase();
            loop = processSetInput(selection);
        }

    }

    // MODIFIES: this, Restaurant, Table
    // EFFECTS: processes user command for setting tables
    private boolean processSetInput(String selection) {
        boolean loop = true;
        for (Table table : restaurant.getTables()) {
            if (selection.equals("c")) {
                System.out.print("Returning to table menu, press enter to continue:");
                input.next();
                loop = false;
                break;
            } else if (selection.equals(table.getName().toLowerCase()) && !table.getSetStatus()) {
                loop = false;
                table.trueSetTable();
                System.out.println("\nSet " + table.getName() + "!");
                reSetTables();
                break;
            }
        }
        if (loop) {
            System.out.println("Invalid name or table is already set, please try again");
        }
        return loop;
    }

    // MODIFIES: this
    // EFFECTS: processes user input for if they want to set another table
    private void reSetTables() {
        if (allSet()) {
            System.out.println("No tables to set, sending you back to table menu...");
            System.out.print("Press Enter to Continue:");
            input.next();
        } else {
            System.out.println("\nTables Needing To Be Set: ");
            for (Table table : restaurant.getTables()) {
                if (!table.getSetStatus()) {
                    System.out.println("Table Name: " + table.getName() + ", Set Status: " + table.getSetStatus());
                }
            }
            System.out.println("\nWould you like to set another table?");
            System.out.println("\ty -> yes");
            System.out.println("\tn -> no");

            System.out.print("Enter: ");
            String command;
            command = input.next().toLowerCase();

            reSetTablesSwitch(command);
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for if they want to set another table
    private void reSetTablesSwitch(String command) {
        switch (command) {
            case "y":
                setTables();
                break;
            case "n":
                printTables();
                System.out.print("\nBringing you back to table menu, press enter to continue:");
                input.next();
                break;
            default:
                System.out.println("Selection not valid...");
                reSetTables();
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for emptying tables
    private void availabilityTables() {
        if (allEmpty()) {
            System.out.println("No tables to empty...");
        } else {
            boolean loop = true; // force entry into loop

            while (loop) {
                System.out.println("\nTable Availability Statuses: ");
                for (Table table : restaurant.getTables()) {
                    System.out.println("Table Name: " + table.getName() + ", Availability Status: "
                            + table.getAvailabilityStatus());
                }
                System.out.print("\nEnter Table Name to mark as unoccupied (enter c to cancel): ");
                String selection = input.next().toLowerCase();
                loop = processAvailabilityInput(selection);
            }
        }

    }

    // MODIFIES this, Restaurant, Table
    // EFFECTS: processes user command for emptying tables
    private boolean processAvailabilityInput(String selection) {
        boolean loop = true;
        for (Table table : restaurant.getTables()) {
            if (selection.equals("c")) {
                System.out.print("Returning to table menu, press enter to continue:");
                input.next();
                loop = false;
                break;
            } else if (selection.equals(table.getName().toLowerCase()) && !table.getAvailabilityStatus()) {
                loop = false;
                table.emptyTable();
                System.out.println("\nEmptied " + table.getName() + "!");
                reAvailabilityTables();
                break;
            }
        }
        if (loop) {
            System.out.println("Invalid name or table is already empty, please try again");
        }
        return loop;
    }

    // MODIFIES: this
    // EFFECTS: processes user input for if they want to empty another table
    private void reAvailabilityTables() {
        if (allEmpty()) {
            System.out.println("No tables to empty, sending you back to table menu...");
            System.out.print("Press Enter to Continue:");
            input.next();
        } else {
            System.out.println("\nTables To Empty: ");
            for (Table table : restaurant.getTables()) {
                if (!table.getAvailabilityStatus()) {
                    System.out.println("Table Name: " + table.getName() + ", Availability Status: "
                            + table.getAvailabilityStatus());
                }
            }
            System.out.println("\nWould you like to empty another table?");
            System.out.println("\ty -> yes");
            System.out.println("\tn -> no");

            System.out.print("Enter: ");
            String command;
            command = input.next().toLowerCase();

            reAvailabilityTablesSwitch(command);
        }
    }

    // EFFECTS: produces true if all tables' availability status is false
    private boolean allEmpty() {
        boolean allEmpty = true;
        for (Table table : restaurant.getTables()) {
            if (!table.getAvailabilityStatus()) {
                allEmpty = false;
                break;
            }
        }
        return allEmpty;
    }

    // MODIFIES: this
    // processes user command for if the user wants to empty another table
    private void reAvailabilityTablesSwitch(String command) {
        switch (command) {
            case "y":
                availabilityTables();
                break;
            case "n":
                printTables();
                System.out.print("\nBringing you back to table menu, press enter to continue:");
                input.next();
                break;
            default:
                System.out.println("Selection not valid...");
                reAvailabilityTables();
                break;
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
        System.out.println("Tables: ");
        for (Table table : restaurant.getTables()) {
            System.out.println("Table Name: " + table.getName() + ", Availability Status: "
                    + table.getAvailabilityStatus());
        }

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
                if (allEmpty()) {
                    System.out.println("No occupied tables...");
                } else {
                    orderFoodTable();
                }
                break;
            case "d":
                if (allNotHungry()) {
                    System.out.println("No tables to delivery food to...");
                } else {
                    deliverFoodTables();
                }
                break;
            case "h":
                deliveryHistoryTable();
                break;
            default:
                System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for delivering food
    private void deliverFoodTables() {
        boolean loop = true; // force entry into loop

        while (loop) {
            System.out.println("\nTable Delivery Statuses: ");
            for (Table table : restaurant.getTables()) {
                System.out.println("Table Name: " + table.getName() + ", Delivery Status: "
                        + table.getFoodDeliveryStatus());
            }
            System.out.print("\nEnter Table Name (enter c to cancel): ");
            String selection = input.next().toLowerCase();
            loop = processDeliveryInput(selection);
        }
    }

    // MODIFIES: this, Restaurant, Table
    // EFFECTS: processes user command for delivering food
    private boolean processDeliveryInput(String selection) {
        boolean loop = true;
        for (Table table : restaurant.getTables()) {
            if (selection.equals("c")) {
                System.out.print("Returning to food menu, press enter to continue:");
                input.next();
                loop = false;
                break;
            } else if (selection.equals(table.getName().toLowerCase()) && !table.getFoodDeliveryStatus()) {
                loop = false;
                table.deliverFood();
                System.out.println("\nDelivered food to " + table.getName() + "!");
                reDeliveryFoodTables();
                break;
            }
        }
        if (loop) {
            System.out.println("Invalid name or no food to delivery to table, please try again");
        }
        return loop;
    }

    // MODIFIES: this
    // EFFECTS: processes user input for if the user wants to deliver food to another table
    private void reDeliveryFoodTables() {
        if (allNotHungry()) {
            System.out.println("No tables to delivery food to, sending you back to food menu...");
            System.out.print("Press Enter to Continue:");
            input.next();
        } else {
            System.out.println("Tables Needing Food: ");
            for (Table table : restaurant.getTables()) {
                if (!table.getFoodDeliveryStatus()) {
                    System.out.println("Table Name: " + table.getName() + ", Delivery Status: " + table.getSetStatus());
                }
            }
            System.out.println("\nWould you like to set another table?");
            System.out.println("\ty -> yes");
            System.out.println("\tn -> no");

            System.out.print("Enter: ");
            String command;
            command = input.next().toLowerCase();

            reDeliveryFoodTablesSwitch(command);
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for if the user wants to deliver food to another table
    private void reDeliveryFoodTablesSwitch(String command) {
        switch (command) {
            case "y":
                deliverFoodTables();
                break;
            case "n":
                printTables();
                System.out.print("\nBringing you back to food menu, press enter to continue:");
                input.next();
                break;
            default:
                System.out.println("Selection not valid...");
                reDeliveryFoodTables();
                break;
        }
    }

    // EFFECTS: produces true if all tables' delivery status is false
    private boolean allNotHungry() {
        boolean allNotHungry = true;
        for (Table table : restaurant.getTables()) {
            if (!table.getFoodDeliveryStatus()) {
                allNotHungry = false;
                break;
            }
        }
        return allNotHungry;
    }

    // MODIFIES: this
    // EFFECTS: processes user input for ordering food for a table
    private void orderFoodTable() {
        System.out.println("\nTables That Can Order: ");
        for (Table table : restaurant.getTables()) {
            if (!table.getAvailabilityStatus()) {
                System.out.println("Table Name: " + table.getName() + ", Max Occupancy: " + table.getMaxOccupancy());
            }
        }
        boolean loop = true; // force entry into loop

        while (loop) {
            System.out.print("\nEnter Table Name: ");
            String selection = input.next().toLowerCase();
            for (Table table : restaurant.getTables()) {
                if (!table.getAvailabilityStatus() && selection.equals(table.getName().toLowerCase())) {
                    loop = false;
                    orderFood(table.getName());
                    System.out.println("\nOrdered food for " + table.getName() + "!");
                    break;
                }
            }
            if (loop) {
                System.out.println("\nInvalid name or cannot order food to tha table, please try again");
            }
        }
        reOrderFoodTable();
    }

    // MODIFIES: this, Restaurant
    // EFFECTS: processes user command for ordering food item
    private void orderFood(String name) {
        System.out.println("\nFood That Can Be Ordered: ");
        for (Food food : restaurant.getMenu()) {
            System.out.println("Food Name: " + food.getName() + ", Price: $" + DF.format(food.getPrice()));
        }
        boolean loop = true; // force entry into loop

        while (loop) {
            System.out.print("\nEnter Food Name: ");
            String selection = input.next().toLowerCase();
            for (Food food : restaurant.getMenu()) {
                if (selection.equals(food.getName().toLowerCase())) {
                    loop = false;
                    restaurant.orderFood(name, food.getName());
                    System.out.println("\nOrdered " + food.getName() + " for "
                            + name + "!");
                    break;
                }
            }
            if (loop) {
                System.out.println("\nInvalid name, please try again");
            }
        }
        reOrderFood(name);
    }

    // MODIFIES: this
    // EFFECTS: processes user input for if the user wants to order another food item
    private void reOrderFood(String name) {
        System.out.println("\nWould you like to order food more food");
        System.out.println("\ty -> yes");
        System.out.println("\tn -> no");

        System.out.print("Enter: ");
        String command;
        command = input.next().toLowerCase();

        reOrderFoodSwitch(command, name);
    }

    // MODIFIES: this
    // EFFECTS: processes user command for if the user wants to order another food item
    private void reOrderFoodSwitch(String command, String name) {
        switch (command) {
            case "y":
                orderFood(name);
                break;
            case "n":
                System.out.print("\nDone ordering food for this table, press enter to continue:");
                input.next();
                break;
            default:
                System.out.println("Selection not valid...");
                reOrderFood(name);
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for if the user wants to order food for another table
    private void reOrderFoodTable() {
        System.out.println("\nTables That Can Order: ");
        for (Table table : restaurant.getTables()) {
            if (!table.getAvailabilityStatus()) {
                System.out.println("Table Name: " + table.getName() + ", Max Occupancy: " + table.getMaxOccupancy());
            }
        }
        System.out.println("\nWould you like to order food for another table?");
        System.out.println("\ty -> yes");
        System.out.println("\tn -> no");

        System.out.print("Enter: ");
        String command;
        command = input.next().toLowerCase();

        reOrderFoodTableSwitch(command);

    }

    // MODIFIES: this
    // EFFECTS: processes user command for if the user wants to order food for another table
    private void reOrderFoodTableSwitch(String command) {
        switch (command) {
            case "y":
                orderFoodTable();
                break;
            case "n":
                printTables();
                System.out.print("\nBringing you back to food menu, press enter to continue:");
                input.next();
                break;
            default:
                System.out.println("Selection not valid...");
                reOrderFoodTable();
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for displaying delivery history
    private void deliveryHistoryTable() {
        boolean loop = true; // force entry into loop
        while (loop) {
            System.out.println("\nTables:");
            for (Table table : restaurant.getTables()) {
                System.out.println("Table Name: " + table.getName());
            }
            System.out.print("\nEnter Table To Get Delivery History: ");
            String selection = input.next().toLowerCase();
            for (Table table : restaurant.getTables()) {
                if (selection.equals(table.getName().toLowerCase())) {
                    loop = false;
                    printDeliveryDates(table);
                    break;
                }
            }
            if (loop) {
                System.out.println("\nInvalid name, please try again");
            }
        }
        reDeliveryHistory();
    }

    // EFFECTS: prints each date in delivery history for given table
    private void printDeliveryDates(Table table) {
        List<String> history = table.getDeliveryHistory();
        Collections.reverse(history);
        System.out.println("\nDelivery History:");
        for (String date : history) {
            System.out.println(date);
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for if the user wants to display delivery history for another table
    private void reDeliveryHistory() {
        System.out.println("\nTables:");
        for (Table table : restaurant.getTables()) {
            System.out.println("Table Name: " + table.getName());
        }
        System.out.println("\nWould you like to check the delivery history of another table?");
        System.out.println("\ty -> yes");
        System.out.println("\tn -> no");

        System.out.print("Enter: ");
        String command;
        command = input.next().toLowerCase();

        reDeliveryHistorySwitch(command);

    }

    // MODIFIES: this
    // EFFECTS: processes user command for if the user wants to display delivery history for another table
    private void reDeliveryHistorySwitch(String command) {
        switch (command) {
            case "y":
                deliveryHistoryTable();
                break;
            case "n":
                printTables();
                System.out.print("\nBringing you back to food menu, press enter to continue:");
                input.next();
                break;
            default:
                System.out.println("Selection not valid...");
                reDeliveryHistory();
                break;
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
        System.out.println("\nTable Pay Statuses:");
        for (Table table : restaurant.getTables()) {
            if (!table.getAvailabilityStatus()) {
                System.out.println("Table Name: " + table.getName() + ", Pay Status: "
                        + table.getBill().getPayStatus());
            } else {
                System.out.println("Table Name " + table.getName() + " is not occupied, no bill");
            }
        }

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
                if (allPaid()) {
                    System.out.println("No tables with active bills...");
                } else {
                    billTable();
                }
                break;
            case "h":
                purchaseHistoryTable();
                break;
            default:
                System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input and command for billing a table
    private void billTable() {
        System.out.println("Tables Needing To Pay: ");
        for (Table table : restaurant.getTables()) {
            if (!table.getAvailabilityStatus() && !table.getBill().getPayStatus()) {
                System.out.println("Table Name: " + table.getName() + ", Pay Status: "
                        + table.getBill().getPayStatus());
            }
        }
        boolean loop = true; // force entry into loop

        while (loop) {
            System.out.print("\nEnter Table Name: ");
            String selection = input.next().toLowerCase();
            for (Table table : restaurant.getTables()) {
                if (!table.getAvailabilityStatus() && selection.equals(table.getName().toLowerCase())) {
                    loop = false;
                    payBill(table);
                    break;
                }
            }
            if (loop) {
                System.out.println("\nInvalid name or no bill at table, please try again");
            }
        }
        reBillTable();
    }

    // MODIFIES: this, Restaurant, Table
    // EFFECTS: pays for food for given table
    private void payBill(Table table) {
        System.out.println("\nCost: $" + DF.format(table.getBill().getCost()));
        tips(table);
        table.payForFood();
        System.out.println("\nTotal Cost: $" + DF.format(table.getBill().getCost()));
        System.out.print("\nPress Enter To Continue:");
        input.next();
    }

    // MODIFIES: this
    // EFFECTS: processes user input and command for tip amount
    private void tips(Table table) {
        String selection;
        System.out.print("\nEnter Tip Amount: $");
        selection = input.next();
        try {
            double selectionDouble = Double.parseDouble(selection);
            if (selectionDouble < 0) {
                System.out.println("Selection not valid...\n");
                tips(table);
            } else {
                table.getBill().setTip(selectionDouble);
                System.out.println("\n$" + DF.format(selectionDouble) + " adding in tips!");
            }
        } catch (Exception exception) {
            System.out.println("Selection not valid...\n");
            tips(table);
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for if the user wants to bill another table
    private void reBillTable() {
        if (allPaid()) {
            System.out.println("No tables need to pay, sending you back to the billing menu...");
            System.out.print("Press Enter To Continue:");
            input.next();
        } else {
            System.out.println("Tables Needing To Pay: ");
            for (Table table : restaurant.getTables()) {
                if (!table.getAvailabilityStatus() && !table.getBill().getPayStatus()) {
                    System.out.println("Table Name: " + table.getName() + ", Pay Status: "
                            + table.getBill().getPayStatus());
                }
            }
            System.out.println("\nWould you like bill another table?");
            System.out.println("\ty -> yes");
            System.out.println("\tn -> no");

            System.out.print("Enter: ");
            String command;
            command = input.next().toLowerCase();

            reBillTableSwitch(command);
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for if the user wants to bill another table
    private void reBillTableSwitch(String command) {
        switch (command) {
            case "y":
                billTable();
                break;
            case "n":
                printTables();
                System.out.print("\nBringing you back to billing menu, press enter to continue:");
                input.next();
                break;
            default:
                System.out.println("Selection not valid...");
                reBillTable();
                break;
        }
    }

    // EFFECTS: produces true if all tables' pay status is true
    private boolean allPaid() {
        boolean allPaid = true;
        for (Table table : restaurant.getTables()) {
            if (!table.getAvailabilityStatus() && !table.getBill().getPayStatus()) {
                allPaid = false;
                break;
            }
        }
        return allPaid;
    }

    // MODIFIES: this
    // EFFECTS: processes user input for displaying purchase history
    private void purchaseHistoryTable() {
        boolean loop = true; // force entry into loop
        while (loop) {
            System.out.println("\nTables:");
            for (Table table : restaurant.getTables()) {
                System.out.println("Table Name: " + table.getName());
            }
            System.out.print("\nEnter Table to Get Purchase History: ");
            String selection = input.next().toLowerCase();
            for (Table table : restaurant.getTables()) {
                if (selection.equals(table.getName().toLowerCase())) {
                    loop = false;
                    printPurchaseDates(table);
                    break;
                }
            }
            if (loop) {
                System.out.println("\nInvalid name, please try again");
            }
        }
        rePurchaseHistoryTable();
    }

    // MODIFIES: this
    // EFFECTS: displays each date in purchase history to user
    private void printPurchaseDates(Table table) {
        List<String> history = table.getPurchaseHistory();
        Collections.reverse(history);
        System.out.println("\nPurchase History:");
        for (String date : history) {
            System.out.println(date);
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for if user wants to display purchase history of another table
    private void rePurchaseHistoryTable() {
        System.out.println("\nTables:");
        for (Table table : restaurant.getTables()) {
            System.out.println("Table Name: " + table.getName());
        }
        System.out.println("\nWould you like to check the purchase history of another table?");
        System.out.println("\ty -> yes");
        System.out.println("\tn -> no");

        System.out.print("Enter: ");
        String command;
        command = input.next().toLowerCase();


        rePurchaseHistorySwitch(command);

    }

    // MODIFIES: this
    // EFFECTS: processes user command for if user wants to display purchase history of another table
    private void rePurchaseHistorySwitch(String command) {
        switch (command) {
            case "y":
                purchaseHistoryTable();
                break;
            case "n":
                printTables();
                System.out.print("\nBringing you back to billing menu, press enter to continue:");
                input.next();
                break;
            default:
                System.out.println("Selection not valid...");
                rePurchaseHistoryTable();
                break;
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

}