/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.action;

import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import name.huliqing.core.Factory;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.skill.Skill;

/**
 *
 * @author huliqing
 */
public class RunSimpleAction extends AbstractAction implements RunAction{
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);

    // 需要走到的目标地址
    private final Vector3f position = new Vector3f();
    // 允许的最近距离，不能太小，太小会产生两个现象,特别是在侦率较低时容易出现。
    // 1.当角色瞬间太快穿过目标路径点时，可能一直走下去。
    // 2.角色可能在路径起点时的ViewDirection方向不正确，表现为瞬间前后转向。
    // 主要是由于新路径的第一个点可能产生在了角色身后而导致方向错误。
    private float nearest = 0.5f;
    
    // 避障
    private final Detour rayDetour = new RayDetour(this);
    private final Detour timeDetour = new TimeDetour(this);
    
    private boolean resetDir;
    
    // 缓存技能id
    private Skill runSkillId;
    private Skill waitSkillId;
    
    public RunSimpleAction() {
        super();
    }
    
    @Override
    public void initialize() {
        super.initialize();
        resetDir = true;
        rayDetour.setActor(actor);
        rayDetour.setAutoFacing(true);
        timeDetour.setActor(actor);
        timeDetour.setAutoFacing(true);
        
        runSkillId = skillService.getSkill(actor, SkillType.run);
        waitSkillId = skillService.getSkill(actor, SkillType.wait);
    }

    @Override
    public void cleanup() {
        rayDetour.cleanup();
        timeDetour.cleanup();
        super.cleanup(); 
    }
    
    @Override
    public void doLogic(float tpf) {
//        System.out.println("actor.position=" + actor.getSpatial().getWorldTranslation());
        
        // 如果角色是不可移动的，则直接返回不处理逻辑
        if (!actorService.isMoveable(actor) || runSkillId == null) {
            end();
            return;
        }
        
        if (resetDir) {
            TempVars tv = TempVars.get();
            skillNetwork.playWalk(actor, runSkillId.getData().getId()
                    , position.subtract(actor.getSpatial().getWorldTranslation(), tv.vect1), true, false);
            tv.release();
            resetDir = false;
        }
        
        // == 确定已经走到目标位置
        float distance = actor.getSpatial().getWorldTranslation().distance(position);
        if (distance <= nearest) {
            if (waitSkillId != null) {
                skillNetwork.playSkill(actor, waitSkillId, false);
            }
            end();
            return;
        }
        
        // 1.通过Ray偿试避开障碍，如果发现障碍，则让它偿试避开后再进行走动。
        if (rayDetour.detouring(tpf)) {
            resetDir = true;
            return;
        }
        
        // 2.通过时间检测偿试避开障碍
        if (timeDetour.detouring(tpf)) {
            resetDir = true;
            return;
        }
        
        runByStraight();
        
    }
    
    private void runByStraight() {
        if (!skillService.isRunning(actor)) {
            TempVars tv = TempVars.get();
            skillNetwork.playWalk(actor, runSkillId.getData().getId()
                    , position.subtract(actor.getSpatial().getWorldTranslation(), tv.vect1), true, false);
            tv.release();
        }
    }

    @Override
    public void setPosition(Vector3f position) {
        // 防止重复设置导致不停进行寻路
        if (this.position.distance(position) <= nearest) {
            return;
        }
        this.resetDir = true;
        this.position.set(position);
    }

    @Override
    public void setNearest(float nearest) {
        this.nearest = nearest;
        if (this.nearest < 0) {
            this.nearest = 0;
        }
    }

    @Override
    public boolean isInPosition(Vector3f pos) {
        if (actor == null) {
            return false;
        }
        return actor.getSpatial().getWorldTranslation().distance(pos) <= nearest;
    }

    @Override
    public boolean isEndPosition() {
        return isInPosition(position);
    }

}
