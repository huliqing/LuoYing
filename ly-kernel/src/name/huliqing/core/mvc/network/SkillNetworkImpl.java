/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import name.huliqing.core.network.Network;
import com.jme3.math.Vector3f;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.mess.MessActorAddSkill;
import name.huliqing.core.mess.MessSkill;
import name.huliqing.core.mess.MessSkillWalk;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.SkillListener;
import name.huliqing.core.object.module.SkillPlayListener;
import name.huliqing.core.object.skill.Skill;

/**
 *
 * @author huliqing
 */
public class SkillNetworkImpl implements SkillNetwork {
    private final static Network NETWORK = Network.getInstance();
    private SkillService skillService;
    
    @Override
    public void inject() {
        skillService = Factory.get(SkillService.class);
    }

    @Override
    public Skill loadSkill(String skillId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Skill loadSkill(SkillData skillData) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void addSkill(Actor actor, String skillId) {
        if (!NETWORK.isClient()) {
            // 1.broadcast to all client
            MessActorAddSkill mess = new MessActorAddSkill();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setSkillId(skillId);
            NETWORK.broadcast(mess);
            
            // 1.addskill
            skillService.addSkill(actor, skillId);
        }
    }
    
    /**
     * 这个方法不支持Network版本
     * @param actor
     * @param skill
     * @param force
     * @return 
     */
    @Override
    public boolean playSkill(Actor actor, Skill skill, boolean force) {
        boolean result = false;
        if (!NETWORK.isClient()) {
            
            // ============================20160504=============================
            // 重要：所有技能的执行顺序是这样的：
            // 1.先在服务端判断是否可以执行，如果不能则直接返回false，否则按顺序执行以下2和3
            // 2.广播到所有客户端进行“强制”执行该技能。
            // 3.在服务端“强制”执行该技能
            
            // 重要：注意顺序不要颠倒，并且只要在服务端判断可以执行之后，
            // 客户端和服务端在实际执行的时候将使用“强制”方式，因为已经在1步中判断
            // 为可执行，所以在广播到客户端和服务端再执行的时候就不再需要判断了。
            
            // -----------------------------------------------------------------
            // 非常重要的原因说明：
            // 1.为什么要先在服务端判断，然后在客户端和服务端再“强制执行”?
            // 2.为什么不直接广播到客户端和在服务端直接执行技能?
            // 这里有一个特别重要的原因，由于服务端和各个客户端的网络延迟各不相同，
            // 无法保证服务端和各个客户端的状态完全一致，这会导致在服务端可以执
            // 行的技能，却无法保证在每个客户端也都能正常执行。
            // 例如：服务端角色A的技能在3码内才能攻击到角色B，
            // 当服务端的角色A到了3码内之后可以执行攻击技能了，
            // 但是由于网络延迟的原因却无法保证所有客户端的相同角色A也都在距离B的
            // 3码范围内，这个时候如果直接广播攻击命令到所有客户端就有可能有些
            // 客户端会无法执行攻击技能,这就会产生许多奇怪的现象，
            // 比如服务端已经攻击完停止了，但是客户端还在跑动，或者客户端会“跳”
            // 到天上去的奇怪现象,虽然这不会影响服务端和客户端的数据同步，
            // 只会造成角色的位置和动画不同步问题，但是在视觉上会带来很糟糕的体验！
            
            // 如果在服务端判断之后再广播到所有客户端以及当前服务端进行强制执行，
            // 就不会造成这种问题。
            
            // 关于服务端和客户端的状态同步，受各种不确定因素的影响太多,保证服务
            // 端和客户端所有状态完全同步几乎是不可能的。
            // ============================20160504=============================
            
            result = skillService.isPlayable(actor, skill, force);
            
            if (result) {
                if (NETWORK.hasConnections()) {
                    MessSkill mess = new MessSkill();
                    mess.setActorId(actor.getData().getUniqueId());
                    mess.setSkillId(skill.getData().getId());
                    mess.setForce(true); // 注：强制执行
                    NETWORK.broadcast(mess);
                }
                skillService.playSkill(actor, skill, true);// 注：强制执行
            }
        }
        return result;
    }
    
    @Override
    public boolean playSkill(Actor actor, String skillId, boolean force) {
        Skill skill = skillService.getSkill(actor, skillId);
        return playSkill(actor, skill, force);
    }

    @Override
    public boolean playWalk(Actor actor, String skillId, Vector3f dir, boolean faceToDir, boolean force) {
        boolean result = false;
        if (!NETWORK.isClient()) {
            
            // ============================20160504=============================
            // 重要：参考上面playSkill中的说明。
            // ============================20160504=============================
            Skill skill = skillService.getSkill(actor, skillId);
            result = skillService.isPlayable(actor, skill, force);
            if (result) {
                if (NETWORK.hasConnections()) {
                    MessSkillWalk mess = new MessSkillWalk();
                    mess.setActorId(actor.getData().getUniqueId());
                    mess.setDir(dir);
                    mess.setFace(faceToDir);
                    mess.setForce(true); // 注：强制执行
                    mess.setSkillId(skillId);
                    NETWORK.broadcast(mess);
                }
                skillService.playWalk(actor, skillId, dir, faceToDir, true); // 注：强制执行
            }
        }
        return result;
    }
    
    @Override
    public boolean isPlayable(Actor actor, Skill skill, boolean force) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int checkStateCode(Actor actor, Skill skill, boolean force) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCooldown(Skill skill) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isSkillLearned(Actor actor, String skillId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isPlayingSkill(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isPlayingSkill(Actor actor, SkillType skillType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWaiting(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRunning(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    @Override
    public boolean isAttacking(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isDefending(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isDucking(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Skill getPlayingSkill(Actor actor, SkillType skillType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getPlayingSkillStates(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void lockSkill(Actor actor, SkillType... skillType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void lockSkillAll(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unlockSkill(Actor actor, SkillType... skillType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unlockSkillAll(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isLocked(Actor actor, SkillType skillType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void lockSkillChannels(Actor actor, String... channels) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unlockSkillChannels(Actor actor, String... channels) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addSkillListener(Actor actor, SkillListener skillListener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void addSkillPlayListener(Actor actor, SkillPlayListener skillPlayListener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeSkillListener(Actor actor, SkillListener skillListener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeSkillPlayListener(Actor actor, SkillPlayListener skillPlayListener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeSkill(Actor actor, String skillId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Skill getSkill(Actor actor, String skillId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Skill getSkill(Actor actor, SkillType skillType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Skill> getSkills(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
