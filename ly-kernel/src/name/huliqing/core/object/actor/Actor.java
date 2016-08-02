/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actor;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Quaternion;
import name.huliqing.core.data.ActorData;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;
import name.huliqing.core.object.DataProcessor;
import name.huliqing.core.object.action.ActionProcessor;
import name.huliqing.core.object.channel.ChannelProcessor;
import name.huliqing.core.object.chat.Chat;
import name.huliqing.core.object.resist.ResistProcessor;
import name.huliqing.core.object.state.StateProcessor;
import name.huliqing.core.object.skill.SkillProcessor;
import name.huliqing.core.object.talent.TalentProcessor;
import name.huliqing.core.object.task.Task;
import name.huliqing.core.object.actorlogic.ActorLogicProcessor;

/**
 * 定义角色的行为
 * @author huliqing
 */
public interface Actor extends DataProcessor<ActorData> {
    
    /**
     * 设置角色质量
     * @param mass 
     */
    void setMass(float mass);
    
    /**
     * 获取角色质量
     * @return 
     */
    float getMass();
    
    /**
     * 获取角色的模型信息。
     * @return 
     */
    Spatial getModel();
    
    /**
     * 获取逻辑管理器，该方法只允许service层调用
     * @return 
     */
    ActorLogicProcessor getLogicProcessor();
    
    void setLogicProcessor(ActorLogicProcessor logicProcessor);
    
    /**
     * 角色行为控制器
     * @return 
     */
    ActionProcessor getActionProcessor();
    
    void setActionProcessor(ActionProcessor actionProcessor);
    
    /**
     * 获取技能控制器,该方法只允许service层调用
     * @return 
     */
    SkillProcessor getSkillProcessor();
    
    void setSkillProcessor(SkillProcessor skillProcessor);
    
    /**
     * 获取角色的状态处理器,该方法只允许service层调用
     * @return 
     */
    StateProcessor getStateProcessor();
    
    void setStateProcessor(StateProcessor stateProcessor);
    
    /**
     * 获取抗性处理器,该方法只允许service层调用
     * @return 
     */
    ResistProcessor getResistProcessor();
    
    void setResistProcessor(ResistProcessor resistProcessor);
    
    /**
     * 获取角色动画通道处理器
     * @return 
     */
    ChannelProcessor getChannelProcessor();
    
    /**
     * 设置角色动画通道处理器
     * @param animChannelProcessor 
     */
    void setChannelProcessor(ChannelProcessor animChannelProcessor);
    
    /**
     * 获取天赋处理器
     * @return 
     */
    TalentProcessor getTalentProcessor();
    
    /**
     * 设置天赋处理器
     * @param talentProcessor 
     */
    void setTalentProcessor(TalentProcessor talentProcessor);
    
    /**
     * 设置角色的位置
     * @param location 
     */
    void setLocation(Vector3f location);
    
    Vector3f getLocation();
    
    /**
     * 设置角色的转换方向
     * @param quaternion 
     */
    void setRotation(Quaternion quaternion);
    
    /**
     * 设置角色的移动方向
     * @return 
     */
    Vector3f getWalkDirection();
    
    /**
     * 设置角色的当前行动方向,注意是行动方向，不是目标位置。
     * @param walkDirection 
     */
    void setWalkDirection(Vector3f walkDirection);
    
    Vector3f getViewDirection();
    
    /**
     * 设置角色的当前视角方向。
     * @param viewDirection 
     */
    void setViewDirection(Vector3f viewDirection);
     
    /**
     * 获取目标位置与当前角色的正前方的夹角度数.简单的说,即当前角色要转多少度才能正视
     * 到position位,返回值为角度,不是弧度
     * @param position
     * @return 返回值为角度,不是弧度
     */
    float getViewAngle(Vector3f position);
    
    /**
     * 判断角色是否已经死亡。
     * @return 
     */
    boolean isDead();
    
    /**
     * 复活
     */
    void reborn();
    
    /**
     * 获取角色与目标位置的距离。
     * @param position
     * @return 
     */
    float getDistance(Vector3f position);
    
    /**
     * 获取角色与目标角色的距离
     * @param target
     * @return 
     */
    float getDistance(Actor target);
    
    /**
     * 与目标的距离（平方）
     * @param target
     * @return 
     */
    float getDistanceSquared(Actor target);
    
