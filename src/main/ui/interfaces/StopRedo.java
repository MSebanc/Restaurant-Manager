package ui.interfaces;

import ui.exceptions.StopRedoException;

public interface StopRedo {
    void run() throws StopRedoException;
}
