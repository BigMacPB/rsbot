package org.bigmac.tearsofguthix;


import java.util.ArrayList;
import java.util.List;

import org.bigmac.tearsofguthix.tasks.*;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;
import org.powerbot.script.rt6.Player;
import org.powerbot.script.rt6.Widget;


@Script.Manifest(
        name = "Tears of Guthix", 
        description = "Learning RSBot", 
        properties = "client=6"
)
public class TearsOfGuthix extends PollingScript<ClientContext> {
    
    private static final List<Task> TASK_LIST = new ArrayList<>();
    
    @Override
    public void start() {
        System.out.println("Script started");
        TASK_LIST.add(new StartMinigame(ctx));
        TASK_LIST.add(new CollectTears(ctx));
    }
    
    @Override
    public void poll() {
        for (Task task : TASK_LIST) {
            if (task.activate()) {
                task.execute();
            }
        }
    }

    @Override
    public void stop() {
        System.out.println("Script stopped");
    }
    
}