    /**
     * 获取角色周围一定范围内的所有角色，包含自己，并且不分敌我、死活。
     * @param distance
     * @param store 
     */
    void getNearActor(float distance, List<Actor> store);
    
    void setJumpForce(Vector3f force);
    
    void jump();
    
    /**
     * 标记为玩家控制的角色.
     * @param player 
     */
    void setPlayer(boolean player);
    
    /**
     * 判断是否为玩家控制的角色.
     * @return 
     */
    boolean isPlayer();
    
    /**
     * 获取角色侦听器
     * @return 
     */
    List<ActorListener> getActorListeners();
    
    /**
     * 获取所有skillListener
     * @return 
     */
    List<SkillListener> getSkillListeners();
    
    /**
     * 添加一个物理事件侦听器
     * @return 
     */
    void addActorPhysicsListener(PhysicsListener apl);
    
    /**
     * 删除一个物理事件侦听器
     * @param apl 
     */
    boolean removeActorPhysicsListener(PhysicsListener apl);
    
    void addItemListener(ItemListener talentListener);
    
    boolean removeItemListener(ItemListener talentListener);
    
    List<ItemListener> getItemListeners();
    
    void addSkinListener(SkinListener skinListener);
    
    boolean removeSkinListener(SkinListener skinListener);
    
    /**
     * 获取Skin侦听器
     * @return 
     */
    List<SkinListener> getSkinListeners();
    
    /**
     * 添加天赋侦听器
     * @param talentListener 
     */
    void addTalentListener(TalentListener talentListener);
    
    /**
     * 移除天赋侦听器
     * @param talentListener
     * @return 
     */
    boolean removeTalentListener(TalentListener talentListener);
    
    /**
     * 获取天赋侦听器
     * @return 
     */
    List<TalentListener> getTalentListeners();
    
    /**
     * 添加任务侦听器
     * @param taskListener 
     */
    void addTaskListener(TaskListener taskListener);
    
    /**
     * 移除任务侦听器
     * @param taskListener 
     */
    boolean removeTaskListener(TaskListener taskListener);
    
    /**
     * 获得任务侦听列表
     * @return 
     */
    List<TaskListener> getTaskListeners();
    
    /**
     * 获取角色的状态监听器,如果不存在任何状态监听器则返回null。
     * @return 
     */
    List<StateListener> getStateListeners();
    
    /**
     * 设置状态监听器
     * @param stateListeners 
     */
    void setStateListeners(List<StateListener> stateListeners);
    
    /**
     * 当前角色是否正在防守状态,也就是查询是否正在执行防守技能
     * @return 
     */
    boolean isDefending();
    
    /**
     * 是否正在闪避
     * @return 
     */
    boolean isDucking();
    
    /**
     * 判断角色当前是否正在执行“攻击”技能.
     * @return 
     */
    boolean isAttacking();
    
    /**
     * 判断角色是否正在“跑步”中
     * @return 
     */
    boolean isRunning();
    
    /**
     * 角色是否正处于等待状态
     * @return 
     */
    boolean isWaiting();
    
    /**
     * 
     * 判断目标是否是敌对方
     * @param target 目标角色
     * @return 
     */
    boolean isEnemy(Actor target);
   
    /**
     * 设置角色的正视角方向，如果角色的默认正视角不是(0,0,1)，则需要重新指定
     * 该方法。
     * @param vec 
     */
    void setLocalForward(Vector3f vec);
    
    /**
     * 是否激活逻辑
     * @param enabled 
     */
    void setEnabled(boolean enabled);
    
    boolean isEnabled();
    
    /**
     * 让角色看向目标。
     * @param position 目标位置
     */
    void faceTo(Vector3f position);
    
    /**
     * 获取角色当前所在的物理空间,如果没有或不支持则返回null.
     * @return 
     */
    PhysicsSpace getPhysicsSpace();
    
    void setKinematic(boolean kinematic);
    
    boolean isKinematic();
    
    /**
     * 获取角色的对话面板
     * @return 
     */
    Chat getChat();
    
    /**
     * 设置角色的对话面板
     * @param chat 
     */
    void setChat(Chat chat);
    
    /**
     * 清理并释放资源
     */
    void cleanup();
    
    /**
     * 获取角色的任务列表
     * @return 
     */
    List<Task> getTasks();
}
