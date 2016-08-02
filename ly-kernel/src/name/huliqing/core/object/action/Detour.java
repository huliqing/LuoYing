/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.action;

import com.jme3.math.Vector3f;
import name.huliqing.core.Factory;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.mvc.network.SkillNetwork;

/**
 * 实现障碍功能
 * @author huliqing
 */
public abstract class Detour {
    private final SkillService skillService = Factory.get(SkillService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    
    protected Action action;
    protected Actor actor;
    
    // 避碍时是否朝向目标
    protected boolean autoFacing;
    
    // 检测的时间间隔,单位秒
    protected float checkInterval = 1;
    protected float checkTimeUsed;
    
    // 是否正在绕道
    private boolean detouring;
    // 偿试绕道的次数
    private int count;
    
    // 当绕道时，指定要使用walk还是run,默认run
    private boolean useRun = true;
    
    private String runSkillId;
    private String walkSkillId;
    
    public Detour() {}
    
    public Detour(Action action) {
        this.action = action;
    }
    
    public void setAutoFacing(boolean autoFacing) {
        this.autoFacing = autoFacing;
    }
    
    public void setActor(Actor actor) {
        this.actor = actor;
        SkillData runSkill = skillService.getSkill(actor, SkillType.run);
        SkillData walkSkill = skillService.getSkill(actor, SkillType.walk);
        if (runSkill != null) {
            runSkillId = runSkill.getId();
        }
        if (walkSkill != null) {
            walkSkillId = walkSkill.getId();
        }
        
    }
    
    public boolean detouring(float tpf) {
        // 检测时间间隔
        checkTimeUsed += tpf;
        if (checkTimeUsed < checkInterval) {
            return false; // 重要，避免长时间锁定。
        }
        
        // 检测是否需要detour
        checkTimeUsed = 0;
        if (isNeedDetour()) {
            tryDetour(count);
            count++;
            detouring = true;
        } else {
            count = 0;
            detouring = false;
        }
        return detouring;
    }
    
    /**
     * 判断是否需要绕道
     * @return 
     */
    protected abstract boolean isNeedDetour();
    
    /**
     * 偿试处理绕道
     * @param count 偿试处理的次数,如果在一个位置一直无法绕开，则该值会一下增加
     *  每一次绕开之后会重置为0
     */
    protected abstract void tryDetour(int count);
    
    /**
     * 进行避障转向
     * @param dir 目标方向
     */
    protected void detour(Vector3f dir) {
        if (useRun) {
            if (runSkillId != null) {
                skillNetwork.playWalk(actor, runSkillId, dir, autoFacing, false);
            } else if (walkSkillId != null) {
                skillNetwork.playWalk(actor, walkSkillId, dir, autoFacing, false);
            }
        } else {
            if (walkSkillId != null) {
                skillNetwork.playWalk(actor, walkSkillId, dir, autoFacing, false);
            } else if (runSkillId != null) {
                skillNetwork.playWalk(actor, runSkillId, dir, autoFacing, false);
            }
        }
    }
    
    /**
     * 设置是否要使用run还是walk来避免障碍,默认run
     * @param useRun 
     */
    public void setUseRun(boolean useRun) {
        this.useRun = useRun;
    }
    
    /**
     * 清理
     */
    public void cleanup() {
        // do cleanup...
    }
}
