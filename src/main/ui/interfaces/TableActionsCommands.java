package ui.interfaces;

import ui.exceptions.StopRedoException;

public interface TableActionsCommands {
    void run() throws StopRedoException;
}
