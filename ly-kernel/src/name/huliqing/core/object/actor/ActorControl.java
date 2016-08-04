/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actor;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import name.huliqing.core.object.state.StateProcessor;
import name.huliqing.core.object.action.ActionProcessor;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.SafeArrayList;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.core.LY;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ActorData;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.mvc.network.ActorNetwork;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.mvc.service.ActionService;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.mvc.service.StateService;
import name.huliqing.core.object.channel.ChannelProcessor;
import name.huliqing.core.object.chat.Chat;
import name.huliqing.core.object.resist.ResistProcessor;
import name.huliqing.core.object.skill.SkillProcessor;
import name.huliqing.core.object.talent.TalentProcessor;
import name.huliqing.core.object.task.Task;
import name.huliqing.core.object.actorlogic.ActorLogicProcessor;

/**
 * 基本的角色控制器，所有各种活动角色都继承自这里.所有的角色的Control都必须在
 * 构造函数中指定spatial
 * @author huliqing
 */
public class ActorControl extends BetterCharacterControl implements Actor, PhysicsCollisionListener{
    private static final Logger LOG = Logger.getLogger(ActorControl.class.getName());
    
    private final PlayService playService = Factory.get(PlayService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ActionService actionService = Factory.get(ActionService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    
    // 角色逻辑控制
    private ActorLogicProcessor logicProcessor;
    
    // 行为控制器
    private ActionProcessor actionProcessor;
    
    // 技能控制器
    private SkillProcessor skillProcessor;
    
    // 角色状态控制器
    private StateProcessor stateProcessor;
    
    // 角色的抗性处理器
    private ResistProcessor resistProcessor;
    
    // 角色动画通道处理器
    private ChannelProcessor channelProcessor;
    
    // 角色任务列表
    private List<Task> tasks;
    
    // 天赋处理器
    private TalentProcessor talentProcessor;
    
    // 角色数据
    private ActorData data;
    
    // 用于判断角色是否为玩家
    private boolean player;
    
    // 监听角色被目标锁定/释放,被击中,被杀死或杀死目标的侦听器
    private List<ActorListener> actorListeners;
    // 临听角色技能的执行和结束 
    private List<SkillListener> skillListeners;
    // 监听角色物品的增删
    private List<ItemListener> itemListeners;
    // 监听角色装备、武器等的穿脱
    private List<SkinListener> skinListeners;
    // 监听角色天赋的增加，删除，更新等
    private List<TalentListener> talentListeners;
    // 监听任务
    private List<TaskListener> taskListeners;
    // 状态监听
    private List<StateListener> stateListeners;
    
    // 临听当前角色与物体的碰撞
    private List<PhysicsListener> actorPhysicsListeners;
    // 判断是否添加了ActorPhysicsListener.
    private boolean physicsListenerAdded;
    
    // 角色的对话面板
    private Chat chat;
    
    /**
     * 不要直接使用该构造函数
     */
    public ActorControl() {}
    
//    public ActorControl(Spatial spatial, float radius, float height, float mass) {
//        this(spatial, radius, height, mass, null);
//    }
//    
//    public ActorControl(Spatial spatial, float radius, float height, float mass, CollisionShape shape) {
//        super(radius, height, mass);
//        if (shape != null) {
//            rigidBody.setCollisionShape(shape);
//        }
//        
//        // 1.添加control,并检测是否忘记设置USER_DATA
//        spatial.addControl(this);
//        data = spatial.getUserData(ProtoData.USER_DATA);
//        if (data == null) {
//            throw new NullPointerException("CharacterData not found! Set CharacterData to spatial's UserData first!");
//        }
//    }
    
    public void setModel(Spatial spatial, float radius, float height, float mass) {
        setModel(spatial, radius, height, mass, null);
    }
    
    public void setModel(Spatial spatial, float radius, float height, float mass, CollisionShape shape) {
        this.radius = radius;
        this.height = height;
        this.mass = mass;
        rigidBody = new PhysicsRigidBody(getShape(), mass);
        jumpForce.set(0, mass * 5, 0);
        rigidBody.setAngularFactor(0);
        
        if (shape != null) {
            rigidBody.setCollisionShape(shape);
        }
        
        // 1.添加control,并检测是否忘记设置USER_DATA
        spatial.addControl(this);
        spatial.setUserData(ObjectData.USER_DATA, data);
    }
    
    @Override
    public ActorData getData() {
        return data;
    }

    @Override
    public void setData(ActorData data) {
        this.data = data;
        ActorModelLoader.loadActorModel(this.data, this);
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        // 角色死亡后可能部分技能还没有结束，如死亡动画，不能在dead后就立
        // 即停止skillProcessor的更新,skillProcessor需要让其自动结束，并cleanup.
        skillProcessor.update(tpf);
        
        // 状态逻辑
        stateProcessor.update(tpf);
        
        if (!isDead()) {
            actionProcessor.update(tpf);
            talentProcessor.update(tpf);
            if (data.isAutoAi()) {
                logicProcessor.update(tpf);
            }
        }
    }
    
    @Override
    public ActorLogicProcessor getLogicProcessor() {
        return logicProcessor;
    }

    @Override
    public void setLogicProcessor(ActorLogicProcessor logicProcessor) {
        this.logicProcessor = logicProcessor;
    }

    @Override
    public ActionProcessor getActionProcessor() {
        return actionProcessor;
    }

    @Override
    public void setActionProcessor(ActionProcessor actionProcessor) {
        this.actionProcessor = actionProcessor;
    }

    /**
     * 该方法只由SkillProcessor调用。其它方法不要偿试调用。
     * 执行技能统一由SkillService调配
     * @return 
     */
    @Override
    public SkillProcessor getSkillProcessor() {
        return skillProcessor;
    }

    @Override
    public void setSkillProcessor(SkillProcessor skillProcessor) {
        this.skillProcessor = skillProcessor;
    }
    
    /**
     * 该方法只由StateService调用，其它方法不要偿试调用。
     * 查询状态统一由StateService执行
     * @return 
     */
    @Override
    public StateProcessor getStateProcessor() {
        return stateProcessor;
    }

    @Override
    public void setStateProcessor(StateProcessor stateProcessor) {
        this.stateProcessor = stateProcessor;
    }
    
    @Override
    public ResistProcessor getResistProcessor() {
        return resistProcessor;
    }

    @Override
    public void setResistProcessor(ResistProcessor resistProcessor) {
        this.resistProcessor = resistProcessor;
    }

    @Override
    public ChannelProcessor getChannelProcessor() {
        return channelProcessor;
    }

    @Override
    public void setChannelProcessor(ChannelProcessor channelProcessor) {
        this.channelProcessor = channelProcessor;
    }

    @Override
    public TalentProcessor getTalentProcessor() {
        return talentProcessor;
    }

    @Override
    public void setTalentProcessor(TalentProcessor talentProcessor) {
        this.talentProcessor = talentProcessor;
    }
    
    @Override
    public void setMass(float mass) {
        this.mass = mass;
        this.rigidBody.setMass(mass);
    }
    
    @Override
    public float getMass() {
        return mass;
    }
    
    public void setCollisionShape(CollisionShape collisionShape) {
        rigidBody.setCollisionShape(collisionShape);
    }

    @Override
    public Spatial getModel() {
        return spatial;
    }

    @Override
    public void setLocation(Vector3f location) {
        super.setPhysicsLocation(location);
//         TODO: 重要，保证了位置的实时正确，但可能需要优化。
        spatial.setLocalTranslation(location);
    }

    @Override
    public Vector3f getLocation() {
        return location;
    }
    
    @Override
    public void setRotation(Quaternion quaternion) {
        super.setPhysicsRotation(quaternion);
    }

    /**
     * 获取目标位置与当前角色的正前方的夹角度数.简单的说,即当前角色要转多少度才能正视
     * 到position位
     * @param position
     * @return 返回值为角度,不是弧度
     */
    @Override
    public float getViewAngle(Vector3f position) {
//        TempVars temp = TempVars.get();
//        Vector3f view = temp.vect2.set(getViewDirection()).normalizeLocal();
//        Vector3f dir = position.subtract(spatial.getWorldTranslation(), temp.vect1).normalizeLocal();
//        float angle = view.angleBetween(dir) * FastMath.RAD_TO_DEG;
//        temp.release();
//        return angle;
        
        // 优化性能
        TempVars tv = TempVars.get();
        Vector3f view = tv.vect1.set(getViewDirection()).normalizeLocal();
        Vector3f dir = tv.vect2.set(position).subtractLocal(spatial.getWorldTranslation()).normalizeLocal();
        float dot = dir.dot(view);
        float angle = 90;
        if (dot > 0) {
            angle = (1.0f - dot) * 90;
        } else if (dot < 0) {
            angle = -dot * 90 + 90;
        } else {
//            angle = 90;
        }
        tv.release();
        return angle;
    }
    
    @Override
    public boolean isDead() {
        return actorService.isDead(this);
    }

    @Override
    public void reborn() {
        actorNetwork.reborn(this);
    }
    
    @Override
    public boolean isEnemy(Actor target) {
//        return actorService.getGroup(this) != actorService.getGroup(target);
        return actorService.isEnemy(this, target);
    }

    @Override
    public float getDistance(Vector3f position) {
        return spatial.getWorldTranslation().distance(position);
    }
    
    @Override
    public float getDistance(Actor target) {
        return spatial.getWorldTranslation().distance(target.getModel().getWorldTranslation());
    }

    @Override
    public float getDistanceSquared(Actor target) {
        return spatial.getWorldTranslation().distanceSquared(target.getModel().getWorldTranslation());
    }
    
    @Override
    public void getNearActor(float distance, List<Actor> store) {
        List<Actor> assailable = LY.getPlayState().getActors();
        for (Actor s : assailable) {
            if (s.getDistance(spatial.getWorldTranslation()) <= distance) {
                store.add(s);
            }
        }
    }
    
    // remove
//    @Override
//    public void playDead() {
//        skillNetwork.playDead(this, null, false);
//    }

    @Override
    public void setPlayer(boolean player) {
        this.player = player;
    }

    @Override
    public boolean isPlayer() {
        return this.player;
    }

    @Override
    public List<ActorListener> getActorListeners() {
        if (actorListeners == null) {
            actorListeners = new SafeArrayList<ActorListener>(ActorListener.class);
        }
        return actorListeners;
    }

    @Override
    public List<SkillListener> getSkillListeners() {
        if (skillListeners == null) {
            skillListeners = new SafeArrayList<SkillListener>(SkillListener.class);
        }
        return skillListeners;
    }

    @Override
    public void addActorPhysicsListener(PhysicsListener apl) {
        if (actorPhysicsListeners == null) {
            actorPhysicsListeners = new SafeArrayList<PhysicsListener>(PhysicsListener.class);
        }
        if (apl != null && !actorPhysicsListeners.contains(apl)) {
            actorPhysicsListeners.add(apl);
        }
        checkEnablePhysicsListener(getPhysicsSpace());
    }

    @Override
    public boolean removeActorPhysicsListener(PhysicsListener apl) {
        if (actorPhysicsListeners == null)
            return false;
        boolean result = actorPhysicsListeners.remove(apl);
        checkEnablePhysicsListener(getPhysicsSpace());
        return result;
    }
    
    // 检查是否应该添加PhysicsListener还是应该移除。
    private void checkEnablePhysicsListener(PhysicsSpace space) {
        if (space == null)
            return;
        
        if (!isEnabled()) {
            if (physicsListenerAdded) {
                space.removeCollisionListener(this);
                physicsListenerAdded = false;
                return;
            }
        }
        
        if (actorPhysicsListeners != null) {
            if (actorPhysicsListeners.size() > 0) {
                if (!physicsListenerAdded) {
                    space.addCollisionListener(this);
                    physicsListenerAdded = true;
                }
            } else {
                if (physicsListenerAdded) {
                    space.removeCollisionListener(this);
                    physicsListenerAdded = false;
                }
            }
        }
    }

    @Override
    public void addItemListener(ItemListener itemListener) {
        if (itemListeners == null) {
            itemListeners = new ArrayList<ItemListener>();
        }
        if (!itemListeners.contains(itemListener)) {
            itemListeners.add(itemListener);
        }
    }

    @Override
    public boolean removeItemListener(ItemListener itemListener) {
        return itemListeners != null && itemListeners.remove(itemListener);
    }

    @Override
    public List<ItemListener> getItemListeners() {
        return itemListeners;
    }

    @Override
    public void addSkinListener(SkinListener skinListener) {
        if (skinListeners == null) {
            skinListeners = new ArrayList<SkinListener>();
        }
        if (!skinListeners.contains(skinListener)) {
            skinListeners.add(skinListener);
        }
    }

    @Override
    public boolean removeSkinListener(SkinListener skinListener) {
        return skinListeners != null && skinListeners.remove(skinListener);
    }

    @Override
    public List<SkinListener> getSkinListeners() {
        return skinListeners;
    }

    @Override
    public void addTalentListener(TalentListener talentListener) {
        if (talentListeners == null) {
            talentListeners = new ArrayList<TalentListener>();
        }
        if (!talentListeners.contains(talentListener)) {
            talentListeners.add(talentListener);
        }
    }

    @Override
    public boolean removeTalentListener(TalentListener talentListener) {
        return talentListeners != null && talentListeners.remove(talentListener);
    }

    @Override
    public List<TalentListener> getTalentListeners() {
        return talentListeners;
    }

    @Override
    public void addTaskListener(TaskListener taskListener) {
        if (taskListeners == null) {
            taskListeners = new ArrayList<TaskListener>();
        }
        if (!taskListeners.contains(taskListener)) {
            taskListeners.add(taskListener);
        }
    }

    @Override
    public boolean removeTaskListener(TaskListener taskListener) {
        return taskListeners != null && taskListeners.remove(taskListener);
    }

    @Override
    public List<TaskListener> getTaskListeners() {
        return taskListeners;
    }

    @Override
    public List<StateListener> getStateListeners() {
        return stateListeners;
    }

    @Override
    public void setStateListeners(List<StateListener> stateListeners) {
        this.stateListeners = stateListeners;
    }
    
    // 碰撞侦听handler
    @Override
    public void collision(PhysicsCollisionEvent event) {
        if (actorPhysicsListeners == null)
            return;
        // 触发碰撞监听
        Object objA = event.getObjectA().getUserObject();
        Object objB = event.getObjectB().getUserObject();
        Object self = null;
        Object other = null;
        if (objA == spatial) {
            self = objA;
            other = objB;
        } else if (objB == spatial) {
            self = objB;
            other = objA;
        }
        // 如果碰撞的A和B都不是self（当前角色）则不处理侦听。注：侦听是全局的
        if (self == null) {
            return;
        }
        
        for (PhysicsListener apl : actorPhysicsListeners) {
            apl.collision(this, other, event);
        }
    }

    @Override
    public boolean isDefending() {
        return skillProcessor.isDefending();
    }
    
    @Override
    public boolean isDucking() {
        return skillProcessor.isDucking();
    }
    
    @Override
    public boolean isAttacking() {
        return skillProcessor.isAttacking();
    }
    
    @Override
    public boolean isRunning() {
        return skillProcessor.isRunning();
    }
    
    @Override
    public boolean isWaiting() {
        return skillProcessor.isWaiting();
    }

    /**
     * TODO:需要把localForwad属性也同步到客户端
     * @param vec 
     */
    @Override
    public void setLocalForward(Vector3f vec) {
        this.localForward.set(vec);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        checkEnablePhysicsListener(getPhysicsSpace());
    }
    
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public void faceTo(Vector3f position) {
        // 静态角色不能朝向
        if (mass <= 0) {
            return;
        }
        TempVars tv = TempVars.get();
        position.subtract(getModel().getWorldTranslation(), tv.vect1);
        setViewDirection(tv.vect1.normalizeLocal());
        tv.release();
    }
    
    @Override
    public Vector3f getViewDirection() {
        return super.getViewDirection();
    }
    
    @Override
    public void setViewDirection(Vector3f vec) {
        TempVars tv = TempVars.get();
        Vector3f viewDir = tv.vect1.set(vec).normalizeLocal();
        super.setViewDirection(viewDir);

        // 确保在没有physics的时候仍能够朝向正确目标
        if (!enabled) {
            Quaternion qua = new Quaternion();
            qua.lookAt(viewDir, Vector3f.UNIT_Y);
//            qua.mult(xxx) // TODO: 需要加上localForward的影响
            spatial.setLocalRotation(qua);
        }
        tv.release();
    }

//    @Override
//    public void say(String mess) {
//        actorNetwork.speak(this, mess, -1);
//    }

    @Override
    public void setKinematic(boolean kinematic) {
        this.rigidBody.setKinematic(kinematic);
    }

    @Override
    public boolean isKinematic() {
        return this.rigidBody.isKinematic();
    }

    @Override
    public Chat getChat() {
        return chat;
    }

    @Override
    public void setChat(Chat chat) {
        this.chat = chat;
    }
    
    // remove20160523,不要再使用这个方法
//    @Override
//    public void destory() {
//        logicProcessor.cleanup();
//        actionProcessor.cleanup();
//        skillProcessor.cleanup();
//        stateProcessor.cleanup();
//        if (actorListeners != null) {
//            actorListeners.clear();
//        }
//        if (skillListeners != null) {
//            skillListeners.clear();
//        }
//        if (actorPhysicsListeners != null) {
//            actorPhysicsListeners.clear();
//        }
//        
//        // 移除侦听器
//        PhysicsSpace ps = getPhysicsSpace();
//        if (ps != null && physicsListenerAdded) {
//            ps.removeCollisionListener(this);
//            physicsListenerAdded = false;
//        }
//        
//        // 暂不处理以下两项清理,还不完善
////        resistProcessor   // ignore
////        channelProcessor  // ignore
//    }
    
    @Override
    public void cleanup() {
        logicProcessor.cleanup();
        actionProcessor.cleanup();
        skillProcessor.cleanup();
        stateProcessor.cleanup();
        if (actorListeners != null) {
            actorListeners.clear();
        }
        if (skillListeners != null) {
            skillListeners.clear();
        }
        if (actorPhysicsListeners != null) {
            actorPhysicsListeners.clear();
        }
        
        // 移除侦听器
        PhysicsSpace ps = getPhysicsSpace();
        if (ps != null && physicsListenerAdded) {
            ps.removeCollisionListener(this);
            physicsListenerAdded = false;
        }
        
        // 暂不处理以下两项清理,还不完善
//        resistProcessor   // ignore
//        channelProcessor  // ignore
    }

    @Override
    protected void addPhysics(PhysicsSpace space) {
        super.addPhysics(space);
        checkEnablePhysicsListener(space);
    }

    @Override
    protected void removePhysics(PhysicsSpace space) {
        super.removePhysics(space);
        checkEnablePhysicsListener(space);
    }

    /**
     * 获取角色的任务列表，如果不存在任务列表则返回空列表
     * @return 
     */
    @Override
    public List<Task> getTasks() {
        if (tasks == null) {
            tasks = new ArrayList<Task>();
        }
        return tasks;
    }
    
}
