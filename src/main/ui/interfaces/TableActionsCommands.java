package ui.interfaces;

import ui.exceptions.StopRedoException;

// Interface for lambda functions that operates on a table
public interface TableActionsCommands {

    // EFFECTS: runs lambda function
    void run() throws StopRedoException;
}
