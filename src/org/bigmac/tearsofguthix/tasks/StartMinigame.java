package org.bigmac.tearsofguthix.tasks;


import static org.bigmac.tearsofguthix.Resources.*;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;
import org.powerbot.script.rt6.Widget;


public class StartMinigame implements Task {

    private ClientContext ctx;
        
    public StartMinigame(ClientContext ctx) {
        this.ctx = ctx;
    }
        
    @Override
    public boolean activate() {
        // Return Juna is valid, and Tears widget is not valid
        Npc juna = ctx.npcs.select().name(JUNA_STR).peek();
        Widget w = ctx.widgets.select().id(WIDGET_ID).peek();
        return juna.valid() && !w.valid();
    }

    @Override
    public void execute() {
        // Interact and tell story to Juna
    }
        
}