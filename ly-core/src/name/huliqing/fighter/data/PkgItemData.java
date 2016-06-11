/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import name.huliqing.fighter.enums.Sex;
import name.huliqing.fighter.utils.ConvertUtils;

/**
 * 简单的物品，主要定义一些可放在角色包裹中的物品，这些物品使用的时候可能需要
 * 判断是否符合种族及性别
 * @author huliqing
 */
@Serializable
public class PkgItemData extends ProtoData {
    
    // 种族限制
    private List<String> raceLimit;
    
    // 性别限制
    private Sex sexLimit;
    
    // 是否可删除的
    private boolean deletable = true;

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        if (raceLimit != null) 
            oc.write(raceLimit.toArray(new String[]{}), "raceLimit", null);
        oc.write(sexLimit, "sexLimit", null);
        oc.write(deletable, "deletable", true);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im); 
        InputCapsule ic = im.getCapsule(this);
        raceLimit = ConvertUtils.toList(ic.readStringArray("raceLimit", null));
        sexLimit = ic.readEnum("sexLimit", Sex.class, null);
        deletable = ic.readBoolean("deletable", true);
    }
    
    public PkgItemData() {}
    
    public PkgItemData(String id) {
        super(id);
    }
    
    /**
     * 获取物品使用的种族限制，如果该列表为empty,则表明没有种族限制.否则
     * 指定的角色的种族必须符合该列表中的限制。
     * @return 返回种族限制，不会返回null,但可能返回empty
     */
    public List<String> getRaceLimit() {
        if (raceLimit == null) {
//            return Collections.EMPTY_LIST; // EMPTY_LIST是内部类，这会造成在序列化的时候异常
            return new ArrayList<String>(0);
        }
        return raceLimit;
    }

    /**
     * 设置物品使用的种族限制
     * @param raceLimit 
     */
    public void setRaceLimit(List<String> raceLimit) {
        this.raceLimit = raceLimit;
    }

    /**
     * 获取性别限制，如果该方法返回null,则表示无性别限制。
     * @return 
     */
    public Sex getSexLimit() {
        return sexLimit;
    }

    /**
     * 设置性别限制
     * @param sexLimit 
     */
    public void setSexLimit(Sex sexLimit) {
        this.sexLimit = sexLimit;
    }

    /**
     * 判断物品是否是可删除的 
     * @return 
     */
    public boolean isDeletable() {
        return deletable;
    }

    /**
     * 设置物品是否是可删除的
     * @param deletable 
     */
    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }
    
}
