/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.network.Network;
import com.jme3.math.Vector3f;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.SkillConstants;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.mess.MessSkillPlay;
import name.huliqing.luoying.mess.MessSkillWalk;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.skill.Skill;

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
    
    /**
     * 这个方法不支持Network版本
     * @param actor
     * @param skill
     * @param force
     */
    @Override
    public boolean playSkill(Entity actor, Skill skill, boolean force) {
        if (skill == null)
            return false;
        
           // remove20161004,注：不能在这里将playService.getTarget的目标对象设置给actor,因为actor可能是游戏中的npc,不能使用当前游戏的主目标
           // 当前游戏的主目标只适用于当前“玩家”
//        actorNetwork.setTarget(actor, playService.getTarget());
        
        MessSkillPlay mess = new MessSkillPlay();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setSkillId(skill.getData().getId());
        mess.setSyncRandomIndex(skill.getData().getRandomIndex());
        
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
            return false;
        }

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
        
        SkillModule skillModule = actor.getModuleManager().getModule(SkillModule.class);
        if (force || skillModule.checkStateCode(skill) == SkillConstants.STATE_OK) {
            
            NETWORK.broadcast(mess);
            
            return skillService.playSkill(skillModule, skill, true);
        }
        return false;
    }

    @Override
    public boolean playWalk(Entity actor, Skill walkSkill, Vector3f dir, boolean faceToDir, boolean force) {
        MessSkillWalk mess = new MessSkillWalk();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setDir(dir);
        mess.setFace(faceToDir);
        mess.setSkillId(walkSkill.getData().getId());
        
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
            return false;
        }
        
        // ============================20160504=============================
        // 重要：参考上面playSkill中的说明。
        // ============================20160504=============================
        if (force || skillService.isPlayable(actor, walkSkill)) {
            if (NETWORK.hasConnections()) {
                NETWORK.broadcast(mess);
            }
            return skillService.playWalk(actor, walkSkill, dir, faceToDir, true);
        }
        
        return false;
    }
    
   
    
}
