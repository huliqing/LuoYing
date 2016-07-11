/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.action.impl;

import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.object.action.AbstractAction;
import name.huliqing.fighter.object.action.RunAction;
import name.huliqing.fighter.data.ActionData;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.enums.SkillType;
import name.huliqing.fighter.game.network.SkillNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.SkillService;

/**
 *
 * @author huliqing
 */
public class RunSimpleAction extends AbstractAction implements RunAction{
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);

    // 需要走到的目标地址
    private Vector3f position = new Vector3f();
    // 允许的最近距离，不能太小，太小会产生两个现象,特别是在侦率较低时容易出现。
    // 1.当角色瞬间太快穿过目标路径点时，可能一直走下去。
    // 2.角色可能在路径起点时的ViewDirection方向不正确，表现为瞬间前后转向。
    // 主要是由于新路径的第一个点可能产生在了角色身后而导致方向错误。
    private float nearest = 0.5f;
    
    // 避障
    private Detour rayDetour = new RayDetour(this);
    private Detour timeDetour = new TimeDetour(this);
    
    private boolean resetDir;
    
    // 缓存技能id
    private String runSkillId;
    private String waitSkillId;
    
    public RunSimpleAction() {
        super();
    }
    
    @Override
    protected void doInit() {
        resetDir = true;
        rayDetour.setActor(actor);
        rayDetour.setAutoFacing(true);
        timeDetour.setActor(actor);
        timeDetour.setAutoFacing(true);
        
        SkillData runSkill = skillService.getSkill(actor, SkillType.run);
        if (runSkill != null) {
            runSkillId = runSkill.getId();
        }
        SkillData waitSkill = skillService.getSkill(actor, SkillType.wait);
        if (waitSkill != null) {
            waitSkillId = waitSkill.getId();
        }
    }

    @Override
    public void cleanup() {
        rayDetour.cleanup();
        timeDetour.cleanup();
        super.cleanup(); 
    }
    
    @Override
    public void doLogic(float tpf) {
        // 如果角色是不可移动的，则直接返回不处理逻辑
        if (!actorService.isMoveable(actor) || runSkillId == null) {
            end();
            return;
        }
        
        if (resetDir) {
            TempVars tv = TempVars.get();
            skillNetwork.playWalk(actor, runSkillId, position.subtract(actor.getModel().getWorldTranslation(), tv.vect1), true, false);
            tv.release();
            resetDir = false;
        }
        
        // == 确定已经走到目标位置
        float distance = actor.getDistance(position);
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
        if (!actor.isRunning()) {
            TempVars tv = TempVars.get();
            skillNetwork.playWalk(actor, runSkillId, position.subtract(actor.getModel().getWorldTranslation(), tv.vect1), true, false);
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
        return actor.getDistance(pos) <= nearest;
    }

    @Override
    public boolean isEndPosition() {
        return isInPosition(position);
    }

}
