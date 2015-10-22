package org.bigmac.tearsofguthix.tasks;


import java.util.concurrent.Callable;

import static org.bigmac.tearsofguthix.Resources.*;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Player;
import org.powerbot.script.rt6.Widget;

import static org.bigmac.tearsofguthix.Resources.*;


public class CollectTears implements Task {

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