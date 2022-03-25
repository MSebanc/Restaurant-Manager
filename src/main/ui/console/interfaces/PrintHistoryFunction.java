package ui.console.interfaces;

import model.Table;

// Interface for lambda functions that prints history for table
public interface PrintHistoryFunction {

    // EFFECTS: runs lambda function
    void run(Table table, String historyName);
}
