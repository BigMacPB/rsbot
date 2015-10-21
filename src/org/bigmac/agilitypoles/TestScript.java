package org.bigmac.agilitypoles;

import java.util.HashMap;
import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Npc;

@Script.Manifest(
        name = "Test script", 
        description = "Testing RSBot API.", 
        properties = "client=6"
)
public class TestScript extends PollingScript<ClientContext> {

    private static final int LADY_HEFIN_ID = 20272;
    private static final int[] ANIM_IDS = { 24532, 24535, 24537, 24539 };
    private static final int[] COMPONENT_IDS = { 1, 2, 3, 4 };
    private static final int WIDGET_ID = 1552;
    
    private static final HashMap<Integer, Integer> MAPPING = new HashMap<Integer, Integer>();

    @Override
    public void start() {
        System.out.println("Script started");
        for (int i = 0; i < ANIM_IDS.length; i++) {
            MAPPING.put(ANIM_IDS[i], COMPONENT_IDS[i]);
        }
    }

    @Override
    public void poll() {
        Npc ladyHefin = ctx.npcs.id(LADY_HEFIN_ID).nearest().peek();
        int hefinAnimation = ladyHefin.animation();
        int playerAnimation = ctx.players.local().animation();

        if (playerAnimation != hefinAnimation && MAPPING.containsKey(hefinAnimation)) {

            Component comp = ctx.widgets.id(WIDGET_ID).peek().component(MAPPING.get(hefinAnimation));
            if (comp.valid()) {
                comp.click();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        return ctx.players.local().animation() == hefinAnimation;
                    }
                });
            } else {
                System.out.println("cannot find controls...");
            }
        }
        Condition.sleep();

    }

    @Override
    public void stop() {
        System.out.println("Script stopped");
    }

}
