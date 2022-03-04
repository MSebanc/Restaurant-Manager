package ui.interfaces;

import model.Table;

// Interface for lambda functions that turns table into string and returns it
public interface TableToStringFunction {

    // EFFECTS: runs lambda function
    String run(Table table);
}
