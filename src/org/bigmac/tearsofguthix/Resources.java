package org.bigmac.tearsofguthix;


/**
 * The constants and strings for the tears of guthix script.
 * Some of the strings might be better off as integer ids.
 */
public class Resources {

    /** Widget ID of the tear counter (valid while playing the minigame) */
    public static final int WIDGET_ID = 4;
    
    /** Main component of the minigame widget */
    public static final int COMPONENT_ID = 0;

    /** Juna the snake character to talk to to start the minigame */
    public static final String JUNA_STR = "Juna";
    
    /** The good tears are blue */
    public static final String BLUE_TEARS_STR = "Blue tears";
    
    /**
     * The weeping wall is what the player interacts with
     * (neither blue nor green)
     */
    public static final String WALL_STR = "Weeping wall";

    /** The interaction string */
    public static final String COLLECT_STR = "Collect-from";

    /** Reaction delays, upper and lower limits */
    public static final int REACTION_LOWER = 10;
    public static final int REACTION_UPPER = 20;

    /** Player orientation constants */
    public static final int EAST_ORI = 0;
    public static final int NORTH_ORI = 90;
    public static final int SOUTH_ORI = 270;
    
}