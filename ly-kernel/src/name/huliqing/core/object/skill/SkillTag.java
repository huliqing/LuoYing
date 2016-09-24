/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

/**
 * 包装技能标记。
 * @author huliqing
 */
public class SkillTag {
    
    private final int index;
    private final String name;
    
    public SkillTag(int index, String tag) {
        this.index = index;
        this.name = tag;
    }
    
    /**
     * 获取技能标记的索引值
     * @return 
     */
    public final int index() {
        return index;
    }
    
    /**
     * <code>
     * 1L << index
     * </code>
     * @return 
     */
    public final long indexAsBinary() {
        // 1必须为long
        return 1L << index;
    }

    /**
     * 获取技能标记的名称
     * @return 
     */
    public final String name() {
        return name;
    }

    @Override
    public String toString() {
        return "skillTagIndex=" + index + ", skillTagName=" + name; 
    }
    
    
}
