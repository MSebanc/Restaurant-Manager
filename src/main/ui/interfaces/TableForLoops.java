package ui.interfaces;

import ui.exceptions.TableForLoopBodyException;

// Interface for lambda functions that runs a for loop
public interface TableForLoops {

    // EFFECTS: runs lambda function
    void run(String selection) throws TableForLoopBodyException;
}
