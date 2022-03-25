package ui.console.interfaces;

import model.Table;

// Interface for lambda functions that return a boolean value if each table has a certain status
public interface AllStatusIfBoolean {

    // EFFECTS: runs lambda function
    boolean run(Table table);
}
