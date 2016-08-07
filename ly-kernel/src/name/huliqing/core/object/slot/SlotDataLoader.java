/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.slot;

import name.huliqing.core.xml.Proto;
import name.huliqing.core.data.SlotData;
import name.huliqing.core.xml.DataLoader;

/**
 *
 * @author huliqing
 */
public class SlotDataLoader implements DataLoader<SlotData> {

    @Override
    public void load(Proto proto, SlotData data) {
        data.setBindBone(proto.getAttribute("bindBone"));
        data.setLocalTranslation(proto.getAsVector3f("localTranslation"));
        data.setLocalRotation(proto.getAsFloatArray("localRotation"));
        data.setLocalScale(proto.getAsVector3f("localScale"));
        data.setLeftHandSkinSkill(proto.getAttribute("leftHandSkinSkill"));
        data.setRightHandSkinSkill(proto.getAttribute("rightHandSkinSkill"));
    }
    
}