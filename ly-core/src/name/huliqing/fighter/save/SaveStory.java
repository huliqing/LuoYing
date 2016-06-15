/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.save;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.IOException;
import java.util.ArrayList;
import name.huliqing.fighter.data.ActorData;

/**
 * 关卡存档器
 * @author huliqing
 */
public class SaveStory implements Savable{
    
    private String saveName;
    
    private long saveTime;
    
    // 已经完成的故事关卡ID
    private int storyCount;
    
    // 玩家数据
    private ActorData player;
    
    private ArrayList<ShortcutSave> shortcuts = new ArrayList<ShortcutSave>();
    
    // 其它角色,这些角色可能包含其它玩家，宠物等
    private ArrayList<ActorData> actors = new ArrayList<ActorData>();

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public int getStoryCount() {
        return storyCount;
    }

    public void setStoryCount(int storyCount) {
        this.storyCount = storyCount;
    }

    public ActorData getPlayer() {
        return player;
    }

    public void setPlayer(ActorData player) {
        this.player = player;
    }

    public ArrayList<ShortcutSave> getShortcuts() {
        return shortcuts;
    }

    public void setShortcuts(ArrayList<ShortcutSave> shortcuts) {
        this.shortcuts = shortcuts;
    }

    public ArrayList<ActorData> getActors() {
        return actors;
    }

    public void setActors(ArrayList<ActorData> actors) {
        this.actors = actors;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(saveName, "saveName", null);
        oc.write(saveTime, "saveTime", 0);
        oc.write(storyCount, "storyCount", 0);
        oc.write(player, "player", null);
        oc.writeSavableArrayList(shortcuts, "shortcuts", null);
        oc.writeSavableArrayList(actors, "actors", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        saveName = ic.readString("saveName", "unknow");
        saveTime = ic.readLong("saveTime", 0);
        storyCount = ic.readInt("storyCount", 0);
        player = (ActorData) ic.readSavable("player", null);
        ArrayList<ShortcutSave> tempShortcuts = ic.readSavableArrayList("shortcuts", null);
        shortcuts.clear();
        if (tempShortcuts != null) {
            shortcuts.addAll(tempShortcuts);
        }
        ArrayList<ActorData> tempActors = ic.readSavableArrayList("actors", null);
        actors.clear();
        if (tempActors != null) {
            actors.addAll(tempActors);
        }
    }
    
}
