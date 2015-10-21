package org.bigmac.tearsofguthix;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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
    
    private static final int WIDGET_ID = 4;
    private static final int COMPONENT_ID = 0;

    private static final String JUNA_STR = "Juna";
    private static final String BLUE_TEARS_STR = "Blue tears";
    private static final String WALL_STR = "Weeping wall";
    private static final String COLLECT_STR = "Collect-from";

    private static final int REACTION_LOWER = 10;
    private static final int REACTION_UPPER = 20;
    
    private static final int EAST_ORI = 0;
    private static final int NORTH_ORI = 90;
    private static final int SOUTH_ORI = 270;
    
    private static final List<Task> TASK_LIST = new ArrayList<Task>();
    
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

    
    private static interface Task {
        
        public boolean activate();
        public void execute();
        
    }
    
    
    private static class CollectTears implements Task {

        private ClientContext ctx;
        private Tile targetTile;
        
        public CollectTears(ClientContext ctx) {
            this.ctx = ctx;
        }
        
        @Override
        public boolean activate() {
            Widget w = ctx.widgets.select().id(WIDGET_ID).peek();
            return w.valid() 
                    && w.component(COMPONENT_ID).valid();
        }

        private boolean tileHasBlueTears(Tile t) {
            return ctx.objects.select().at(t).name(BLUE_TEARS_STR).peek().valid();
        }
        
        private boolean facingBlueTears() {
            Tile inFront = ctx.players.local().tile();
            
            switch(ctx.players.local().orientation()) {
                case EAST_ORI:
                    // Looking at the East wall
                    inFront = inFront.derive(1, 0);
                    break;
                case NORTH_ORI:
                    // Looking at the North wall
                    inFront = inFront.derive(0, 1);
                    break;
                case SOUTH_ORI:
                    // Looking at the South wall
                    inFront = inFront.derive(0, -1);
                    break;
                default:
                    // Wrong direction so we cannot be facing blue tears
                    return false;
            }
            
            // Direction is good, check if the tile in front has blue tears
            return tileHasBlueTears(inFront);
            
        }
        
        private void clickWeepingWall() {
            
            // Filter the "Weeping walls" that don't have blue streams
            ctx.objects.select(new Filter<GameObject>() {

                @Override
                public boolean accept(GameObject arg0) {
                    return arg0.name().equals(WALL_STR)
                            && tileHasBlueTears(arg0.tile());
                }
                
            });
            
            // If query was successful
            if (!ctx.objects.isEmpty()) {
                
                GameObject weepingWall = ctx.objects.nearest().peek();
                if (weepingWall.interact(COLLECT_STR)) {
                    targetTile = weepingWall.tile();
                    
                    // Interact and wait until the player is either no longer
                    // idle (started harvesting tears), or is moving (running
                    // to the tears) -- when either of these happen, no more
                    // interactions will occur unless the stream changed colour
                    Condition.wait(new Callable<Boolean>() {

                        @Override
                        public Boolean call() throws Exception {
                            return !ctx.players.local().idle()
                                    || ctx.players.local().inMotion();
                        }
                        
                    });
                }
            }
        }
        
        @Override
        public void execute() {
            // Click blue tears
            Player p = ctx.players.local();
            
            // If the player is idle or not in right place, AND not moving TO
            // the right place...
            if ((p.idle() || !facingBlueTears())
                    && !p.inMotion()) {
                clickWeepingWall();
            }
            
            // If we are moving, only switch targets if the initial target is
            // no longer blue
            if (p.inMotion()
                    && targetTile != null
                    && !tileHasBlueTears(targetTile)) {
                clickWeepingWall();
            }
            
            // Above could obviously be in one 'if', I've left it like this
            // because it is easier to read -- the second 'if' interrupts
            // movement if the stream changes colour while moving
            Condition.sleep(Random.nextInt(REACTION_LOWER, REACTION_UPPER));
        }
        
    }
    
    
    private static class StartMinigame implements Task {

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
    
    
}
