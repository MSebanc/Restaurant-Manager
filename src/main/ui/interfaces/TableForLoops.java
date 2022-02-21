package ui.interfaces;

import ui.exceptions.TableForLoopBodyException;

public interface TableForLoops {
    void run(String selection) throws TableForLoopBodyException;
}
