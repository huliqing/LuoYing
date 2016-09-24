/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.define;

import name.huliqing.core.object.Loader;

/**
 * 
 * @author huliqing
 */
public class DefineFactory {
    private final static String SKILL_TAG_DEFINE_ID = "defineSkillTag";
    
    private static SkillTagDefine skillTagDefine;
    
    /**
     * 获取技能标记定义
     * @return 
     */
    public static SkillTagDefine getSkillTagDefine() {
        if (skillTagDefine == null) {
            skillTagDefine = Loader.load(SKILL_TAG_DEFINE_ID);
            if (skillTagDefine == null) {
                throw new NullPointerException("Could not find skillTag define by id: " + SKILL_TAG_DEFINE_ID);
            }
        }
        return skillTagDefine;
    }
}
