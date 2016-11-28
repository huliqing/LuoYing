/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.define;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.data.DefineData;
import name.huliqing.luoying.object.skill.SkillType;

/**
 * 技能类型的定义
 * @author huliqing
 */
public class SkillTypeDefine extends Define {
    private static final Logger LOG = Logger.getLogger(SkillTypeDefine.class.getName());
    
    /**
     * 技能标记列表,这个列表是有序的,并且在载入后不在运行时改变
     */
    private final List<SkillType> typeList = new ArrayList<SkillType>();

    @Override
    public void setData(DefineData data) {
        super.setData(data);
        String[] types = data.getAsArray("skillTypes");
        if (types != null && types.length > 0) {
            for (String type : types) {
                registerSkillType(type);
            }
        } else {
            LOG.log(Level.WARNING, "skillTypes undefined.");
        }
    }
    
    /**
     * 查询当前已经注册的skillType的数量
     * @return 
     */
    public final int size() {
        return typeList.size();
    }
    
    /**
     * 通过技能标记获取SkillType
     * @param skillType
     * @return 
     */
    public SkillType getSkillType(String skillType) {
        SkillType st = getSkillTypeInner(skillType);
        if (st != null) {
            return st;
        }
        return null;
    }
    
    /**
     * 转化所有skillType为一个整型，所有整型中每个二进制(1)位代表一个skillType类型, 如果skillTypes为null则返回0.
     * @param skillTypes
     * @return 
     */
    public long convert(String... skillTypes) {
        long result = 0;
        if (skillTypes != null) {
            SkillType st;
            for (String type : skillTypes) {
                st = getSkillTypeInner(type);
                if (st == null) {
                    continue;
                }
                result |= st.indexAsBinary();
            }            
        }
        return result;
    }
 
    /**
     * 注册、登记一个技能类型标记,这个注册应该放在系统初始化的时候进行。
     * @param skillType 
     */
    public synchronized void registerSkillType(String skillType) {
        SkillType st = getSkillTypeInner(skillType);
        if (st != null) {
            LOG.log(Level.WARNING, "Could not register skill type,  skillType already exists! skillType={0}", st);
            return;
        }
        if (size() >= 64) {
            LOG.log(Level.WARNING
                    , "Could not register skill type, the size of skillType could not more than 64! skillType={0}, current size={1}"
                    , new Object[] {skillType, size()});
            return;
        }
        typeList.add(new SkillType(typeList.size(), skillType));
    }
    
    /**
     * 清理所有技能标记
     */
    public synchronized void clear() {
        typeList.clear();
    }
    
    private SkillType getSkillTypeInner(String skillType) {
        for (SkillType st : typeList) {
            if (st.name().equals(skillType)) {
                return st;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return typeList.toString();
    }
    
    /**
     * @param types
     * @return 
     */
    public String toString(long types) {
        List<String> temp = new ArrayList<String>();
        for (int i = 0; i < size(); i++) {
            if ((types & 1 << i) != 0) {
                temp.add(typeList.get(i).name());
            }
        }
        return temp.toString();
    }
}
