package ui.interfaces;

import ui.exceptions.StopRedoException;

// Interface for lambda functions that checks if a function should not be run again
public interface StopRedo {

    // EFFECTS: runs lambda function
    void run() throws StopRedoException;
}
