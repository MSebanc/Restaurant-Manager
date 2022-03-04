package ui.interfaces;

import java.util.List;

// Interface for lambda functions that return a history for a table
public interface GetHistoryFunction {

    // EFFECTS: runs lambda function
    List<String> run();
}
