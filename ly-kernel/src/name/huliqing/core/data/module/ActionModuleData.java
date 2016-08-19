/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data.module;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;

/**
 * @author huliqing
 */
@Serializable
public class ActionModuleData extends ModuleData {

    // 角色的两个默认的行为，当角色处于玩家控制时需要这两个行为来执行打架和跑路
    // 如果这两个行为没有设置，则ActionService在必要时会为目标actor创建两个默认的行为
    // See ActionService.playFight,playRun
    private String actionDefFight;
    private String actionDefRun;

    /**
     * 获取角色默认的“战斗”行为的id
     *
     * @return
     */
    public String getActionDefFight() {
        return actionDefFight;
    }

    public void setActionDefFight(String actionDefFight) {
        this.actionDefFight = actionDefFight;
    }

    /**
     * 获取角色默认的“跑路”行为的id
     *
     * @return
     */
    public String getActionDefRun() {
        return actionDefRun;
    }

    public void setActionDefRun(String actionDefRun) {
        this.actionDefRun = actionDefRun;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(actionDefFight, "actionDefFight", null);
        oc.write(actionDefRun, "actionDefRun", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        actionDefFight = ic.readString("actionDefFight", null);
        actionDefRun = ic.readString("actionDefRun", null);
    }

}
