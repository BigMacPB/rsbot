package org.bigmac.tearsofguthix.tasks;


/**
 * The task interface
 *
 * All major components of the script are separated into tasks that contain an
 * activate method that defines when the task should occur, and an execute
 * method that defines what the task actually does.
 *
 * The tasks are then checked for activation in a loop at each poll by the
 * polling script.
 */
public interface Task {

    /** When to activate */
    boolean activate();
    
    /** What to do */
    void execute();
        
}