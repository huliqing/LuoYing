/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly;

/**
 *
 * @author huliqing
 */
public class Config {
    
    private static boolean shortcutLocked = false;
    private static float shortcutSize = 1.0f;
    private static float speakTimeWorld = 0.22f;
    private static float speakTimeMin = 0.25f;
    private static float speakTimeMax = 8.0f;
    private static float speakMaxDistance = 80f;
    private static final String[] lanGames = {"gameSurvival"};
    
    public static float getShortcutSize() {
        return shortcutSize;
    }
    
    public static void setShortcutSize(float shortcutSize) {
        Config.shortcutSize = shortcutSize;
    }
    
    public static boolean isShortcutLocked() {
        return shortcutLocked;
    }

    public static void setShortcutLocked(boolean shortcutLocked) {
        Config.shortcutLocked = shortcutLocked;
    }

    public static float getSpeakTimeWorld() {
        return speakTimeWorld;
    }

    public static void setSpeakTimeWorld(float speakTimeWorld) {
        Config.speakTimeWorld = speakTimeWorld;
    }

    public static float getSpeakTimeMin() {
        return speakTimeMin;
    }

    public static void setSpeakTimeMin(float speakTimeMin) {
        Config.speakTimeMin = speakTimeMin;
    }

    public static float getSpeakTimeMax() {
        return speakTimeMax;
    }

    public static void setSpeakTimeMax(float speakTimeMax) {
        Config.speakTimeMax = speakTimeMax;
    }

    public static float getSpeakMaxDistance() {
        return speakMaxDistance;
    }

    public static void setSpeakMaxDistance(float speakMaxDistance) {
        Config.speakMaxDistance = speakMaxDistance;
    }

    public static String[] getLanGames() {
        return lanGames;
    }
}
