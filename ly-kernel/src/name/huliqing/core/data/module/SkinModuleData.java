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
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.SkinData;

/**
 *
 * @author huliqing
 */
@Serializable
public class SkinModuleData extends ModuleData {
    
//    private List<SkinData> skinBase;
//    private List<SkinData> skinDatas;

//    /**
//     * 获取角色的基本皮肤
//     * @return 
//     * @see #setSkinBase(java.util.List) 
//     */
//    public List<SkinData> getSkinBase() {
//        return skinBase;
//    }
//
//    /**
//     * 角色的基本皮肤，基本皮肤是用来在切换装备后“修补皮肤”缺失的问题。比如当
//     * 穿上一套上下连身的法袍（同时包含lowerBody和upperBody）后，再使用一件只
//     * 包含upperBody的装备来换上时，由于法袍被替换，这时角色身上将丢失lowerBody
//     * 的装备，这时就需要从skinBase中查看是否有lowerBody的基本皮肤来补上，否则角
//     * 色会缺少下半身的装备模型。
//     * 注意：基本皮肤中不要指定哪些包含两个或两个以上type部位的皮肤。比如：
//     * 同时包含lowerBody和upperBody的skin,如上下连身的套装之类的皮肤，这类皮肤
//     * 会造成在换装备后进行修补的时候又替换掉已经穿上的装备。
//     * @param skinBase 
//     */
//    public void setSkinBase(List<SkinData> skinBase) {
//        this.skinBase = skinBase;
//    }
//
//    /**
//     * 角色所有的装备,包含包裹中的装备
//     * @return 
//     */
//    public List<SkinData> getSkinDatas() {
//        return skinDatas;
//    }
//
//    /**
//     * 设置角色的装备,包含包裹中的装备
//     * @param skinDatas 
//     */
//    public void setSkinDatas(List<SkinData> skinDatas) {
//        this.skinDatas = skinDatas;
//    }
//        
//    @Override
//    public void write(JmeExporter ex) throws IOException {
//        super.write(ex);
//        OutputCapsule oc = ex.getCapsule(this);
//        if (skinBase != null) {
//            oc.writeSavableArrayList(new ArrayList<SkinData>(skinBase), "skinBase", null);
//        }
//        if (skinDatas != null) {
//            oc.writeSavableArrayList(new ArrayList<SkinData>(skinDatas), "skinDatas", null);
//        }
//    }
//
//    @Override
//    public void read(JmeImporter im) throws IOException {
//        super.read(im);
//        InputCapsule ic = im.getCapsule(this);
//        skinBase = ic.readSavableArrayList("skinBase", null);
//        skinDatas = ic.readSavableArrayList("skinDatas", null);
//    }
    
    
}
