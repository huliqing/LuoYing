/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 * @author huliqing
 */
@Serializable
public class SlotData extends ProtoData{
    
    // 武器所绑定的骨头
    private String bindBone;
    
    // 武器挂起时的本地变换
    private Vector3f localTranslation;
    
    // 武器挂起时的本地旋转
    private float[] localRotation;
    
    // 武器挂起时的本地缩放
    private Vector3f localScale;
    
    // 绑定一个左手拿该槽位武器的Skin类型的技能
    private String leftHandSkinSkill;
    
    // 绑定一个右手拿该槽位武器的Skin类型的技能
    private String rightHandSkinSkill;
    
    public SlotData() {}
    
    public SlotData(String id) {
        super(id);
    }

    /**
     * 武器所绑定的骨头
     * @return 
     */
    public String getBindBone() {
        return bindBone;
    }

    public void setBindBone(String bindBone) {
        this.bindBone = bindBone;
    }

    public Vector3f getLocalTranslation() {
        return localTranslation;
    }

    public void setLocalTranslation(Vector3f localTranslation) {
        this.localTranslation = localTranslation;
    }

    public float[] getLocalRotation() {
        return localRotation;
    }

    public void setLocalRotation(float[] localRotation) {
        this.localRotation = localRotation;
    }

    public Vector3f getLocalScale() {
        return localScale;
    }

    public void setLocalScale(Vector3f localScale) {
        this.localScale = localScale;
    }

    public String getLeftHandSkinSkill() {
        return leftHandSkinSkill;
    }

    public void setLeftHandSkinSkill(String leftHandSkinSkill) {
        this.leftHandSkinSkill = leftHandSkinSkill;
    }

    public String getRightHandSkinSkill() {
        return rightHandSkinSkill;
    }

    public void setRightHandSkinSkill(String rightHandSkinSkill) {
        this.rightHandSkinSkill = rightHandSkinSkill;
    }
   
}
