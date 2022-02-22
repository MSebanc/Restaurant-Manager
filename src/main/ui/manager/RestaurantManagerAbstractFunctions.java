package ui.manager;

import model.Table;
import ui.exceptions.StopRedoException;
import ui.exceptions.TableForLoopBodyException;
import ui.interfaces.*;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

public class RestaurantManagerAbstractFunctions extends RestaurantManager {

    public RestaurantManagerAbstractFunctions() throws FileNotFoundException {
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

    // MODIFIES: this
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

    // MODIFIES: this
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

    // MODIFIES: this
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

                stopRedo.run();
            }
        };

        try {
            commandFunction.run();
            redoAbstractTableCommand(printTable, commandFunction, redoQuestion, menuName, stopRedo);
        } catch (StopRedoException e) {
            // do nothing
        }
    }

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

    protected static void printHistoryDates(GetHistoryFunction getHistory, String historyName) {
        List<String> history = getHistory.run();
        Collections.reverse(history);
        System.out.println("\n" + historyName + " History");
        for (String date : history) {
            System.out.println(date);
        }
    }

    protected static void printTableFunction(TableToStringFunction printStatement) {
        System.out.println("\nCurrent Tables:");
        for (Table table : restaurant.getTables()) {
            System.out.println("Table Name: " + table.getName() + printStatement.run(table));
        }
    }

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
