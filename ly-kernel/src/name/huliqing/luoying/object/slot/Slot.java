/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.slot;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.data.SlotData;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 武器槽位定义,武器槽位可以用于收起武器，一个武器槽位可以存放不同类型的武器。
 * 根据武器类型的不同，槽位可能绑定不同的技能(SkinSkill)来处理“取出”和“收起”的动画。
 * @author huliqing
 */
public class Slot implements DataProcessor<SlotData> {

    protected SlotData data;
    
    // 武器所绑定的骨头
    private String bindBone;
    
    // 武器挂起时的本地变换
    private Vector3f localLocation;
    
    // 武器挂起时的本地旋转
    private Quaternion localRotation;
    
    // 武器挂起时的本地缩放
    private Vector3f localScale;
    
    private HangSkillWrap[] hangSkills;
    
    @Override
    public void setData(SlotData data) {
        this.data = data;
        bindBone = data.getAsString("bindBone");
        localLocation = data.getAsVector3f("localLocation");
        localRotation = data.getAsQuaternion("localRotation");
        localScale = data.getAsVector3f("localScale");
        
        String[] tempHangSkills = data.getAsArray("hangSkills");
        if (tempHangSkills != null && tempHangSkills.length > 0) {
            hangSkills = new HangSkillWrap[tempHangSkills.length];
            for (int i = 0; i < tempHangSkills.length; i++) {
                String[] hsArr = tempHangSkills[i].split("\\|");
                HangSkillWrap hs = new HangSkillWrap();
                hs.weaponType = hsArr[0];
                hs.skillId = hsArr[1];
                hangSkills[i] = hs;
            }
        }
    }

    @Override
    public SlotData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }
    
     /**
     * 武器所绑定的骨头
     * @return 
     */
    public String getBindBone() {
        return bindBone;
    }

    public Vector3f getLocalTranslation() {
        return localLocation;
    }

    public Quaternion getLocalRotation() {
        return localRotation;
    }

    public Vector3f getLocalScale() {
        return localScale;
    }
    
    /**
     * 通过指定的技能类型获取"绑定的悬挂技能(skinSkill)", 如果不存在指定的设置或找不到适合的技能则返回null.
     * @param weaponType
     * @return 
     */
    public String getHangSkill(String weaponType) {
        if (hangSkills == null)
            return null;
        
        for (HangSkillWrap hs : hangSkills) {
            if ((hs.weaponType.equals(weaponType))) {
                return hs.skillId;
            }
        }
        return null;
    }
    
    private class HangSkillWrap {
        String weaponType;
        String skillId;
    }
    
}
