package org.bigmac.tearsofguthix.tasks;


import static org.bigmac.tearsofguthix.Resources.*;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;
import org.powerbot.script.rt6.Widget;


/**
 * Task for starting the tears of guthix minigame
 */
public class StartMinigame implements Task {

    /** Reference to the client context for doing everything useful */
    private ClientContext ctx;

    /**
     * Passes a reference to the client context to this task instance
     * @param ctx The client context
     */
    public StartMinigame(ClientContext ctx) {
        this.ctx = ctx;
    }

    /**
     * When to activate this task
     */
    @Override
    public boolean activate() {
        // Return Juna is valid, and Tears widget is not valid
        Npc juna = ctx.npcs.select().name(JUNA_STR).peek();
        Widget w = ctx.widgets.select().id(WIDGET_ID).peek();
        return juna.valid() && !w.valid();
    }

    /**
     * What to do when activated
     */
    @Override
    public void execute() {
        // Interact and tell story to Juna
    }
        
}