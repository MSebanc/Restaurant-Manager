package ui.console.manager;

import model.Table;
import ui.exceptions.StopRedoException;
import ui.exceptions.TableForLoopBodyException;
import ui.console.interfaces.*;

import java.util.Collections;
import java.util.List;

// Restaurant Manager Application (functions that take lambda functions as parameters)
public class RestaurantManagerAbstractFunctions extends RestaurantManager {

    // EFFECTS: Constructor
    public RestaurantManagerAbstractFunctions() {
        // does nothing
    }

    // EFFECTS: abstract function that prints tables, calls given command function on tables,
    // and calls a function to complete command again
    protected static void abstractTableCommand(PrintFunction printTable, TableActionsCommands commandFunction,
                                               String redoQuestion, String menuName, StopRedo stopRedo) {

        try {
            printTable.run();
            commandFunction.run();
            redoAbstractTableCommand(printTable, commandFunction, redoQuestion, menuName, stopRedo);
        } catch (StopRedoException e) {
            // do nothing
        }

    }

    // MODIFIES: RestaurantManager
    // processes user command for if the user wants to redo command function
    protected static void redoAbstractTableCommand(PrintFunction printTable, TableActionsCommands commandFunction,
                                          String redoQuestion, String menuName, StopRedo stopRedo) {
        System.out.println("\nWould you like to " + redoQuestion);
        System.out.println("\ty -> yes");
        System.out.println("\tn -> no");

        System.out.print("Enter: ");
        String command = input.next();

        redoAbstractTableCommandSwitch(printTable, commandFunction, redoQuestion, command, menuName, stopRedo);
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: processes user commands for if the user wants to redo command function
    protected static void redoAbstractTableCommandSwitch(PrintFunction printTable, TableActionsCommands commandFunction,
                                                String redoQuestion, String command, String menuName,
                                                StopRedo stopRedo) {
        switch (command) {
            case "y":
                abstractTableCommand(printTable, commandFunction, redoQuestion, menuName, stopRedo);
                break;
            case "n":
                printTable.run();
                System.out.print("\nBringing you back to " + menuName + ", press enter to continue:");
                input.next();
                break;
            default:
                System.out.println("Selection not valid...");
                redoAbstractTableCommand(printTable, commandFunction, redoQuestion, menuName, stopRedo);
                break;
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: processes user input for the given table command calls function with that input
    protected static void abstractTableFunction(PrintFunction printTable, String enterStatement,
                                       TableForLoops forLoop, String invalidInputStatement,
                                       String redoQuestion, String menuName, StopRedo stopRedo) {

        TableActionsCommands commandFunction = () -> {
            boolean forLoopException = true;
            while (forLoopException) {
                System.out.print("\n" + enterStatement);
                String selection = input.next();
                try {
                    forLoop.run(selection);
                    forLoopException = false;
                } catch (TableForLoopBodyException e) {
                    System.out.println("\nInvalid " + invalidInputStatement + ", please try again");
                }
            }
            stopRedo.run();
        };

        try {
            commandFunction.run();
            redoAbstractTableCommand(printTable, commandFunction, redoQuestion, menuName, stopRedo);
        } catch (StopRedoException e) {
            // do nothing
        }
    }

    // MODIFIES: RestaurantManager
    // EFFECTS: processes user input for history functions
    protected static void abstractHistoryFunction(String historyName, PrintHistoryFunction printHistory,
                                                  String redoQuestion, String menuName) {
        StopRedo stopRedo = () -> {

        };
        PrintFunction printTable = RestaurantManagerPrintAndAllStatusFunctions::printTables;

        TableActionsCommands commandFunction = () -> {
            boolean forLoopException = true;
            while (forLoopException) {
                try {
                    System.out.print("\nEnter Table Name To Get " + historyName + " History For: ");
                    String selection = input.next();

                    historyForLoop(selection, printHistory, historyName);
                    forLoopException = false;

                } catch (TableForLoopBodyException e) {
                    System.out.println("\nInvalid name, please try again");
                }
            }
        };

        try {
            commandFunction.run();
            redoAbstractTableCommand(printTable, commandFunction, redoQuestion, menuName, stopRedo);
        } catch (StopRedoException e) {
            // do nothing
        }
    }

    // EFFECTS: prints history dates of given history function
    protected static void printHistoryDates(GetHistoryFunction getHistory, String historyName) {
        List<String> history = getHistory.run();
        Collections.reverse(history);
        System.out.println("\n" + historyName + " History:");
        for (String date : history) {
            System.out.println(date);
        }
    }

    // EFFECTS: prints table with given print statement
    protected static void printTableFunction(TableToStringFunction printStatement) {
        System.out.println("\nCurrent Tables:");
        for (Table table : restaurant.getTables()) {
            System.out.println("Table Name: " + table.getName() + printStatement.run(table));
        }
    }

    // EFFECTS: returns true when given condition is met
    protected static boolean allStatus(AllStatusIfBoolean ifBoolean) {
        boolean allStatus = true;
        for (Table table : restaurant.getTables()) {
            if (ifBoolean.run(table)) {
                allStatus = false;
                break;
            }
        }
        return allStatus;
    }

    // EFFECTS: loops through all tables in restaurant and prints the history of that function selection is equal to
    // table name. Throws TableForLoopBodyException if no table is found
    protected static void historyForLoop(String selection, PrintHistoryFunction printHistory, String historyName)
            throws TableForLoopBodyException {
        boolean forLoopException = true;
        for (Table table: restaurant.getTables()) {
            if (selection.equalsIgnoreCase(table.getName())) {
                printHistory.run(table, historyName);
                forLoopException = false;
            }
        }
        if (forLoopException) {
            throw new TableForLoopBodyException();
        }
    }

}
