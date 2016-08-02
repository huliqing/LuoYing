/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.talent;

import java.util.List;

/**
 *
 * @author huliqing
 */
public interface TalentProcessor {

    /**
     * 增加一个天赋
     * @param talent 
     */
    void addTalent(Talent talent);
    
    /**
     * 移除一个天赋
     * @param talent 
     */
    boolean removeTalent(Talent talent);
    
    /**
     * 通过ID移除talent
     * @param talentId
     * @return 
     */
    boolean removeTalent(String talentId);
    
    /**
     * 获取指定ID的天赋
     * @param talentId
     * @return 
     */
    Talent getTalent(String talentId);

    /**
     * 获得当前的天赋列表
     * @return 
     */
    List<Talent> getTalents();
    
    /**
     * 更新天赋逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 清理
     */
    void cleanup();
}
